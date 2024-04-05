package com.example.rxpress10.data

import android.util.Log
import com.example.rxpress10.R
import com.example.rxpress10.model.Prescriptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.util.*

class PrescriptionData {
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    fun loadPrescriptions(): List<Prescriptions> {
        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/Medications")

        return listOf(
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription),
            Prescriptions(R.string.prescription)
)
    }
}
