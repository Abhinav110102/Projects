package com.example.rxpress10

import android.os.Bundle
import android.view.Menu
import android.widget.Toast
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.rxpress10.databinding.ActivityMenuBinding
import com.example.rxpress10.model.UserInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MenuActivity : AppCompatActivity() {

    //This class sets up the navigation drawer as well as saving info for users
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMenuBinding
    private lateinit var  auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var userInfo: UserInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMenu.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
 /**       binding.appBarMenu.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
 */
        val firstN = intent.getStringExtra("firstN")
        val lastN = intent.getStringExtra("lastN")
        val email = intent.getStringExtra("email")

        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database =
            FirebaseDatabase.getInstance().getReference("users/" + userId + "UserInfo")

        database.get().addOnSuccessListener {

            userInfo = createUserInfo(
                firstN!!, lastN!!, email!!, "NA", "NA", "NA"
            )
            database.setValue(userInfo).addOnSuccessListener {
                Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT)
                    .show()
            }.addOnFailureListener {
                Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
            }
        }

        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_faq, R.id.nav_interactions, R.id.nav_prescription_home, R.id.nav_calendar_fragment, R.id.nav_settings
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //Creates user info instance
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

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_menu)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}