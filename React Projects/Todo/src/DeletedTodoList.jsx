export function DeletedTodoList({ deletedTodos }) {
  return (
    <div className="deleted-todos">
      <h2 className="header">Deleted Todos</h2>
      <ul className="list">
      {deletedTodos.length === 0 && "No Deleted Todos"}
        {deletedTodos.map((todo) => (
          <li key={todo.id}>{todo.title}</li>
        ))}
      </ul>
    </div>
  );
}
