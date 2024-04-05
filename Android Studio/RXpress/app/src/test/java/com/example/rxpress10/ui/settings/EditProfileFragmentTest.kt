package com.example.rxpress10.ui.settings

import com.example.android.sports.model.Medication
import com.example.rxpress10.ui.prescriptions.PrescriptionFragment
import com.example.rxpress10.ui.settings.EditProfileFragment
import com.example.rxpress10.model.UserInfo
import org.junit.Assert.*
import org.junit.Test

internal class EditProfileFragmentTest {

    // This checks if the UserInfo is null
    @Test
    fun Null() {
        val x: UserInfo? = null
        /* if x is null assign y to the method createUserInfo with hard coded strings else assign y to x*/
        val y: UserInfo? = x ?: EditProfileFragment().createUserInfo(
            "Abcd",
            "Efgh",
            "abcd@gmail.com",
            "01-01-2001",
            "Male",
            "he/him"
        )
        /*if x is null then print y otherwise print x*/
        println(x ?: y)
    }

    //This checks if the user info was created properly and the values match
    @Test
    fun UserInfoCreated() {
        val med = EditProfileFragment().createUserInfo(
            "Abcd",
            "Efgh",
            "abcd@gmail.com",
            "01-01-2001",
            "Male",
            "he/him"
        )
        assertEquals(
            med, EditProfileFragment().createUserInfo(
                "Abcd",
                "Efgh",
                "abcd@gmail.com",
                "01-01-2001",
                "Male",
                "he/him"
            )
        )
        if (med == EditProfileFragment().createUserInfo(
                "Abcd",
                "Efgh",
                "abcd@gmail.com",
                "01-01-2001",
                "Male",
                "he/him"
            )
        )
            println(
                "$med is equivalent to ${
                    EditProfileFragment().createUserInfo(
                        "Abcd",
                        "Efgh",
                        "abcd@gmail.com",
                        "01-01-2001",
                        "Male",
                        "he/him"
                    )
                }"
            )
    }
}