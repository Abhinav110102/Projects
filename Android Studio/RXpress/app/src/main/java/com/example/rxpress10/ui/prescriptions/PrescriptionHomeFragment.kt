package com.example.rxpress10.ui.prescriptions

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
//import com.example.rxpress10.SwipeGesture
import com.example.rxpress10.adapter.PrescriptionHomeAdapter
import com.example.rxpress10.data.PrescriptionData
import com.example.rxpress10.databinding.ActivityPrescriptionHomeFragmentBinding
import com.example.rxpress10.ui.interaction.CardAdapter
import com.example.rxpress10.ui.prescriptions.users.UserNameAdapter
import com.example.rxpress10.ui.prescriptions.model.Users
import com.example.rxpress10.ui.prescriptions.view.EditFragmentArgs
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.user_card.*
import kotlinx.android.synthetic.main.user_card.view.*

// Prescription home screen where the user can add up to 5 users and can click a user name to view
// their prescriptions
class PrescriptionHomeFragment :
    Fragment(com.example.rxpress10.R.layout.activity_prescription_home_fragment) {

    // initializing binding database and authentication
    private lateinit var binding: ActivityPrescriptionHomeFragmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private var count = 0

    // initializing the recyclerView and Adapters
    private lateinit var recyclerView: RecyclerView
    private lateinit var interactionAdapter: CardAdapter
    private var userArr = ArrayList<Users>()
    private var code = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionHomeFragmentBinding.inflate(layoutInflater)
        setUserRecyclerView()

        val newCount = setUserRecyclerView()
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val root: View = binding.root

        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers")

        // setting the users recycler view
        setUserRecyclerView()

        val newCount = setUserRecyclerView()

        // adding a user
        binding.addUserButton.setOnClickListener {

            // initializing the dialog layouts
            val builder = AlertDialog.Builder(context)
            val dialogLayout = layoutInflater.inflate(R.layout.add_user_dialog, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.edit_user)

            with(builder) {
                setTitle("Add User")
                setPositiveButton("Ok") { dialog, which ->
                    // add user to database
                    Log.d("RV Size", "$newCount")
                    // if the arrSize is less than six add the user to the array and
                    // set the recycler view to update
                    if (newCount < 6) {
                        val user = editText.text.toString()
                        addUser(user)
                        setUserRecyclerView()
                    } else {
                        Toast.makeText(context, "Only 6 users allowed", Toast.LENGTH_LONG).show()
                    }
                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("PrescriptionHomeFrag", "Negative button clicked")
                }

                setView(dialogLayout)
                show()
            }
        }

        // clears the array for duplicates
        userArr.clear()
        return root
    }


    override fun onResume() {
        super.onResume()
        setUserRecyclerView()
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
    }

    // used for old recycler view redundant now
    fun deleteMedication(medName: String) {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/$userId/Medications")
        database.child("${medName}").removeValue()
    }

    // adding the user to the database
    fun addUser(userName: String) {

        // initiialzing firebase reference
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        val re = "[^A-Za-z0-9 ]".toRegex()
        var dName = re.replace(userName, "")

        // checks for valid user names
        if (!userName.equals("Primary", true) && !userName.equals(
                "Your",
                true
            ) && !userName.equals("Your Prescriptions", true) && !dName.isNullOrEmpty()
        ) {

            // creating object of Users and adding them to the array
            val user = Users(userName)
            userArr.add(user)

            database =
                FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers")

            // all alphanumeric numbers will be replace the original userName if it has
            // non alphanumric chars for the database name.
            val re = "[^A-Za-z0-9 ]".toRegex()
            val dName = re.replace(userName, "")

            database.child(dName).setValue(user).addOnSuccessListener {
                Toast.makeText(context, "$userName Has Been Added", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Invalid User Entered", Toast.LENGTH_SHORT).show()
        }
    }

    // setting the recycler view by using an array adapter
    fun setUserRecyclerView(): Int {

        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers")

        database.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {

                userArr.clear()
                if (snapshot.exists()) {
                    for (userSnap in snapshot.children) {
                        val userData = userSnap.getValue(Users::class.java)
                        // renaming your to your prescriptions
                        if (userData == Users("Your")) {
                            userArr.add(Users("Your Prescriptions"))
                            userArr.remove(Users("Your"))
                        } else
                            userArr.add(userData!!)
                        Log.d("USER_RV", "$userData")
                    }

                    // inflating the recyclerView
                    val recyclerView = binding.userNameRV

                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    // initializing the adapter with the array
                    var adapter = UserNameAdapter(
                        PrescriptionHomeFragment(),
                        userArr
                    )

                    val name = userArr.map { it.name }
                    recyclerView.adapter = adapter

                    count = adapter.itemCount


                    // on click navigates to User prescription screen
                    adapter.setOnItemClickListener(object : UserNameAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {

                            val action =
                                PrescriptionHomeFragmentDirections.actionNavPrescriptionHomeToUserPrescriptionFragment(
                                    position
                                )
                            binding.userNameRV.user_card.findNavController().navigate(action)
                        }

                    })


                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


        binding.addUserButton.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = layoutInflater.inflate(R.layout.add_user_dialog, null)
            val editText = dialogLayout.findViewById<EditText>(R.id.edit_user)

            with(builder) {
                setTitle("Add User")
                setPositiveButton("Ok") { dialog, which ->
                    // add user to database

                    if (count < 6) {
                        val user = editText.text.toString()
                        addUser(user)
                        setUserRecyclerView()
                    } else {
                        Toast.makeText(context, "Only 6 users allowed", Toast.LENGTH_LONG).show()
                    }

                }
                setNegativeButton("Cancel") { dialog, which ->
                    Log.d("PrescriptionHomeFrag", "Negative button clicked")
                }

                setView(dialogLayout)
                show()
            }
        }

        Log.d("RV Size IN RVSET", "$count")

        return count
    }
}