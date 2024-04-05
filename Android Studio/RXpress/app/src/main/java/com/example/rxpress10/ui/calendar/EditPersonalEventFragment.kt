package com.example.rxpress10.ui.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivityEditPersonalEventFragmentBinding
import com.example.rxpress10.model.PersonalEvent
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_personal_event_fragment.*
import java.util.*


class EditPersonalEventFragment : Fragment(R.layout.activity_edit_personal_event_fragment), AdapterView.OnItemSelectedListener {
    private lateinit var binding: ActivityEditPersonalEventFragmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var pEventInfo: PersonalEvent
    private lateinit var pName: String
    private var startIndex: Int = 0
    private var endIndex: Int = 0
    private var eTime: Int = 0
    private var sTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPersonalEventFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        //Get the argument passed via navigation
        val args: EditPersonalEventFragmentArgs by navArgs()
        pName = args.eventName

        //Set the spinners
        setSpinners()
        //Set the hints for the text boxes and spinners
        setHints(pName)
        //Listen for the edit event button to update the database
        binding.editEventButton.setOnClickListener { database(pName) }

        //Get the start and end spinners
        val startSpinner = binding.editStartTime
        val stopSpinner = binding.editEndTime

        //Set on item selected listener for when a user selects a time
        startSpinner.setOnItemSelectedListener(this)
        stopSpinner.setOnItemSelectedListener(this)

