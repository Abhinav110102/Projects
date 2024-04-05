package com.example.rxpress10.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
import com.example.rxpress10.model.Details
import com.example.rxpress10.model.Reminders
import com.example.rxpress10.ui.calendar.CalendarFragment
import com.example.rxpress10.ui.calendar.EventFragment
import com.example.rxpress10.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class DetailAdapter (
    private val context: EventFragment,
    private val userName: String,
    private val medName: String,
    private val date: String,
    private val dataset: ArrayList<Details>
    ) : RecyclerView.Adapter<DetailAdapter.ItemViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var nameArr: ArrayList<String>
    private lateinit var freqOrStartArr: ArrayList<String>
    private lateinit var dosageOrEndArr: ArrayList<String>
    private lateinit var rxNumArr: ArrayList<String>
    private lateinit var directionArr: ArrayList<String>
    private lateinit var takenArr: ArrayList<String>
    private lateinit var freq: String
    private lateinit var dosage: String
    private lateinit var units: String
    private lateinit var rxnum: String
    private lateinit var directions: String
    private lateinit var taken: String

        class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            val viewer: TextView = view.findViewById(R.id.detailText)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.details_view, parent, false)

            return ItemViewHolder(adapterLayout)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            auth = FirebaseAuth.getInstance()
            var userId = auth.currentUser?.uid

            nameArr = ArrayList<String>()
            freqOrStartArr = ArrayList<String>()
            dosageOrEndArr = ArrayList<String>()
            rxNumArr = ArrayList<String>()
            directionArr = ArrayList<String>()
            takenArr = ArrayList<String>()

            if (medName == "More"){
                database = FirebaseDatabase.getInstance().getReference("users/"+userId)
                //If the name is "More" then add the personal events and the medication events
                //and set the data in the recycler view
                database.get().addOnSuccessListener {
                    val personal = it.child("PersonalEvent").children
                    val users = it.child("ChildUsers").children

                    personal.forEach { pe ->
                        if (date == pe.child("date").value.toString()) {
                            nameArr.add("Name: "+pe.child("name").value.toString())
                            freqOrStartArr.add("Start Time: "+pe.child("startTime").value.toString())
                            dosageOrEndArr.add("End Time: "+pe.child("endTime").value.toString())
                            rxNumArr.add("Description: "+pe.child("description").value.toString())
                            directionArr.add("")
                        } else {
                            return@forEach
                        }
                    }

                    users.forEach { na ->
                        val names = na.child("Medications").children
                        names.forEach { me ->
                            nameArr.add("Name: "+me.child("name").value.toString()+" - "+na.child("name").value.toString() + " RX")
                            freqOrStartArr.add("Frequency: Taken "+me.child("frequency").value.toString()+" times a day "+"Administration: "+me.child("administered").value.toString())
                            dosageOrEndArr.add("Dosage: "+me.child("dosage").value.toString()+me.child("units").value.toString())
                            rxNumArr.add("Rx Number: "+me.child("rxNum").value.toString())
                            if (me.child("other").value.toString() == "") {
                                directionArr.add("Directions: " + me.child("other").value.toString() + "\n")
                            } else {
                                directionArr.add("Directions: Take with " + me.child("other").value.toString() + "\n")
                            }
                        }
                    }

                    val item = dataset[position]
                    holder.viewer.text = context.resources.getString(item.stringResourceId, nameArr[position], freqOrStartArr[position], dosageOrEndArr[position], rxNumArr[position], directionArr[position])

                }.addOnFailureListener{
                    Log.e("firebase", "Error getting data", it)
                }
            } else {
                database =
                    FirebaseDatabase.getInstance().getReference("users/$userId/ChildUsers")
                //Set the information for the medication that was selected
                database.get().addOnSuccessListener {
                    val name = it.children
                    name.forEach { na ->
                        val n = na.child("name").value.toString()
                        val meds = na.child("Medications").children
                        meds.forEach re@{ me ->
                            val medN = me.child("name").value.toString()
                            if (medName == medN && userName == n) {
                                freq = me.child("frequency").value.toString()
                                dosage = me.child("dosage").value.toString()
                                units = me.child("units").value.toString()
                                rxnum = me.child("rxNum").value.toString()
                                directions = me.child("other").value.toString()
                                taken = me.child("administered").value.toString()
                                val item = dataset[position]
                                holder.viewer.text = context.resources.getString(item.stringResourceId,
                                    "Name: $medN",
                                    "Rx Number: $rxnum",
                                    "Dosage: $dosage$units",
                                    "Frequency: Taken $freq times a day\n\t Administration: $taken",
                                    "Directions: Take with $directions"
                                )
                            } else {
                                return@re
                            }
                        }
                    }
                }.addOnFailureListener {
                    Log.e("firebase", "Error getting data", it)
                }
            }
        }

        override fun getItemCount() = dataset.size
    }