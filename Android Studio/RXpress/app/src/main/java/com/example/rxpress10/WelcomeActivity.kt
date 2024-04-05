package com.example.rxpress10

import android.content.Intent
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Shader
import android.os.Bundle
import android.text.TextPaint
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.rxpress10.model.UserInfo
import com.example.rxpress10.ui.prescriptions.model.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_welcome.*


class WelcomeActivity : AppCompatActivity() {
    private lateinit var  auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var database: DatabaseReference

    companion object {
        private const val RC_SIGN_IN = 120
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
       // auth= FirebaseAuth.getInstance()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this,gso)
        //FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(googleSignInClient.))

        auth= FirebaseAuth.getInstance()
        //auth.signInWithCredential(GoogleAuthProvider.getCredential(GoogleSignIn.getSignedInAccountFromIntent(data)))
        println(auth.currentUser?.uid)
        gSignInBtn.setOnClickListener{
            signIn()
        }
    }
    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, WelcomeActivity.RC_SIGN_IN)
    }

    fun test(): Int{
       return 1 + 2
    }
// Allow User to Sign up with google
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //var account =task.getResult(ApiException::class.java)
        var email = ""
        var firstname =""
        var lastname =""
        super.onActivityResult(requestCode, resultCode, data)
        //var userId = auth.currentUser?.uid
        //auth.signInWithCredential(GoogleAuthProvider.getCredential(GoogleSignIn.getSignedInAccountFromIntent(data),null ))
        //println(userId)
        if(requestCode == WelcomeActivity.RC_SIGN_IN){
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try{
                var userId = auth.currentUser?.uid
                val account =task.getResult(ApiException::class.java)
                    email = account.email!!
                 firstname =account.givenName!!
                 lastname =account.familyName!!
               // println(userId)
                //val id = account.uid
                println(userId)
                val database = FirebaseDatabase.getInstance().getReference("users/"+ userId + "/UserInfo")
                println(email)
                println(firstname)
                println(lastname)

            val userInfo = createUserInfo(firstname!!, lastname!!, email!!, "n/a", "n/a", "n/a")
                database.child(firstname).setValue(userInfo).addOnSuccessListener {
                    println(firstname)
                    val account =task.getResult(ApiException::class.java)
                    val email = account.email
                    val firstname =account.givenName
                    val lastname =account.familyName
                    println(firstname)
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)

                }
                    .addOnFailureListener {
                        //Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                    }
                println(auth.currentUser)

            }catch (e: ApiException) {}

            val exception = task.exception
            if(task.isSuccessful){
                try {
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("SignInActivity"," firebaseAuthWithGoogle:" + account.id )
                    firebaseAuthWithGoogle(account.idToken!! , email , firstname , lastname)
                } catch (e: ApiException) {
                    Log.w("SignInActivity", "Google sign in failed", e)
                }
            }else {
                Log.w("SignInActivity",exception.toString())
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken:String , email: String ,firstname:String , lastname : String ){
        val credential = GoogleAuthProvider.getCredential(idToken,null)
        val userInfo = createUserInfo(firstname!!, lastname!!, email!!, "n/a", "n/a", "n/a")
//        database =FirebaseDatabase.getInstance().getReference("users/"+ userId + "/UserInfo")

        auth.signInWithCredential(credential)
            .addOnCompleteListener(this){ task ->

                if(task.isSuccessful) {
                    println("HERE USER ID "+ auth.currentUser?.uid)
                    val userId = auth.currentUser?.uid
                    val user = Users("Your")

                    database = FirebaseDatabase.getInstance().getReference("users/"+ userId)

                    database.addListenerForSingleValueEvent(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(!snapshot.hasChild("UserInfo")){

                                database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/ChildUsers")

                                database.child("Primary").setValue(user).addOnSuccessListener {
                                    Log.d("RegisterActivity", "Adding Primary to Database on Register")
                                }
                                database =FirebaseDatabase.getInstance().getReference("users/"+ userId + "/UserInfo")
                                database.setValue(userInfo).addOnSuccessListener {
                                    println(firstname)
                                    //Toast.makeText(this, "$firstname has been added", Toast.LENGTH_SHORT).show()
                                    Log.d("Success", "$firstname has been added")


                                }
                                    .addOnFailureListener {
//                                        Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                                        Log.d("Failed", "Failed to add user")
                                    }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })
                    val intent = Intent(this, MenuActivity::class.java)
                    startActivity(intent)

                    println(auth.currentUser)

                    Log.d("SigninActivity", "signInWithCredential:success")
                    // take them to profileactivity
                    //val intent =Intent(this,MenuActivity::class.java)
                    //startActivity(intent)
                }
                else{
                    Log.w("SigninActivity", "signInWithCredential:failure", task.exception)
                }

            }
    }

    // need this part
    fun createUserInfo(
        firstname: String, lastName: String, email: String,  dob: String,
        gender: String, pronouns: String
    ): UserInfo {
        return UserInfo(
            firstname,
            lastName,
            email,
            dob,
            gender,
            pronouns
        )
    }



    fun goToLogin(view: View){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }

    fun goToRegister(view: View){
        val intent= Intent(this,RegisterActivity::class.java)
        startActivity(intent)
    }
}