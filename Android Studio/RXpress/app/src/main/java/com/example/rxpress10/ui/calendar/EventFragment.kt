package com.example.rxpress10.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxpress10.R
import com.example.rxpress10.adapter.DetailAdapter
import com.example.rxpress10.databinding.ActivityEventFragmentBinding
import com.example.rxpress10.model.Details
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class EventFragment : Fragment(com.example.rxpress10.R.layout.activity_event_fragment) {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityEventFragmentBinding
    private lateinit var medName: String
    private lateinit var uName: String
    private lateinit var d: String
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private var num: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEventFragmentBinding.inflate(layoutInflater)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get arguments from the navigation
        val args: EventFragmentArgs by navArgs()

        d = args.date
        medName = args.prescripName.substringAfter(" ").substringBefore(" -")
        uName = args.prescripName.substringAfter("- ").substringBefore(" ")
        //Set the name of the prescription to the med name
        binding.prescripName.text = medName

        //If there are any special characters replace them with a space
        val databaseName = medName.replace(".", "")
        val re = "[^A-Za-z0-9 ]".toRegex()
        val dbName = re.replace(uName,"")

        //If the med name is not "More" dynamically set the ammount of details in the list
        if (medName != "More") {
            auth = FirebaseAuth.getInstance()
            var userId = auth.currentUser?.uid
            database = FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers/$dbName/Medications/$databaseName")

            val list = ArrayList<Details>()
            database.get().addOnSuccessListener {
                list.add(Details(R.string.pNameUnKnown))
                setMoreRecyclerView(medName, uName, d, list)
            }
        } else {
            val list = ArrayList<Details>()
            auth = FirebaseAuth.getInstance()
            var userId = auth.currentUser?.uid
            database = FirebaseDatabase.getInstance().getReference("users/$userId")

            database.get().addOnSuccessListener {
                val personal = it.child("PersonalEvent").children
                //Get the number of personal events and medication events and dynamically allocate a list
                personal.forEach { pe ->
                    if (pe.child("date").value.toString() == d) {
                        num += 1
                    }
                }
                val child = it.child("ChildUsers").children
                child.forEach { ch ->
                    val meds = ch.child("Medications").children
                    meds.forEach { me ->
                        num += 1
                    }
                }
                for (i in 1..num) {
                    list.add(Details(R.string.pNameUnKnown))
                }
                setMoreRecyclerView(medName, uName, d, list)
            }
        }

        return binding.root

    }

    //Set the recycler views using the dynamically allocated lists and the other fields
    fun setMoreRecyclerView(medName: String, uName: String, date: String, list: ArrayList<Details>){
        val recyclerView = binding.detailRecyclerView
        recyclerView.adapter = DetailAdapter(this, uName, medName, date, list)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
    }
}