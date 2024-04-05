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
class ForgotPasswordInstrumentedTest {
    // loginCheckForgotPassword :Test checks if user is able navigate to Forgot Password Screen from the login Screen .
    @Test
    fun loginChecksForgotPassword(){
        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.forgotpassword))
            .perform(click())
    }
// forgotPasswordFieldWork: Test checks if user can use the text Field and buttons on forgot Password Screen
    @Test
    fun forgotPasswordFieldWork(){
        ActivityScenario.launch(ForgotPasswordActivity::class.java)
        onView(withId(R.id.et_forgot_email)).check(matches(withHint("Email")))
        onView(withId(R.id.et_forgot_email))
            .perform(typeText("abhinavmyadala@gmail.com"), closeSoftKeyboard())
            .check(matches(withText("abhinavmyadala@gmail.com")))
        onView(withId(R.id.btn_submit))
            .perform(click())
            .check(matches(isClickable()))
    }

}