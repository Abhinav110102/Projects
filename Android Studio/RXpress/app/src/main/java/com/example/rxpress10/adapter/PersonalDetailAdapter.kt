package com.example.rxpress10.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
import com.example.rxpress10.model.Details
import com.example.rxpress10.ui.calendar.PersonalEventDetailFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList

class PersonalDetailAdapter(
    private val context: PersonalEventDetailFragment,
    private val eventName: String,
    private val dataset: List<Details>
) : RecyclerView.Adapter<PersonalDetailAdapter.ItemViewHolder>() {
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var nameArr: ArrayList<String>
    private lateinit var startArr: ArrayList<String>
    private lateinit var endArr: ArrayList<String>
    private lateinit var descriptArr: ArrayList<String>

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

        nameArr = ArrayList<String>(20)
        startArr = ArrayList<String>(20)
        endArr = ArrayList<String>(20)
        descriptArr = ArrayList<String>(20)

        if (eventName == "More"){
            //If the name is "More" then set the recycler view with the personal event details
            database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/PersonalEvent")

            database.get().addOnSuccessListener {
                val events = it.childrenCount.toInt()
                for (i in 1..events-1){
                    val eventName = it.children
                    eventName.forEach{
                        val name = it.child("name").value.toString()
                        val start = it.child("startTime").value.toString()
                        val end = it.child("endTime").value.toString()
                        val dets = it.child("description").value.toString()
                        nameArr.add(name)
                        startArr.add(start)
                        endArr.add(end)
                        descriptArr.add(dets)
                    }
                }
                val item = dataset[position]
                if (position > events-3){
                    holder.viewer.visibility = View.GONE
                } else {
                    holder.viewer.text = context.resources.getString(
                        item.stringResourceId,
                        nameArr[position + 2],
                        startArr[position + 2],
                        endArr[position + 2],
                        descriptArr[position + 2]
                    )
                }
            }.addOnFailureListener{
                Log.e("firebase", "Error getting data", it)
            }
        } else {
            //Set the details for the specified event
            database =
                FirebaseDatabase.getInstance().getReference("users/" + userId + "/PersonalEvent")
            val re = "[^A-Za-z0-9 ]".toRegex()
            val dName = re.replace(eventName, "")

            database.get().addOnSuccessListener {
                val start = it.child(dName).child("startTime").value.toString()
                val end = it.child(dName).child("endTime").value.toString()
                val dets = it.child(dName).child("description").value.toString()

                val item = dataset[position]
                holder.viewer.text = context.resources.getString(item.stringResourceId, start, end, dets)

            }.addOnFailureListener {
                Log.e("firebase", "Error getting data", it)
            }
        }
    }

    override fun getItemCount() = dataset.size
}