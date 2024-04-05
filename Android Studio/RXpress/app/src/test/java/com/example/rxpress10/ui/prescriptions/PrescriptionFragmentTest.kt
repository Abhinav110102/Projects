package com.example.rxpress10.ui.prescriptions

import com.example.android.sports.model.Medication
import com.example.rxpress10.ui.prescriptions.PrescriptionFragment
import org.bouncycastle.asn1.x500.style.RFC4519Style.name
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import java.io.IOException

internal class PrescriptionFragmentTest {

    // This checks if the medication is null
    @Test
    fun medicationNull() {
        val x: Medication? = null
        /* if x is null assign y to the method createMedication with hard coded strings else assign y to x*/
        val y: Medication? = x ?: PrescriptionFragment().createMedication(
            "Allegra",
            500.0,
            "mg",
            3,
            "take orally",
            "965478-75",
            "1"
        )
        /*if x is null then print y otherwise print x*/
        println(x ?: y)
    }

    //This checks if the medication was created properly and the values match
    @Test
    fun medicationCreated() {
        val med = PrescriptionFragment().createMedication(
            "Allegra",
            500.0,
            "mg",
            3,
            "take orally",
            "965478-75",
            "1"
        )
        assertEquals(
            med, PrescriptionFragment().createMedication(
                "Allegra",
                500.0,
                "mg",
                3,
                "take orally",
                "965478-75",
                "1"
            )
        )
        if (med == PrescriptionFragment().createMedication(
                "Allegra",
                500.0,
                "mg",
                3,
                "take orally",
                "965478-75",
                "1"
            )
        )
            println(
                "$med is equivalent to ${
                    PrescriptionFragment().createMedication(
                        "Allegra",
                        500.0,
                        "mg",
                        3,
                        "take orally",
                        "965478-75",
                        "1"
                    )
                }"
            )
    }
}