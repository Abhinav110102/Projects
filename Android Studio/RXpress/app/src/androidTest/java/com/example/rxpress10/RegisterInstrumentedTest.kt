package com.example.rxpress10

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class RegisterInstrumentedTest {
    //testRegisterScreenField: Test checks if all field and button are working on Register Screen.
    @Test
    fun testRegisterScreenField() {
        ActivityScenario.launch(RegisterActivity::class.java)
        //FirstName
        onView(withId(R.id.FirstName))
            .check(matches(withHint("First Name")))
        onView(withId(R.id.FirstName))
            .perform(typeText("abhinav"), closeSoftKeyboard())
            .check(matches(withText("abhinav")))

        //LastName
        onView(withId(R.id.LastName))
            .check(matches(withHint("Last Name")))
        onView(withId(R.id.LastName))
            .perform(typeText("Myadala"), closeSoftKeyboard())
            .check(matches(withText("Myadala")))

        // Email
        onView(withId(R.id.editTextEmailAddress))
            .check(matches(withHint("Email")))
        onView(withId(R.id.editTextEmailAddress))
            .perform(typeText("Myadala@email.sc.edu"), closeSoftKeyboard())
            .check(matches(withText("Myadala@email.sc.edu")))
        // Password
        onView(withId(R.id.editTextPassword))
            .check(matches(withHint("Password")))
        onView(withId(R.id.editTextPassword))
            .perform(typeText("B5544ddrf#"), closeSoftKeyboard())
            .check(matches(withText("B5544ddrf#")))
        // Confirm Password
        onView(withId(R.id.editTextConfirmPassword))
            .check(matches(withHint("Confirm Password")))
        onView(withId(R.id.editTextConfirmPassword))
            .perform(typeText("B5544ddrf#"), closeSoftKeyboard())
            .check(matches(withText("B5544ddrf#")))

        onView(withId(R.id.buttonRegister))
            .perform(click())
            .check(matches(isClickable()))
        //TODO test that firebase authenticates correctly
    }
    //checkNavigationToLogin: Test checks if user is able navigate to Login Screen from the register Screen .
    @Test
    fun checkNavigationToLogin(){
        ActivityScenario.launch(RegisterActivity::class.java)
        onView(withId(R.id.textViewLogin))
            .perform(click())
           // .check(matches(isClickable()))
    }
    //	RegisterWithGoogle: Test checks if user is able navigate to Welcome Screen from the Register Screen .
    @Test
    fun RegisterWithGoogle(){
        ActivityScenario.launch(RegisterActivity::class.java)
        onView(withId(R.id.textViewWelcome))
            .perform(click())
    }



}