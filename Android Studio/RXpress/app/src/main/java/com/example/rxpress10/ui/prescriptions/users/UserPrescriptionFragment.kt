package com.example.rxpress10.ui.prescriptions.users

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxpress10.R
import com.example.rxpress10.databinding.FragmentUsersPrescriptionsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_prescription_fragment.*
import kotlinx.android.synthetic.main.fragment_users_prescriptions.*
import kotlinx.android.synthetic.main.prescription_card.view.*
import kotlinx.android.synthetic.main.user_card.*
import kotlinx.android.synthetic.main.user_card.view.*
import kotlinx.android.synthetic.main.user_prescription_card.view.*

// User prescription screen holds the users name as the title and has a list of prescriptions that
// user is taking. It also allows the user to add more prescriptions or delete the user unless it is
// the primary user
class UserPrescriptionFragment : Fragment(R.layout.fragment_users_prescriptions) {

    // initializing binding database and authentication

    private lateinit var binding: FragmentUsersPrescriptionsBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // initializing the userNamePosition name arr and drug list
    private var userNamePosition: Int = 0
    private lateinit var nameArr: ArrayList<String>
    private var drugList = ArrayList<UserPrescription>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentUsersPrescriptionsBinding.inflate(layoutInflater)
    }

    // on resume is based on the lifecycle of android so on back it will help with the ui of the drawables
    override fun onResume() {
        super.onResume()
        binding.fab.setImageResource(R.drawable.ic_baseline_add_24)
        binding.addFab.visibility = View.GONE
        binding.deleteFab.visibility = View.GONE

        binding.fab.setOnClickListener {
            if (delete_fab.visibility == View.GONE &&
                add_fab.visibility == View.GONE
            ) {
                binding.deleteFab.show()
                binding.addFab.show()
                binding.deleteFab.visibility = View.VISIBLE
                binding.addFab.visibility = View.VISIBLE
                binding.fab.setImageResource(R.drawable.ic_baseline_expand_less_24)
            } else {
                binding.deleteFab.hide()
                binding.addFab.hide()
                binding.deleteFab.visibility = View.GONE
                binding.addFab.visibility = View.GONE
                binding.fab.setImageResource(R.drawable.ic_baseline_add_24)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding.otherUserTitle.visibility = View.GONE


        // Inflate the layout for this fragment
        val root: View = binding.root

        // nav arguments
        val args: UserPrescriptionFragmentArgs by navArgs()
        userNamePosition = args.position

        nameArr = java.util.ArrayList<String>(20)

        // firebase database reference
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers")
        database.get().addOnSuccessListener {
            val users = it.childrenCount.toInt()
            val userName = it.children
            userName.forEach {
                val name = it.child("name").value.toString()
                nameArr.add(name)
            }

            // this userName from the position from the clicked user from prescription home
            val thisUser = nameArr[userNamePosition]

            Log.d("ThisUser", "$thisUser")

            // if it equals Your then rename it to UserInfo Database reference
            if (thisUser.equals("Your", true)) {
                database = FirebaseDatabase.getInstance().getReference("users/$userId/UserInfo")
                database.get().addOnSuccessListener {
                    val primaryName = it.child("firstName").value.toString()
                    binding.otherUserTitle.visibility = View.VISIBLE
                    binding.otherUserTitle.text =
                        getString(R.string.userNamePrescriptions, primaryName)
                }
            }
            // otherwise its the other users name
            else {
                binding.otherUserTitle.visibility = View.VISIBLE
                binding.otherUserTitle.text = getString(R.string.userNamePrescriptions, thisUser)
            }

            // populates the medication list of the user
            populateMedicationList(thisUser)


            // animations for the floating action button changes the drawables and visibility depending on clicking
            binding.addFab.visibility = View.GONE
            binding.deleteFab.visibility = View.GONE

            binding.fab.setOnClickListener {
                if (delete_fab.visibility == View.GONE &&
                    add_fab.visibility == View.GONE
                ) {
                    binding.deleteFab.show()
                    binding.addFab.show()
                    binding.deleteFab.visibility = View.VISIBLE
                    binding.addFab.visibility = View.VISIBLE
                    binding.fab.setImageResource(R.drawable.ic_baseline_expand_less_24)
                } else {
                    binding.deleteFab.hide()
                    binding.addFab.hide()
                    binding.deleteFab.visibility = View.GONE
                    binding.addFab.visibility = View.GONE
                    binding.fab.setImageResource(R.drawable.ic_baseline_add_24)
                }
            }

            // navigates to add presription screen with this user name
            binding.addFab.setOnClickListener {
                val action =
                    UserPrescriptionFragmentDirections.actionUserPrescriptionFragmentToNavPrescriptions(
                        thisUser
                    )
                binding.addFab.findNavController().navigate(action)
            }

            // deletes the user and opens a dialog box
            binding.deleteFab.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                val dialogLayout = layoutInflater.inflate(R.layout.confirm_delete_dialog, null)

                with(builder) {
                    setTitle("Are you sure you want to delete $thisUser?")
                    setPositiveButton("Confirm") { dialog, which ->
                        // delete user to database
                        database =
                            FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers/")
                        database.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {

                                // if it does not equal Your delete otherwise it will not delete the primary user
                                if (!thisUser.equals("Your", true)) {
                                    val re = "[^A-Za-z0-9 ]".toRegex()
                                    val dbName = re.replace(thisUser, "")
                                    if (snapshot.hasChild(dbName)) {

                                        val action =
                                            UserPrescriptionFragmentDirections.actionUserPrescriptionFragmentToNavPrescriptionHome()
                                        binding.deleteFab.findNavController().navigate(action)
                                        database.child(dbName).removeValue()

                                    }
                                    Toast.makeText(
                                        context,
                                        "$thisUser Has Been Deleted",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Cannot Delete Primary User",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        })


                    }
                    setNegativeButton("Cancel") { dialog, which ->
                        Log.d("PrescriptionHomeFrag", "Negative button clicked")
                    }

                    setView(dialogLayout)
                    show()
                }
            }
        }

        // clears drug list because of duplicates
        drugList.clear()
        return root
    }


    // get Med data from the database and add to the list
    fun populateMedicationList(
        userName: String
    ) {

        val re = "[^A-Za-z0-9 ]".toRegex()
        val dbName = re.replace(userName, "")

        Log.d("populate", "entered populateMedicationList")

        nameArr = ArrayList<String>(20)
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        if (userName == "Your") {

            Log.d("PopulateMedList", "Entered Your If Statement")
            database = FirebaseDatabase.getInstance()
                .getReference("users/$userId/ChildUsers/Primary/Medications")
            database.get().addOnSuccessListener {

                Log.d("Snapshot", "Entered Snapshot")
                val meds = it.childrenCount.toInt()

                println(meds)
                val medName = it.children
                medName.forEach {
                    val name = it.child("name").value.toString()
                    nameArr.add(name)
                    Log.d("drugList", "$userName")

                }

                println(nameArr)

                setUserRecyclerView("Primary", nameArr)

            }
        } else {
            Log.d("PopulateMedList", "Entered Else Statement")
            database = FirebaseDatabase.getInstance()
                .getReference("users/$userId/ChildUsers/$dbName/Medications")

            println(database)


            database.get().addOnSuccessListener {

                Log.d("Snapshot", "Entered Snapshot")
                val meds = it.childrenCount.toInt()

                println(meds)
                val medName = it.children
                medName.forEach {
                    val name = it.child("name").value.toString()
                    nameArr.add(name)
                    Log.d("drugList", "$userName")

                }

                println(nameArr)

                setUserRecyclerView(userName, nameArr)

            }
        }

    }

    // This method takes in the userName and a list and will add the prescriptions to said user
    // it will also allow the user to click the prescription and navigate them to the details of
    // the prescription clicked
    fun setUserRecyclerView(userName: String, list: ArrayList<String>) {

        Log.d("setRecyclerView", "$list")

        val re = "[^A-Za-z0-9 ]".toRegex()
        val dbName = re.replace(userName, "")

        val size = list.size

        for (i in 0 until size) {
            val name = list[i]
            val userPrescription = UserPrescription(name)
            drugList.add(userPrescription)
        }

        println(drugList)
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance()
            .getReference("users/$userId/ChildUsers/$dbName/Medications")

        database.addListenerForSingleValueEvent(object : ValueEventListener {


            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {

                    val recyclerView = binding.drugRV
                    recyclerView.layoutManager = LinearLayoutManager(context)
                    recyclerView.setHasFixedSize(true)

                    var adapter = UserPrescriptionsAdapter(UserPrescriptionFragment(), drugList)

                    recyclerView.adapter = adapter
                    adapter.setOnItemClickListener(object :
                        UserPrescriptionsAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val action =
                                UserPrescriptionFragmentDirections.actionUserPrescriptionFragmentToViewPrescription(
                                    userName,
                                    nameArr[position]
                                )
                            binding.drugRV.user_prescription_card.findNavController()
                                .navigate(action)
                        }

                    })


                }

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })


    }

    fun deleteMedication(medName: String) {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/Medications")
        database.child("${medName}").removeValue()
    }
}