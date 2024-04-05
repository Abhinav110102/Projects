package com.example.rxpress10

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_forgot_password.*
class ForgotPasswordActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        auth= FirebaseAuth.getInstance()
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


