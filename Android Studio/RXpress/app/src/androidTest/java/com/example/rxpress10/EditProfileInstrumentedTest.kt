package com.example.rxpress10

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.ui.settings.EditProfileFragment
import com.example.rxpress10.ui.settings.ProfileFragment
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
//Test for the edit profile fragment
@RunWith(AndroidJUnit4::class)
class EditProfileInstrumentedTest {
    //Initializes the testing activity
    @Before
    fun setUp() {
        ActivityScenario.launch(FragmentTestActivity::class.java)
        .onActivity { it.supportFragmentManager.beginTransaction().add(it.fragmentView.id, EditProfileFragment()).commit() }
    }
        //Test to ensure that editing profile information works properly
        @Test
        fun testEditScreen() {
            //val scenario = launchFragmentInContainer<EditProfileFragment>()
            Espresso.onView(ViewMatchers.withId(R.id.editFirstName))
                .check(ViewAssertions.matches(ViewMatchers.withHint("First Name")))
            Espresso.onView(ViewMatchers.withId(R.id.editFirstName))
                .perform(ViewActions.typeText("Abcd"), ViewActions.closeSoftKeyboard())
                .check(ViewAssertions.matches(ViewMatchers.withText("Abcd")))
            Espresso.onView(ViewMatchers.withId(R.id.editLastName))
                .check(ViewAssertions.matches(ViewMatchers.withHint("Last Name")))
            Espresso.onView(ViewMatchers.withId(R.id.editLastName))
                .perform(ViewActions.typeText("Efgh"), ViewActions.closeSoftKeyboard())
                .check(ViewAssertions.matches(ViewMatchers.withText("Efgh")))
            Espresso.onView(ViewMatchers.withId(R.id.editDoB))
                .check(ViewAssertions.matches(ViewMatchers.withHint("MM-DD-YYYY")))
            Espresso.onView(ViewMatchers.withId(R.id.radioGroup))
                .check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withId(R.id.male))))
            Espresso.onView(ViewMatchers.withId(R.id.radioGroup))
                .check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withId(R.id.female))))
            Espresso.onView(ViewMatchers.withId(R.id.radioGroup))
                .check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withId(R.id.nonBinary))))
            Espresso.onView(ViewMatchers.withId(R.id.radioGroup))
                .check(ViewAssertions.matches(ViewMatchers.withChild(ViewMatchers.withId(R.id.other))))
            Espresso.onView(ViewMatchers.withId(R.id.male))
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
            Espresso.onView(ViewMatchers.withId(R.id.female))
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
            Espresso.onView(ViewMatchers.withId(R.id.nonBinary))
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
            Espresso.onView(ViewMatchers.withId(R.id.other))
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
            Espresso.onView(ViewMatchers.withId(R.id.editTextTextPersonName3))
                .check(ViewAssertions.matches(ViewMatchers.withHint("Pronouns")))
            Espresso.onView(ViewMatchers.withId(R.id.editTextTextPersonName3))
                .perform(ViewActions.typeText("they/them"), ViewActions.closeSoftKeyboard())
                .check(ViewAssertions.matches(ViewMatchers.withText("they/them")))
            Espresso.onView(ViewMatchers.withId(R.id.saveBtn))
                .perform(ViewActions.click())
                .check(ViewAssertions.matches(ViewMatchers.isClickable()))
        }
}