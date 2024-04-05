package com.example.rxpress10.ui.prescriptions.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
import com.example.rxpress10.ui.prescriptions.PrescriptionHomeFragment
import com.example.rxpress10.ui.prescriptions.model.Users
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

// This adapter is the list of users and uploads the list of user names to a list and display them
// in a recycler view
class UserNameAdapter(
    val context: PrescriptionHomeFragment,
    val userList: ArrayList<Users>): RecyclerView.Adapter<UserNameAdapter.UserCardViewHolder>() {

    private lateinit var mListener: onItemClickListener
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    fun deleteItem(i : Int, name: String)
    {
        userList.removeAt(i)

        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/" + userId +"/ChildUsers/")
        database.child("${name}").removeValue()
        notifyDataSetChanged()
    }

    fun moveItem(i: Int, users: Users){
        userList.removeAt(i)
        userList.add(i, users)
        notifyDataSetChanged()
    }

    interface onItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }


    class UserCardViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val userName : TextView = itemView.findViewById(R.id.userName)
        val addMedBtn: ImageView = itemView.findViewById(R.id.add_medication_btn)

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserCardViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_card, parent, false)
        return UserCardViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: UserCardViewHolder, position: Int) {
        val currentItem = userList[position]
        holder.userName.text = currentItem.name
    }

    override fun getItemCount(): Int {
        return userList.size
    }
}