        return binding.root

    }

    //This function sets the hints for the text boxes
    fun setHints(name: String) {
        //Get uId from firebase authentication
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        //If there are special characters replace them with a space
        val re = "[^A-Za-z0-9 ]".toRegex()
        val dName = re.replace(name, "")
        //Get a database instance for the personal event of the dName
        database = FirebaseDatabase.getInstance().getReference("users/$userId/PersonalEvent/$dName")

        database.get().addOnSuccessListener {
            //Get the name, start time, end time, and description
            val eventName = it.child("name").value.toString()
            val eventStart = it.child("startTime").value.toString()
            val eventEnd = it.child("endTime").value.toString()
            val eventDescrip = it.child("description").value.toString()

            val re = "[^A-Za-z0-9 ]".toRegex()
            val dName = re.replace(eventName, "")

            //Set index based off of the start time
            if (eventStart == "1 am") {
                startIndex = 1
            } else if (eventStart == "2 am") {
                startIndex = 2
            } else if (eventStart == "3 am") {
                startIndex = 3
            } else if (eventStart == "4 am") {
                startIndex = 4
            } else if (eventStart == "5 am") {
                startIndex = 5
            } else if (eventStart == "6 am") {
                startIndex = 6
            } else if (eventStart == "7 am") {
                startIndex = 7
            } else if (eventStart == "8 am") {
                startIndex = 8
            } else if (eventStart == "9 am") {
                startIndex = 9
            } else if (eventStart == "10 am") {
                startIndex = 10
            } else if (eventStart == "11 am") {
                startIndex = 11
            } else if (eventStart == "12 pm") {
                startIndex = 12
            } else if (eventStart == "1 pm") {
                startIndex = 13
            } else if (eventStart == "2 pm") {
                startIndex = 14
            } else if (eventStart == "3 pm") {
                startIndex = 15
            } else if (eventStart == "4 pm") {
                startIndex = 16
            } else if (eventStart == "5 pm") {
                startIndex = 17
            } else if (eventStart == "6 pm") {
                startIndex = 18
            } else if (eventStart == "7 pm") {
                startIndex = 19
            } else if (eventStart == "8 pm") {
                startIndex = 20
            } else if (eventStart == "9 pm") {
                startIndex = 21
            } else if (eventStart == "10 pm") {
                startIndex = 22
            } else if (eventStart == "11 pm") {
                startIndex = 23
            } else if (eventStart == "12 am") {
                startIndex = 24
            }

            //Set end index based off of the end time
            if (eventEnd == "1 am") {
                endIndex = 1
            } else if (eventEnd == "2 am") {
                endIndex = 2
            } else if (eventEnd == "3 am") {
                endIndex = 3
            } else if (eventEnd == "4 am") {
                endIndex = 4
            } else if (eventEnd == "5 am") {
                endIndex = 5
            } else if (eventEnd == "6 am") {
                endIndex = 6
            } else if (eventEnd == "7 am") {
                endIndex = 7
            } else if (eventEnd == "8 am") {
                endIndex = 8
            } else if (eventEnd == "9 am") {
                endIndex = 9
            } else if (eventEnd == "10 am") {
                endIndex = 10
            } else if (eventEnd == "11 am") {
                endIndex = 11
            } else if (eventEnd == "12 pm") {
                endIndex = 12
            } else if (eventEnd == "1 pm") {
                endIndex = 13
            } else if (eventEnd == "2 pm") {
                endIndex = 14
            } else if (eventEnd == "3 pm") {
                endIndex = 15
            } else if (eventEnd == "4 pm") {
                endIndex = 16
            } else if (eventEnd == "5 pm") {
                endIndex = 17
            } else if (eventEnd == "6 pm") {
                endIndex = 18
            } else if (eventEnd == "7 pm") {
                endIndex = 19
            } else if (eventEnd == "8 pm") {
                endIndex = 20
            } else if (eventEnd == "9 pm") {
                endIndex = 21
            } else if (eventEnd == "10 pm") {
                endIndex = 22
            } else if (eventEnd == "11 pm") {
                endIndex = 23
            } else if (eventEnd == "12 am") {
                endIndex = 24
            }

            binding.editEventName.hint = eventName
            binding.editStartTime.setSelection(startIndex)
            binding.editEndTime.setSelection(endIndex)
            binding.editEventDescription.hint = eventDescrip
        }.addOnFailureListener {
            //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

    //Create a personal event to be used later
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
        val startSpinner = binding.editStartTime
        val endSpinner = binding.editEndTime

        this.activity?.let {
            ArrayAdapter.createFromResource(
                it,
                com.example.rxpress10.R.array.time_array,
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

    //Sets the variables sTime and eTime based off of the name or number of the start and end time
    fun setTimeValues(eventStart: String, eventEnd: String, startTime: String, endTime: String) {
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
        } else  {
            sTime = startTime.toInt()
        }

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

    //Sets the values in the database
    private fun database(name: String) {
        //Gets the uId from firebase authentication
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        //Get database instance of users personal events
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/PersonalEvent")

        database.get().addOnSuccessListener {
            //Get the name, start time, end time, and description from the text boxes/spinners
            val eventName = binding.editEventNameEditText.text.toString()
            val eventStart = binding.editStartTime.selectedItem.toString()
            val eventEnd = binding.editEndTime.selectedItem.toString()
            val eventDescrip = binding.editEventDescriptionEditText.text.toString()

            //If the name of the event has not been left empty
            if (eventName.isNotEmpty()) {
                val re = "[^A-Za-z0-9 ]".toRegex()
                val dName = re.replace(eventName, "")
                if (dName != "") {
                    //If the name input is the same as the current event name
                    if (eventName == name) {
                        //If the start time is not "None"
                        if (eventStart != "None") {
                            //If the end time is not "Non"
                            if (eventEnd != "None") {
                                //Set the time values
                                setTimeValues(
                                    eventStart,
                                    eventEnd,
                                    eventStart.substringBefore(" "),
                                    eventEnd.substringBefore(" ")
                                )
                                //If the end time is greater than or equal to the start time
                                if (eTime >= sTime) {
                                    //If the description was not left empty
                                    if (eventDescrip.isNotEmpty()) {
                                        //create a personal event with all inputs
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                eventStart,
                                                eventEnd,
                                                eventDescrip,
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        //Change any special characters to a space
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            //Set the value dName into the database with the event information and clear the text boxes and spinners
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                eventStart,
                                                eventEnd,
                                                it.child("${name}")
                                                    .child("description").value.toString(),
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            } else {
                                setTimeValues(
                                    eventStart,
                                    it.child("${name}").child("endTime").value.toString(),
                                    eventStart.substringBefore(" "),
                                    it.child("${name}").child("endTime").value.toString()
                                        .substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information and put in previous end time
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                eventStart,
                                                it.child("${name}")
                                                    .child("endTime").value.toString(),
                                                eventDescrip,
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous end time and previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                eventStart,
                                                it.child("${name}")
                                                    .child("endTime").value.toString(),
                                                it.child("${name}")
                                                    .child("description").value.toString(),
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            }
                        } else {
                            if (eventEnd != "None") {
                                setTimeValues(
                                    it.child("${name}").child("startTime").value.toString(),
                                    eventEnd,
                                    it.child("${name}").child("startTime").value.toString()
                                        .substringBefore(" "),
                                    eventEnd.substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information and put in previous start time
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                it.child("${name}")
                                                    .child("startTime").value.toString(),
                                                eventEnd,
                                                eventDescrip,
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous start time and previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                it.child("${name}")
                                                    .child("startTime").value.toString(),
                                                eventEnd,
                                                it.child("${name}")
                                                    .child("description").value.toString(),
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            } else {
                                setTimeValues(
                                    it.child("${name}").child("startTime").value.toString(),
                                    it.child("${name}").child("endTime").value.toString(),
                                    it.child("${name}").child("startTime").value.toString()
                                        .substringBefore(" "),
                                    it.child("${name}").child("endTime").value.toString()
                                        .substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information and put in previous start time and previous end time
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                it.child("${name}")
                                                    .child("startTime").value.toString(),
                                                it.child("${name}")
                                                    .child("endTime").value.toString(),
                                                eventDescrip,
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous start time, previous end time and previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                name,
                                                it.child("${name}")
                                                    .child("startTime").value.toString(),
                                                it.child("${name}")
                                                    .child("endTime").value.toString(),
                                                it.child("${name}")
                                                    .child("description").value.toString(),
                                                it.child("${name}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            }
                        }
                        //Set action to go to the personal event detail fragment
                        val action =
                            EditPersonalEventFragmentDirections.actionEditPersonalEventFragmentToPersonalEventDetailFragment(
                                name
                            )
                        //Navigate to action
                        binding.editEventButton.findNavController().navigate(action)
                    } else {
                        if (eventStart != "None") {
                            if (eventEnd != "None") {
                                setTimeValues(
                                    eventStart,
                                    eventEnd,
                                    eventStart.substringBefore(" "),
                                    eventEnd.substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information put in
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                eventStart,
                                                eventEnd,
                                                eventDescrip,
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                eventStart,
                                                eventEnd,
                                                it.child("${pName}")
                                                    .child("description").value.toString(),
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            } else {
                                setTimeValues(
                                    eventStart,
                                    it.child("${pName}").child("endTime").value.toString(),
                                    eventStart.substringBefore(" "),
                                    it.child("${pName}").child("endTime").value.toString()
                                        .substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information and put in previous end time
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                eventStart,
                                                it.child("${pName}")
                                                    .child("endTime").value.toString(),
                                                eventDescrip,
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous end time and previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                eventStart,
                                                it.child("${pName}")
                                                    .child("endTime").value.toString(),
                                                it.child("${pName}")
                                                    .child("description").value.toString(),
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            }
                        } else {
                            if (eventEnd != "None") {
                                setTimeValues(
                                    it.child("${pName}").child("startTime").value.toString(),
                                    eventEnd,
                                    it.child("${pName}").child("startTime").value.toString()
                                        .substringBefore(" "),
                                    eventEnd.substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information and put in previous start time
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                it.child("${pName}")
                                                    .child("startTime").value.toString(),
                                                eventEnd,
                                                eventDescrip,
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous start time and previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                it.child("${pName}")
                                                    .child("startTime").value.toString(),
                                                eventEnd,
                                                it.child("${pName}")
                                                    .child("description").value.toString(),
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            } else {
                                setTimeValues(
                                    it.child("${pName}").child("startTime").value.toString(),
                                    it.child("${pName}").child("endTime").value.toString(),
                                    it.child("${pName}").child("startTime").value.toString()
                                        .substringBefore(" "),
                                    it.child("${pName}").child("endTime").value.toString()
                                        .substringBefore(" ")
                                )
                                if (eTime >= sTime) {
                                    if (eventDescrip.isNotEmpty()) {
                                        //Add information and put in previous start time and previous end time
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                it.child("${pName}")
                                                    .child("startTime").value.toString(),
                                                it.child("${pName}")
                                                    .child("endTime").value.toString(),
                                                eventDescrip,
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    } else {
                                        //Add information and put in previous start time, previous end time, and previous description
                                        pEventInfo =
                                            createPersonalEvent(
                                                eventName,
                                                it.child("${pName}")
                                                    .child("startTime").value.toString(),
                                                it.child("${pName}")
                                                    .child("endTime").value.toString(),
                                                it.child("${pName}")
                                                    .child("description").value.toString(),
                                                it.child("${pName}").child("date").value.toString()
                                            )
                                        val re = "[^A-Za-z0-9 ]".toRegex()
                                        val dName = re.replace(eventName, "")
                                        if (dName != "") {
                                            database.child(dName).setValue(pEventInfo)
                                                .addOnSuccessListener {
                                                    binding.editEventNameEditText.text!!.clear()
                                                    binding.editStartTime.setSelection(0)
                                                    binding.editEndTime.setSelection(0)
                                                    binding.editEventDescriptionEditText.text!!.clear()

                                                    //Toast.makeText(
                                                    //    context,
                                                    //    "Successfully Saved",
                                                    //    Toast.LENGTH_SHORT
                                                    //).show()
                                                }.addOnFailureListener {
                                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                                }
                                        } else {
                                            binding.editEventNameEditText.text!!.clear()
                                        }
                                    }
                                }
                            }
                        }
                        val re = "[^A-Za-z0-9 ]".toRegex()
                        val dName = re.replace(eventName, "")
                        database.child("${pName}").removeValue()
                        //Set action to go to personal event detail fragment
                        val action =
                            EditPersonalEventFragmentDirections.actionEditPersonalEventFragmentToPersonalEventDetailFragment(
                                eventName
                            )
                        //Navigate to action
                        binding.editEventButton.findNavController().navigate(action)
                    }
                } else {
                    binding.editEventNameEditText.text!!.clear()
                }
            } else {
                if (eventStart != "None") {
                    if (eventEnd != "None") {
                        setTimeValues(eventStart, eventEnd, eventStart.substringBefore(" "), eventEnd.substringBefore(" "))
                        if (eTime >= sTime) {
                            if (eventDescrip.isNotEmpty()) {
                                //Add information and put in previous name
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        eventStart,
                                        eventEnd,
                                        eventDescrip,
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                //Add information and put in previous name and previous description
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        eventStart,
                                        eventEnd,
                                        it.child("${pName}").child("description").value.toString(),
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child("$pName").setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        setTimeValues(
                            eventStart,
                            it.child("${pName}").child("endTime").value.toString(),
                            eventStart.substringBefore(" "),
                            it.child("${pName}").child("endTime").value.toString()
                                .substringBefore(" ")
                        )
                        if (eTime >= sTime) {
                            if (eventDescrip.isNotEmpty()) {
                                //Add information and put in previous name and previous end time
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        eventStart,
                                        it.child("${pName}").child("endTime").value.toString(),
                                        eventDescrip,
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                //Add information and put in previous name, previous end time, and previous description
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        eventStart,
                                        it.child("${pName}").child("endTime").value.toString(),
                                        it.child("${pName}").child("description").value.toString(),
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                } else {
                    if (eventEnd != "None") {
                        setTimeValues(it.child("${pName}").child("startTime").value.toString(), eventEnd, it.child("${pName}").child("startTime").value.toString().substringBefore(" "), eventEnd.substringBefore(" "))
                        if (eTime >= sTime) {
                            if (eventDescrip.isNotEmpty()) {
                                //Add information and put in previous name and previous start time
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        it.child("${pName}").child("startTime").value.toString(),
                                        eventEnd,
                                        eventDescrip,
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                //Add information and put in previous name, previous start time, and previous description
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        it.child("${pName}").child("startTime").value.toString(),
                                        eventEnd,
                                        it.child("${pName}").child("description").value.toString(),
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        setTimeValues(
                            it.child("${pName}").child("startTime").value.toString(),
                            it.child("${pName}").child("endTime").value.toString(),
                            it.child("${pName}").child("startTime").value.toString()
                                .substringBefore(" "),
                            it.child("${pName}").child("endTime").value.toString()
                                .substringBefore(" ")
                        )
                        if (eTime >= sTime) {
                            if (eventDescrip.isNotEmpty()) {
                                //Add information and put in previous name, previous start time, and previous end time
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        it.child("${pName}").child("startTime").value.toString(),
                                        it.child("${pName}").child("endTime").value.toString(),
                                        eventDescrip,
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                //Add information and put in previous name, previous start time, previous end time, and previous description
                                pEventInfo =
                                    createPersonalEvent(
                                        it.child("${pName}").child("name").value.toString(),
                                        it.child("${pName}").child("startTime").value.toString(),
                                        it.child("${pName}").child("endTime").value.toString(),
                                        it.child("${pName}").child("description").value.toString(),
                                        it.child("${pName}").child("date").value.toString()
                                    )
                                database.child(it.child("${pName}").child("name").value.toString())
                                    .setValue(pEventInfo).addOnSuccessListener {
                                    binding.editEventNameEditText.text!!.clear()
                                    binding.editStartTime.setSelection(0)
                                    binding.editEndTime.setSelection(0)
                                    binding.editEventDescriptionEditText.text!!.clear()

                                    //Toast.makeText(
                                    //    context,
                                    //    "Successfully Saved",
                                    //    Toast.LENGTH_SHORT
                                    //).show()
                                }.addOnFailureListener {
                                    //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
                //Set action to go to personal event detail fragment
                val action = EditPersonalEventFragmentDirections.actionEditPersonalEventFragmentToPersonalEventDetailFragment(pName)
                //Navigate to action
                binding.editEventButton.findNavController().navigate(action)
            }
        }.addOnFailureListener {
            //Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }
}