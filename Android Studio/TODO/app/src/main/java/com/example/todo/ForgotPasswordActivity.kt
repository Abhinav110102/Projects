package com.example.todo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity: AppCompatActivity(){
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth= FirebaseAuth.getInstance()
        val et_forgot_email = findViewById<EditText>(R.id.et_forgot_email)
        val btn_submit = findViewById<Button>(R.id.btn_submit)
// Check if user entered a valid email that is registered in firebase and send a reset link to their email
        btn_submit.setOnClickListener {
            val email:String =et_forgot_email.text.toString().trim{it <= ' '}
            if(email.isEmpty()){
                Toast.makeText(this@ForgotPasswordActivity,"Please enter email", Toast.LENGTH_SHORT).show()
            }
            else{
                FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    if(task.isSuccessful){
                        Toast.makeText(this@ForgotPasswordActivity,"Email sent successfully to reset password", Toast.LENGTH_LONG).show()
                        finish()
                    }
                    else{
                        Toast.makeText(this@ForgotPasswordActivity,task.exception!!.message.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}