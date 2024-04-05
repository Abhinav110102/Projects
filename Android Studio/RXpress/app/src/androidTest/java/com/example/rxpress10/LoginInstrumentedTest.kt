package com.example.rxpress10

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class LoginInstrumentedTest {
//	testLoginScreen: Test checks if user is able enter email and password to login into the account.
    @Test
    fun testLogInScreen() {
        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.editTextEmailAddress))
            .check(matches(withHint("Email")))
        onView(withId(R.id.editTextEmailAddress))
            .perform(typeText("aka4@email.sc.edu"), closeSoftKeyboard())
            .check(matches(withText("aka4@email.sc.edu")))
        onView(withId(R.id.editTextPassword))
            .check(matches(withHint("Password")))
        onView(withId(R.id.editTextPassword))
                .perform(typeText("B5544ddrf#"), closeSoftKeyboard())
                .check(matches(withText("B5544ddrf#")))
            onView(withId(R.id.buttonLogin))
                .perform(click())
                .check(matches(isClickable()))
            //TODO test that firebase authenticates correctly
        }

    //    @Test
    //    fun testMenuClickable() {
    //        ActivityScenario.launch(MainActivity::class.java)
    //        onView(withId(R.id.MenuButton))
    //            .perform(click())
//        onView(withId(R.id.app_bar_menu))
//            .check(matches(isDisplayed()))
//        onView(withId(R.id.nav_host_fragment_content_menu))
//            .check(matches(isDisplayed()))
//    }
//checkNavigationToRegister: Test checks if user is able navigate to Register Screen from the login Screen .
    @Test
    fun checkNavigationToRegister() {
    ActivityScenario.launch(LoginActivity::class.java)
    onView(withId(R.id.textViewRegister))
        .perform(click())
    // .check(matches(isClickable()))
    }
    //loginWithGoogle: Test checks if user is able navigate to Welcome Screen from the login Screen .
    @Test
    fun loginWithGoogle(){
        ActivityScenario.launch(LoginActivity::class.java)
        onView(withId(R.id.textViewGoogle))
            .perform(click())
    }

}