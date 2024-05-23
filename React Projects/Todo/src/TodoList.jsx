import { TodoItem } from "./TodoItem";

export function TodoList({ todos, toggleTodo, deleteTodo ,completeTodo}) {
  return (
    <ul className="list">
      {todos.length === 0 && "No Todos"}
      {todos.map((todo) => {
        return (
          <TodoItem
            {...todo}
            key={todo.id}
            toggleTodo={toggleTodo}
            deleteTodo={deleteTodo}
            completeTodo={completeTodo}
          />
        );
      })}
    </ul>
  );
}
