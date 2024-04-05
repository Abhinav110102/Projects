package com.example.rxpress10.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
import com.example.rxpress10.data.ReminderData
import com.example.rxpress10.model.Reminders
import com.example.rxpress10.ui.home.HomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class ReminderAdapter (
    private val context: HomeFragment,
    private val dataset: List<Reminders>
    ) : RecyclerView.Adapter<ReminderAdapter.ItemViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var nameArr: ArrayList<String>
    private lateinit var dosageArr: ArrayList<String>
    private lateinit var unitsArr: ArrayList<String>
    private lateinit var freqArr: ArrayList<String>
    private lateinit var takenArr: ArrayList<String>

        class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
            val viewer: TextView = view.findViewById(R.id.reminderView)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
            val adapterLayout = LayoutInflater.from(parent.context)
                .inflate(R.layout.reminder_view, parent, false)

            return ItemViewHolder(adapterLayout)
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            auth = FirebaseAuth.getInstance()
            var userId = auth.currentUser?.uid

            nameArr = ArrayList<String>()
            dosageArr = ArrayList<String>()
            unitsArr = ArrayList<String>()
            freqArr = ArrayList<String>()
            takenArr = ArrayList<String>()

            //Gets the primary users medications
            database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/ChildUsers/Primary/Medications")
            database.get().addOnSuccessListener {
                val meds = it.childrenCount.toInt()
                for (i in 0..meds-1){
                    val medName = it.children
                    medName.forEach{
                        //Gets all information needed to display from the database
                        val name = it.child("name").value.toString()
                        val dosage = it.child("dosage").value.toString()
                        val units = it.child("units").value.toString()
                        val freq = it.child("frequency").value.toString()
                        val taken = it.child("administered").value.toString()
                        nameArr.add(name)
                        dosageArr.add(dosage)
                        unitsArr.add(units)
                        freqArr.add(freq)

                        if (taken == "Orally") {
                            takenArr.add(taken)
                        } else {
                            takenArr.add("via "+taken)
                        }
                    }
                }
                val item = dataset[position]
                if (position > meds-1){
                    holder.viewer.visibility = View.GONE
                } else {
                    //Sets the text to the information from the database and depending on the frequency of the medication
                    //The times are set
                    if (freqArr?.get(position) == "1") {
                        holder.viewer.text = context.resources.getString(
                            item.stringResourceId,
                            dosageArr?.get(position),
                            unitsArr?.get(position),
                            nameArr?.get(position),
                            takenArr?.get(position),
                            "8 am"
                        )
                    } else if (freqArr?.get(position) == "2") {
                        holder.viewer.text = context.resources.getString(
                            item.stringResourceId,
                            dosageArr?.get(position),
                            unitsArr?.get(position),
                            nameArr?.get(position),
                            takenArr?.get(position),
                            "8 am and 8 pm"
                        )
                    } else if (freqArr?.get(position) == "3") {
                        holder.viewer.text = context.resources.getString(
                            item.stringResourceId,
                            dosageArr?.get(position),
                            unitsArr?.get(position),
                            nameArr?.get(position),
                            takenArr?.get(position),
                            "8 am, 2 pm, and 8 pm"
                        )
                    } else {
                        holder.viewer.text = context.resources.getString(
                            item.stringResourceId,
                            dosageArr?.get(position),
                            unitsArr?.get(position),
                            nameArr?.get(position),
                            takenArr?.get(position),
                            "8 am, 12 pm, 4 pm, and 8 pm"
                        )
                    }
                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        }

        override fun getItemCount() = dataset.size
    }
