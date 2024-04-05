package com.example.rxpress10.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.core.view.isNotEmpty
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivityPersonalEventFragmentBinding
import com.example.rxpress10.model.PersonalEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_personal_event_fragment.*

class PersonalEventFragment : Fragment(R.layout.activity_personal_event_fragment), AdapterView.OnItemSelectedListener {

    private lateinit var binding: ActivityPersonalEventFragmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var personalEvent: PersonalEvent
    private lateinit var dateOfEvent: String
    private var eTime: Int = 0
    private var sTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonalEventFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get the arguments from the navigation
        val args: PersonalEventFragmentArgs by navArgs()
        dateOfEvent = args.date
        setSpinners()
        //Listen for click on add event button and call database function
        binding.addEventButton.setOnClickListener { database(dateOfEvent) }

        //Get the start and end spinners
        val startSpinner = binding.startTime
        val stopSpinner = binding.endTime

        //Listen for item selected for both start and end spinners
        startSpinner.setOnItemSelectedListener(this)
        stopSpinner.setOnItemSelectedListener(this)

        return binding.root

    }

    //Creates a personal event
    fun createPersonalEvent(
        name: String, startTime: String, endTime: String,
        description: String, date: String
    ): PersonalEvent {
        // reads the data input on each text input and assigns it to a variable

        // creates an object named medication with the user input
        return PersonalEvent(
            name,
            startTime,
            endTime,
            description,
            date
        )
    }

    //Set the spinners to be able to work properly
    fun setSpinners() {
        val startSpinner = binding.startTime
        val endSpinner = binding.endTime

        this.activity?.let {
            ArrayAdapter.createFromResource(
                it,
                R.array.time_array,
                android.R.layout.simple_spinner_item
            ).also { adapter ->
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                startSpinner.adapter = adapter
                endSpinner.adapter = adapter
            }
        }
    }

    //Needed for spinners to work properly
    override fun onItemSelected(parent: AdapterView<*>, view: View?, pos: Int, id: Long) {
        val startText = parent.getItemAtPosition(pos).toString()
        val endText = parent.getItemAtPosition(pos).toString()
    }

    //Needed for spinners to work properly
    override fun onNothingSelected(parent: AdapterView<*>){}

    //Sets the values in the database
    private fun database(date: String) {
        //Get the uId from firebase authentication
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        //Get database instance of personal events
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/PersonalEvent")

        //Get the text in the text boxes and the spinners
        val eventName = binding.eventNameEditText.text.toString()
        val eventStart = binding.startTime.selectedItem.toString()
        val startTime = binding.startTime.selectedItem.toString().substringBefore(" ")
        val eventEnd = binding.endTime.selectedItem.toString()
        val endTime = binding.endTime.selectedItem.toString().substringBefore(" ")
        val eventDescrip = binding.eventDescriptionEditText.text.toString()

        //Replace all special characters with spaces
        val re = "[^A-Za-z0-9 ]".toRegex()
        val dName = re.replace(eventName, "")


        val date = date
        //Set sTime and eTime based off of the name or number from the spinners
        if (eventStart != "None") {
            if (eventStart == getString(R.string.p1)) {
                sTime = 13
            } else if (eventStart == getString(R.string.p2)) {
                sTime = 14
            } else if (eventStart == getString(R.string.p3)) {
                sTime = 15
            } else if (eventStart == getString(R.string.p4)) {
                sTime = 16
            } else if (eventStart == getString(R.string.p5)) {
                sTime = 17
            } else if (eventStart == getString(R.string.p6)) {
                sTime = 18
            } else if (eventStart == getString(R.string.p7)) {
                sTime = 19
            } else if (eventStart == getString(R.string.p8)) {
                sTime = 20
            } else if (eventStart == getString(R.string.p9)) {
                sTime = 21
            } else if (eventStart == getString(R.string.p10)) {
                sTime = 22
            } else if (eventStart == getString(R.string.p11)) {
                sTime = 23
            } else if (eventStart == getString(R.string.a12)) {
                sTime = 24
            } else {
                sTime = startTime.toInt()
            }
        }

        if (eventEnd != "None") {
            if (eventEnd == "1 pm") {
                eTime = 13
            } else if (eventEnd == "2 pm") {
                eTime = 14
            } else if (eventEnd == "3 pm") {
                eTime = 15
            } else if (eventEnd == "4 pm") {
                eTime = 16
            } else if (eventEnd == "5 pm") {
                eTime = 17
            } else if (eventEnd == "6 pm") {
                eTime = 18
            } else if (eventEnd == "7 pm") {
                eTime = 19
            } else if (eventEnd == "8 pm") {
                eTime = 20
            } else if (eventEnd == "9 pm") {
                eTime = 21
            } else if (eventEnd == "10 pm") {
                eTime = 22
            } else if (eventEnd == "11 pm") {
                eTime = 23
            } else if (eventEnd == "12 am") {
                eTime = 24
            } else {
                eTime = endTime.toInt()
            }
        }

        if (dName != "") {
            //Make sure name, start time, and end time are not empty
            //Otherwise give an error message
            if (eventName.isNotEmpty() && eventStart != "None" && eventEnd != "None") {
                //Make sure that the end time is greater than or equal to the start time
                //Otherwise show an error message
                if (eTime >= sTime) {
                    //create the personal event with the values input
                    personalEvent =
                        createPersonalEvent(eventName, eventStart, eventEnd, eventDescrip, date)
                    database.child(dName).setValue(personalEvent).addOnSuccessListener {
                        binding.eventNameEditText.text!!.clear()
                        binding.startTime.setSelection(0)
                        binding.endTime.setSelection(0)
                        binding.eventDescriptionEditText.text!!.clear()

                        Toast.makeText(
                            context,
                            "Successfully Saved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(
                        context,
                        "The end time cannot be earlier than the start time",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "The name, start time, and end time cannot be left blank",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            Toast.makeText(this.context, "Name cannot have all special characters", Toast.LENGTH_SHORT).show()
        }
    }
}
