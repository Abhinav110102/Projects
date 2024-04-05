package com.example.rxpress10.ui.prescriptions.model

import com.example.android.sports.model.Medication
import com.example.rxpress10.data.PrescriptionData

data class Prescriptions(
    val user: String,
    val prescriptions: ArrayList<Medication> // List of child items
)
