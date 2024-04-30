package com.example.todo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth


class LoginActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()

        val textViewRegister = findViewById<TextView>(R.id.textViewRegister)
        textViewRegister.setOnClickListener{
            startActivity(Intent(this, SignupActivity::class.java))
            finish()
        }
        val forgotpassword = findViewById<TextView>(R.id.forgotpassword)
        forgotpassword.setOnClickListener{
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
            finish()
        }
        val editTextEmailAddress =findViewById<EditText>(R.id.editTextEmailAddress)
        val editTextPassword = findViewById<EditText>(R.id.editTextPassword)
        val LoginButton = findViewById<Button>(R.id.buttonLogin)

        LoginButton.setOnClickListener {
            val email = editTextEmailAddress.text.toString()
            val pass = editTextPassword.text.toString()
            if (email.isNotEmpty() && pass.isNotEmpty()) {

                auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener {
                    if (it.isSuccessful) {
                        val intent = Intent(this, ToDoActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(this, it.exception.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Empty Fields Are not Allowed !!", Toast.LENGTH_SHORT).show()
            }
        }
    }
//    override fun onStart() {
//        super.onStart()
//
//        if(auth.currentUser != null){
//            val intent = Intent(this, ToDoActivity::class.java)
//            startActivity(intent)
//        }
//    }

}