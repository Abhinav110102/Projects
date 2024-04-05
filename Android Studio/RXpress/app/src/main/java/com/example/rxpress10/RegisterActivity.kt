package com.example.rxpress10

import android.app.Activity
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.nfc.Tag
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat.startActivity
import com.example.rxpress10.model.UserInfo
import com.example.rxpress10.ui.prescriptions.model.Users
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    //DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://rxpress-27f8a-default-rtdb.firebaseio.com/");

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var  auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    companion object {
        private const val RC_SIGN_IN =120
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        title="Register"

        auth= FirebaseAuth.getInstance()

//        val googleButton: ImageButton = findViewById(R.id.googleSignIn)
//        val text: TextView = findViewById(R.id.textView3)
//        googleButton.visibility = View.GONE
//        text.visibility = View.GONE

//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(R.string.default_web_client_id))
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(this,gso)
//        auth= FirebaseAuth.getInstance()
//
//        gSignInBtn.setOnClickListener{
//            signIn()
//        }
    }
//    private fun signIn() {
//        val signInIntent = googleSignInClient.signInIntent
//        startActivityForResult(signInIntent, RC_SIGN_IN)
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(requestCode == RC_SIGN_IN){
//            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
//            val exception = task.exception
//            if(task.isSuccessful){
//                try {
//                val account = task.getResult(ApiException::class.java)!!
//                Log.d("SigInActivity"," firebaseAuthWithGoogle:" + account.id )
//                firebaseAuthWithGoogle(account.idToken!!)
//                } catch (e:ApiException) {
//                Log.w("SignInActivity", "Google sign in failed", e)
//                }
//            }else {
//                Log.w("SignInActivity",exception.toString())
//            }
//        }
//    }
//
//private fun firebaseAuthWithGoogle(idToken: String){
//    val credential = GoogleAuthProvider.getCredential(idToken,null)
//    auth.signInWithCredential(credential)
//        .addOnCompleteListener(this){ task ->
//            if(task.isSuccessful) {
//                Log.d("SigninActivity", "signInWithCredential:success")
//                val intent =Intent(this,MenuActivity::class.java)
//                startActivity(intent)
//            }
//            else{
//                Log.w("SigninActivity", "signInWithCredential:failure", task.exception)
//            }
//
//        }
//}


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

    fun register(view: View) {
        val email = editTextEmailAddress.text.toString()
        val password = editTextPassword.text.toString()
        val confirmPassword = editTextConfirmPassword.text.toString()
        val lastname = LastName.text.toString()
        val firstname = FirstName.text.toString()



        // Check if field are empty and if password are matching
        if (firstname.isNotEmpty() && lastname.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()) {
            //Toast.makeText(this,"Please fill all fields", Toast.LENGTH_SHORT).show();

            // password does not equal the confirm password
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
//                val intent = Intent(this, RegisterActivity::class.java)
//                startActivity(intent)
                editTextConfirmPassword.text!!.clear()
                // buttonRegister.isClickable = false

                // all is filled and confirmed passwords go to menu activity
            } else {
                Toast.makeText(this, "Password are matching", Toast.LENGTH_SHORT).show()

                auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                // Check if the User has verified their email and also make sure the user data is stored in Firebase realtime database
                        auth.currentUser?.sendEmailVerification()
                            ?.addOnSuccessListener {
                                var userId = auth.currentUser?.uid
                                val user = Users("Your")
                                database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/ChildUsers")

                                database.child("Primary").setValue(user).addOnSuccessListener {
                                    Log.d("RegisterActivity", "Adding Primary to Database on Register")
                                }

                                Toast.makeText(this, "Please Verify your Email", Toast.LENGTH_SHORT)
                                    .show()



//                                auth = FirebaseAuth.getInstance()


                                println(userId)
                                database =
                                    FirebaseDatabase.getInstance().getReference("users/" + userId + "/UserInfo")
                                val userInfo = createUserInfo(firstname, lastname, email, "n/a", "n/a", "n/a")

                                Log.d("firstName", "$firstname")
                                Log.d("userInfo", "$userInfo")


                                database.setValue(userInfo).addOnSuccessListener {

                                    Toast.makeText(this, "$firstname has been added", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, MenuActivity::class.java)
                                    startActivity(intent)

                                }.addOnFailureListener {
                                        Toast.makeText(this, "Failed to add user", Toast.LENGTH_SHORT).show()
                                    }
                                println(auth.currentUser)

                                //val intent= Intent(this,MenuActivity::class.java)
                                //startActivity(intent)
                            }
//                    ?.addOnFailureListener(){
//                        Toast.makeText(this,"" ,Toast.LENGTH_SHORT).show()
//                    }
//                        val intent = Intent(this, MenuActivity::class.java)
//                        intent.putExtra("firstN", firstname)
//                        intent.putExtra("lastN", lastname)
//                        intent.putExtra("email", email)
//                        startActivity(intent)
                        finish()
                    }
                }
                    .addOnFailureListener { exception ->
                        Toast.makeText(
                            applicationContext,
                            exception.localizedMessage,
                            Toast.LENGTH_LONG
                        ).show()
                    }



            }
        }// Check if All field are entered if a field is missing a Toast Message will be sent which alert the user to fill in that certain field that are missing
        else {

            if (firstname.isEmpty()) {
                FirstName.text!!.clear()
                Toast.makeText(this, "First Name is required", Toast.LENGTH_SHORT).show()
            } else if (lastname.isEmpty()) {
                LastName.text!!.clear()
                Toast.makeText(this, "Last Name is required", Toast.LENGTH_SHORT).show()
            } else if (email.isEmpty()) {
                editTextEmailAddress.text!!.clear()
                Toast.makeText(this, "Email is required", Toast.LENGTH_SHORT).show()
            } else if (password.isEmpty()) {
                editTextPassword.text!!.clear()
                Toast.makeText(this, "Password is required", Toast.LENGTH_SHORT).show()
            } else if (confirmPassword.isEmpty()) {
                editTextConfirmPassword.text!!.clear()
                Toast.makeText(this, "Confirm Password is required", Toast.LENGTH_SHORT).show()
            } else {

                val intent = Intent(this, RegisterActivity::class.java)
                startActivity(intent)
            }

        }
    }

//    }

    fun goToLogin(view: View){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
    fun goToWelcome(view: View){
        val intent= Intent(this,WelcomeActivity::class.java)
        startActivity(intent)
    }
}