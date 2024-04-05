package com.example.rxpress10

import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.ViewAssertion
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.ui.calendar.CalendarFragment
import com.example.rxpress10.ui.prescriptions.PrescriptionFragment
import com.example.rxpress10.ui.prescriptions.PrescriptionHomeFragment
import com.example.rxpress10.ui.prescriptions.PrescriptionHomeFragmentDirections
import com.example.rxpress10.ui.prescriptions.users.UserPrescriptionFragment
import com.example.rxpress10.ui.prescriptions.users.UserPrescriptionFragmentDirections
import com.example.rxpress10.ui.settings.EditProfileFragment
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
//Test for prescriptions fragment
@RunWith(AndroidJUnit4::class)
class PrescriptionInstrumentedTest {
    //This is a test to see if a User can be created properly
    @Test
    fun testCreatingUser() {

        ActivityScenario.launch(FragmentTestActivity::class.java)
            .onActivity { it.supportFragmentManager.beginTransaction().add(it.fragmentView.id, PrescriptionHomeFragment()).commit() }
        Espresso.onView((ViewMatchers.withId(R.id.add_user_button)))
            .perform(ViewActions.click())
        Espresso.onView((ViewMatchers.withId(R.id.edit_user)))
            .perform(ViewActions.typeText("Test User"), ViewActions.closeSoftKeyboard())
            .check(ViewAssertions.matches(ViewMatchers.withText("Test User")))
        Espresso.onView((ViewMatchers.withId(android.R.id.button1)))
            .perform(ViewActions.click())
    }
//      These are failed attempts at other tests for prescriptions due to problems with retrieving user arguments in prescription adding screen.
//    @Test
//    fun testAddingPrescription() {
//        //val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
//        //val prescriptionScenario = launchFragmentInContainer<PrescriptionFragment>()
//        //prescriptionScenario.onFragment { fragment ->
//        //   navController.setGraph(R.navigation.mobile_navigation3)
//        //    navController.navigate(R.id.nav_prescriptions)
//        //    Navigation.setViewNavController(fragment.requireView(), navController)
//        //}
//        Espresso.onView((ViewMatchers.withId(R.id.userNameRV)))
//            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,ViewActions.click()))
//        Espresso.onView(ViewMatchers.withId(R.id.fab))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.add_fab))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.prescription_name_edit_text))
//            .perform(ViewActions.typeText("Concerta"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("Concerta")))
//        Espresso.onView(ViewMatchers.withId(R.id.dosage_edit_text))
//            .perform(ViewActions.typeText("10"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("10")))
//        Espresso.onView(ViewMatchers.withId(R.id.units_edit_text))
//            .perform(ViewActions.typeText("mg"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("mg")))
//        Espresso.onView(ViewMatchers.withId(R.id.frequency_edit_text))
//            .perform(ViewActions.typeText("1"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("1")))
//        Espresso.onView(ViewMatchers.withId(R.id.taken_edit_text))
//            .perform(ViewActions.typeText("Orally"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("Orally")))
//        Espresso.onView(ViewMatchers.withId(R.id.other_requirements_edit_text))
//            .perform(ViewActions.typeText("With food"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("With food")))
//        Espresso.onView(ViewMatchers.withId(R.id.rx_num_edit_text))
//            .perform(ViewActions.typeText("12345678"), ViewActions.closeSoftKeyboard())
//            .check(ViewAssertions.matches(ViewMatchers.withText("12345678")))
//        Espresso.onView(ViewMatchers.withId(R.id.add_medication_button))
//            .perform(ViewActions.click())
//    }

//    @Test
//    fun testDeletingUser() {
//        Espresso.onView((ViewMatchers.withId(R.id.userNameRV)))
//            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(1,ViewActions.click()))
//        Espresso.onView(ViewMatchers.withId(R.id.fab))
//            .perform(ViewActions.click())
//        Espresso.onView(ViewMatchers.withId(R.id.delete_fab))
//            .perform(ViewActions.click())
//        Espresso.onView((ViewMatchers.withId(android.R.id.button1)))
//            .perform(ViewActions.click())
//    }
}