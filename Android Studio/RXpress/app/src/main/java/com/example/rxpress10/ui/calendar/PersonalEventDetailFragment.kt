package com.example.rxpress10.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxpress10.R
import com.example.rxpress10.adapter.PersonalDetailAdapter
import com.example.rxpress10.data.PersonalDetailData
import com.example.rxpress10.databinding.ActivityPersonalEventDetailFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PersonalEventDetailFragment : Fragment(R.layout.activity_personal_event_detail_fragment) {

    private lateinit var binding: ActivityPersonalEventDetailFragmentBinding
    private lateinit var eventName: String
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalEventDetailFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get the arguments from the navigation
        val args: PersonalEventDetailFragmentArgs by navArgs()

        eventName = args.eventName
        binding.eventName.text = eventName
        //Replace each special character with a space
        val re = "[^A-Za-z0-9 ]".toRegex()
        val dName = re.replace(eventName, "")
        //Set the recycler view for the event
        setRecyclerView(dName)

        //If edit is clicked go to edit
        binding.editPersonalButton.setOnClickListener { editClicked(dName) }
        //If delete is clicked go to deleteClicked
        binding.deletePersonalButton.setOnClickListener { deleteClicked() }

        return binding.root

    }

    //Set the recycler view with the information from that event
    fun setRecyclerView(medName: String){
        val myDataset = PersonalDetailData().loadDetails(eventName)
        val recyclerView = binding.eventDetailRecyclerView
        recyclerView.adapter = PersonalDetailAdapter(this, medName, myDataset)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)
    }

    //Navigate to edit personal event fragment
    fun editClicked(eName: String) {
        val action = PersonalEventDetailFragmentDirections.actionPersonalEventDetailFragmentToEditPersonalEventFragment(eName)
        binding.editPersonalButton.findNavController().navigate(action)
    }

    //Deletes the event
    fun deleteClicked() {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/PersonalEvent")
        //Replace all special characters with a space
        val re = "[^A-Za-z0-9 ]".toRegex()
        val dName = re.replace(eventName, "")
        //Remove the database instance of the event
        database.child("${dName}").removeValue()

        //Navigate to the calendar fragment
        val action =
            PersonalEventDetailFragmentDirections.actionPersonalEventDetailFragmentToNavCalendarFragment()
        binding.deletePersonalButton.findNavController().navigate(action)
    }
}