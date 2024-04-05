package com.example.rxpress10

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import org.hamcrest.CoreMatchers.*
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.Visibility
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.ui.calendar.CalendarFragment
import com.example.rxpress10.ui.settings.EditProfileFragment
import com.example.rxpress10.ui.settings.ProfileFragment
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
//Test for the calendar fragment
@RunWith(AndroidJUnit4::class)
class CalendarInstrumentedTest {
    //instantiates activity at the calendar page
    @Before
    fun setUp() {
        ActivityScenario.launch(FragmentTestActivity::class.java)
            .onActivity { it.supportFragmentManager.beginTransaction().add(it.fragmentView.id, CalendarFragment()).commit() }
    }
    //Test to check if different dates can be selected properly
    @Test
    fun testEditScreen() {
        Espresso.onView(ViewMatchers.withId(R.id.forwardButton))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.backButton))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isClickable()))
        Espresso.onView(ViewMatchers.withId(R.id.sun))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
        Espresso.onView(ViewMatchers.withId(R.id.mon))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
        Espresso.onView(ViewMatchers.withId(R.id.tue))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
        Espresso.onView(ViewMatchers.withId(R.id.wed))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
        Espresso.onView(ViewMatchers.withId(R.id.thur))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
        Espresso.onView(ViewMatchers.withId(R.id.fri))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
        Espresso.onView(ViewMatchers.withId(R.id.sat))
            .perform(ViewActions.click())
            .check(ViewAssertions.matches(ViewMatchers.isSelected()))
    }
    //Test to check if adding a personal event to the calendar works properly
    @Test
    fun testAddPerspnalEventButton() {
        //navigation implemented for test to be able to switch between views
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val calendarScenario = launchFragmentInContainer<CalendarFragment>()
        calendarScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.mobile_navigation3)
            navController.navigate(R.id.nav_calendar_fragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.addEvent))
            .perform(ViewActions.click())
        assertEquals((navController.currentDestination?.id), (R.id.personalEventFragment))
    }
}