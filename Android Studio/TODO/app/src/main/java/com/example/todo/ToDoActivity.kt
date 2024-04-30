package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore

class ToDoActivity : AppCompatActivity(){
    private lateinit var logoutButton: Button
    private lateinit var todoEditText: EditText
    private lateinit var addTodoButton: Button
    private lateinit var todoListView: ListView
    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var todoAdapter: TodoAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_todo)

        todoEditText = findViewById(R.id.todoEditText)
        addTodoButton = findViewById(R.id.addTodoButton)
        todoListView = findViewById(R.id.todoListView)
        logoutButton = findViewById(R.id.logoutbutton)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val userId = auth.currentUser?.uid ?: ""
        val todosRef = firestore.collection("users").document(userId).collection("todos")

        val todoList = mutableListOf<Todo>()
        todoAdapter = TodoAdapter(this, todoList)
        todoListView.adapter = todoAdapter

        addTodoButton.setOnClickListener {
            val todoText = todoEditText.text.toString().trim()
            if (todoText.isNotEmpty()) {
                val todo = hashMapOf(
                    "text" to todoText,
                    "completed" to false
                )
                todosRef.add(todo)
                    .addOnSuccessListener {
                        todoEditText.setText("")
                        Toast.makeText(this, "Task Added ", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to add Task ", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        todoListView.setOnItemClickListener { parent, view, position, id ->
            val todoItem = todoAdapter.getItem(position)
            todoItem?.let {
                todosRef.document(todoItem.id!!).delete()
                    .addOnSuccessListener {

                        Toast.makeText(this, "Task Completed", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(this, "Task could not be deleted", Toast.LENGTH_SHORT).show()
                    }
            }
        }
        todosRef.addSnapshotListener { value, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            val newTodoList = mutableListOf<Todo>()
            value?.forEach { document ->
                val todoText = document.getString("text")
                val todoId = document.id
                if(todoText!= null && todoId != null) {
                    val todo = Todo(todoId, todoText)
                    newTodoList.add(todo)
                }
            }
            todoAdapter.clear()
            todoAdapter.addAll(newTodoList)
            todoAdapter.notifyDataSetChanged()
        }
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

}

