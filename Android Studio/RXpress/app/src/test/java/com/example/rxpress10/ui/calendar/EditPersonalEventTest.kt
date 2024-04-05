package com.example.rxpress10.ui.calendar

import com.example.rxpress10.model.PersonalEvent
import org.junit.Assert
import org.junit.Test

class EditPersonalEventTest {

    // This checks if the personal event is null
    @Test
    fun Null() {
        val x: PersonalEvent? = null
        /* if x is null assign y to the method createUserInfo with hard coded strings else assign y to x*/
        val y: PersonalEvent? = x ?: EditPersonalEventFragment().createPersonalEvent(
            "Test",
            "3 am",
            "4 am",
            "Other",
            "Thu 20 April 2023"
        )
        /*if x is null then print y otherwise print x*/
        println(x ?: y)
    }

    //This checks if the user info was created properly and the values match
    @Test
    fun UserInfoCreated() {
        val med = EditPersonalEventFragment().createPersonalEvent(
            "Test",
            "3 am",
            "4 am",
            "Other",
            "Thu 20 April 2023"
        )
        Assert.assertEquals(
            med, EditPersonalEventFragment().createPersonalEvent(
                "Test",
                "3 am",
                "4 am",
                "Other",
                "Thu 20 April 2023"
            )
        )
        if (med == EditPersonalEventFragment().createPersonalEvent(
                "Test",
                "3 am",
                "4 am",
                "Other",
                "Thu 20 April 2023"
            )
        )
            println(
                "$med is equivalent to ${
                    EditPersonalEventFragment().createPersonalEvent(
                        "Test",
                        "3 am",
                        "4 am",
                        "Other",
                        "Thu 20 April 2023"
                    )
                }"
            )
    }
}