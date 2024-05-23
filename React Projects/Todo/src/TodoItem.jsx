export function TodoItem({ completed, id, title, toggleTodo, deleteTodo, completeTodo }) {
  return (
    <li>
      <label>
        <input
          type="checkbox"
          checked={completed}
          onChange={(e) => toggleTodo(id, e.target.checked)}
        />
        {title}
      </label>
      <button onClick={() => completeTodo(id)} className="btn btn-complete">
        Completed
      </button>
      <button onClick={() => deleteTodo(id)} className="btn btn-danger">
        Delete
      </button>
      
    </li>
  );
}
