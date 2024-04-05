package com.example.rxpress10.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.R
import com.example.rxpress10.model.Prescriptions
import com.example.rxpress10.ui.prescriptions.PrescriptionHomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.ArrayList
import com.example.rxpress10.ui.prescriptions.PrescriptionFragment
import java.util.*

class PrescriptionHomeAdapter (
    private val context: PrescriptionHomeFragment,
    private val dataset: List<Prescriptions>
    ) : RecyclerView.Adapter<PrescriptionHomeAdapter.ItemViewHolder>() {

    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var nameArr: ArrayList<String>
    private lateinit var dosageArr: ArrayList<String>
    private lateinit var unitsArr: ArrayList<String>

    class ItemViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val viewer: TextView = view.findViewById(R.id.prescriptionView)
        val button: ImageButton = view.findViewById(R.id.imageButton2)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.prescription_view, parent, false)

        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        nameArr = ArrayList<String>(20)
        dosageArr = ArrayList<String>(20)
        unitsArr = ArrayList<String>(20)

        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/Medications")
        database.get().addOnSuccessListener {
            val meds = it.childrenCount.toInt()
            for (i in 0..meds - 1) {
                val medName = it.children
                medName.forEach {
                    val name = it.child("name").value.toString()
                    val dosage = it.child("dosage").value.toString()
                    val units = it.child("units").value.toString()
                    nameArr.add(name)
                    dosageArr.add(dosage)
                    unitsArr.add(units)
                }
            }
            val item = dataset[position]
            if (position > meds-1){
                holder.viewer.visibility = View.GONE
                holder.button.visibility = View.GONE
            } else {
                holder.viewer.text = context.resources.getString(
                    item.stringResourceId,
                    nameArr?.get(position),
                    dosageArr?.get(position),
                    unitsArr?.get(position)
                )
            }
            holder.button.setOnClickListener {
                context.deleteMedication(nameArr.get(position))
            }
        }.addOnFailureListener {
            Log.e("firebase", "Error getting data", it)
        }

    }

    override fun getItemCount() = dataset.size
}
