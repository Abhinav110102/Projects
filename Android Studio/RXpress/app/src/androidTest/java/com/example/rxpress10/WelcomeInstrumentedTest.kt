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
class WelcomeInstrumentedTest {

    //navigateToLogin: Test checks if user can navigate from Welcome Screen to Login Screen.
    @Test
    fun navigateToLogin(){
        ActivityScenario.launch(WelcomeActivity::class.java)
        onView(withId(R.id.loginButton))
            .perform(click())
    }
    //	navigateToRegister: Test checks if user can navigate from Welcome Screen to Register Screen.
    @Test
    fun navigateToRegister(){
        ActivityScenario.launch(WelcomeActivity::class.java)
        onView(withId(R.id.registerButton))
            .perform(click())
    }
    //signUpWithGoogle: Test checks if user is able sign up with google.
    @Test
    fun signUpWithGoogle(){
        ActivityScenario.launch(WelcomeActivity::class.java)
        onView(withId(R.id.gSignInBtn))
            .perform(click())
    }
}