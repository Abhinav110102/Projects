package com.example.rxpress10.data

import com.example.rxpress10.R
import com.example.rxpress10.model.Details
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PersonalDetailData {
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var names: MutableList<Details>

    fun loadDetails(medName: String): List<Details> {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        names = mutableListOf<Details>()

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/PersonalEvent")

        if (medName == "More") {
            return listOf<Details>(
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown),
                Details(R.string.personalNameUnKnown)
            )
        } else {
            return listOf<Details>(
                Details(R.string.personalNameKnown)
            )
        }
    }


}