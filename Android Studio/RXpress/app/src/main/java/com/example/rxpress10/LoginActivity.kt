package com.example.rxpress10

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.IntentSender
import android.media.Image
import android.util.Log
import android.view.View

import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import android.widget.*

import com.google.firebase.database.FirebaseDatabase

import androidx.core.app.ActivityCompat.startIntentSenderForResult
import com.google.firebase.auth.GoogleAuthProvider

import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    //private lateinit var textView: TextView
    private lateinit var googleSigninClient : GoogleSignInClient

    private lateinit var  auth: FirebaseAuth

    private lateinit var database: FirebaseDatabase
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val REQ_ONE_TAP = 2  // Can be any integer unique to the Activity
    private var showOneTapUI = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        title="Login"
        auth= FirebaseAuth.getInstance()

        forgotpassword.setOnClickListener{
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }
 // Check if user already has account determine if they can login into the app
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            REQ_ONE_TAP -> {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(data)
                    val idToken = credential.googleIdToken
                    val username = credential.id
                    val password = credential.password
                    when {
                        idToken != null -> {
                            // Got an ID token from Google. Use it to authenticate
                            // with your backend.
                            Log.d(TAG, "Got ID token.")
                        }
                        password != null -> {
                            // Got a saved username and password. Use them to authenticate
                            // with your backend.
                            Log.d(TAG, "Got password.")
                        }
                        else -> {
                            // Shouldn't happen.
                            Log.d(TAG, "No ID token or password!")
                        }
                    }
                } catch (e: ApiException) {
                        showOneTapUI = false
                }
            }
        }
    }
// Check if user email is verified if not send them a message to verify their email
    fun login(view: View){
        val email=editTextEmailAddress.text.toString()
        val password=editTextPassword.text.toString()
        if(password.isNullOrEmpty()){
            Toast.makeText(this, "Must enter a password.", Toast.LENGTH_SHORT).show()
        } else {
            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val intent = Intent(this, MenuActivity::class.java)
                    val verification = auth.currentUser?.isEmailVerified
                    if (verification == true) {
                        finish()
                    } else {
                        Toast.makeText(this, "Please verify your Email!", Toast.LENGTH_SHORT).show()
                    }
                    val user = auth.currentUser
                    startActivity(intent)
                    finish()
                }
            }.addOnFailureListener { exception ->
                Toast.makeText(applicationContext, exception.localizedMessage, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    fun goToRegister(view: View){
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
    fun goToWelcome2(view: View){
        val intent= Intent(this,WelcomeActivity::class.java)
        startActivity(intent)
    }
}