package com.example.rxpress10.ui.prescriptions.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// this file creates the adapter that uploads the array into a recycler view as well as
// having interfaces that allow the user to click on the item

class UserPrescriptionsAdapter(
    val context: UserPrescriptionFragment,
    val drugList: ArrayList<UserPrescription>
) : RecyclerView.Adapter<UserPrescriptionsAdapter.UserCardViewHolder>() {


    private lateinit var mListener: onItemClickListener
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    fun deleteItem(i: Int, name: String, medName: String) {
        drugList.removeAt(i)

        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance()
            .getReference("users/" + userId + "/ChildUsers/" + name + "/Medications/")
        database.child("${medName}").removeValue()
        notifyDataSetChanged()
    }

    fun moveItem(i: Int, med: UserPrescription) {
        drugList.removeAt(i)
        drugList.add(i, med)
        notifyDataSetChanged()
    }


    class UserCardViewHolder(itemView: View, listener: onItemClickListener) :
        RecyclerView.ViewHolder(itemView) {
        val prescriptionName: TextView = itemView.findViewById(R.id.prescriptionObject)
        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.user_prescription_card, parent, false)
        return UserCardViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) {
        val currentItem = drugList[position]
        holder.prescriptionName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return drugList.size
    }
}