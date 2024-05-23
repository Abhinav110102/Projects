export function CompletedTodoList({ completedTodos }) {
    return (
      <div>
        <h2>Completed Todos</h2>
        <ul className="list">
          {completedTodos.length === 0 && "No Completed Todos"}
          {completedTodos.map((todo) => (
            <li key={todo.id}>{todo.title}</li>
          ))}
        </ul>
      </div>
    );
  }