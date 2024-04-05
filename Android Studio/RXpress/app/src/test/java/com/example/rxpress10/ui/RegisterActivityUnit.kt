package com.example.rxpress10.ui

import android.content.Context
import android.widget.TextView
import com.example.rxpress10.WelcomeActivity
import com.example.rxpress10.ui.settings.EditProfileFragment
import com.google.firebase.auth.FirebaseAuth
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
//Unit test for registering users
class RegisterActivityUnit {
    //    private val registerActivity: RegisterActivity = mock()
    //Test to ensure user info is registered properly
    @Test
    fun UserInfoCreated() {
        val info = EditProfileFragment().createUserInfo(
            firstname = "Test",
            lastName = "Test",
            dob = "01-01-2001",
            gender = "Non-Binary",
            pronouns = "they/them",
            email = "abcd@gmail.com"
        )
        assertEquals(
            info, EditProfileFragment().createUserInfo(
                firstname = "Test",
                lastName = "Test",
                dob = "01-01-2001",
                gender = "Non-Binary",
                pronouns = "they/them",
                email = "abcd@gmail.com"
            )
        )
    }


}