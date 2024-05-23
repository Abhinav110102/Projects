import "./styles.css";
import { useState, useEffect } from "react";
import { NewTodoForm } from "./NewTodoForm.jsx";
import { TodoList } from "./TodoList.jsx";
import { DeletedTodoList } from "./DeletedTodoList.jsx";
import { CompletedTodoList } from "./CompletedTodoList.jsx";


export default function App() {
  const [todos, setTodos] = useState(() => {
    const localvalue = localStorage.getItem("ITEMS");
    if (localvalue == null) return [];
    return JSON.parse(localvalue);
  });
  const [deletedTodos, setDeletedTodos] = useState([]);
  const [deletedTodoIds, setDeletedTodoIds] = useState(new Set());
  const [completedTodos, setCompletedTodos] = useState([]);

  useEffect(() => {
    localStorage.setItem("ITEMS", JSON.stringify(todos));
  }, [todos]);

  function addTodo(title) {
    setTodos((currentTodos) => {
      return [
        ...currentTodos,
        { id: crypto.randomUUID(), title, completed: false },
      ];
    });
  }

  function toggleTodo(id, completed) {
    setTodos((currentTodos) => {
      return currentTodos.map((todo) => {
        if (todo.id === id) {
          return { ...todo, completed };
        }
        return todo;
      });
    });
  }
  function deleteTodo(id) {
    setTodos((currentTodos) => {
      const todoToDelete = currentTodos.find((todo) => todo.id === id);
      if (todoToDelete && !deletedTodoIds.has(id)) {
        setDeletedTodos((currentDeletedTodos) => {
          if (!currentDeletedTodos.some((todo) => todo.id === id)) {
            return [todoToDelete, ...currentDeletedTodos];
          }
          return currentDeletedTodos;
        });
        setDeletedTodoIds((currentIds) => new Set([...currentIds, id]));
      }
      return currentTodos.filter((todo) => todo.id !== id);
    });
  }

  function completeTodo(id) {
    setTodos((currentTodos) => {
      const todoToComplete = currentTodos.find((todo) => todo.id === id);
      if (todoToComplete && !completedTodos.some((todo) => todo.id === id)) {
        setCompletedTodos((currentCompletedTodos) => {
          if (!currentCompletedTodos.some((todo) => todo.id === id)) {
            return [todoToComplete, ...currentCompletedTodos];
          }
          return currentCompletedTodos;
        });
      }
      return currentTodos.filter((todo) => todo.id !== id);
    });
  }

  function restoreTodo() {
    setDeletedTodos((currentDeletedTodos) => {
      const [todoToRestore, ...remainingDeletedTodos] = currentDeletedTodos;
      if (
        todoToRestore &&
        !todos.some((todo) => todo.id === todoToRestore.id)
      ) {
        setTodos((currentTodos) => [todoToRestore, ...currentTodos]);
        setDeletedTodoIds((currentIds) => {
          const newIds = new Set(currentIds);
          newIds.delete(todoToRestore.id);
          return newIds;
        });
      }
      return remainingDeletedTodos;
    });
  }

  return (
    <>
      <NewTodoForm onSubmit={addTodo} />
      <h1 className="header">Todo List</h1>
      <TodoList todos={todos} toggleTodo={toggleTodo} deleteTodo={deleteTodo} completeTodo = {completeTodo} />
      <button
        className="btn btn-restore space-between"
        onClick={restoreTodo}
        disabled={deletedTodos.length === 0}
      >
        Restore Last Deleted Todo
      </button>
      <CompletedTodoList completedTodos={completedTodos} />
      <DeletedTodoList deletedTodos={deletedTodos} />
    </>
  );
}
