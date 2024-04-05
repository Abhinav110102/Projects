package com.example.rxpress10

import androidx.test.core.app.ActivityScenario
import android.view.LayoutInflater
import android.widget.Button
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.databinding.ActivityCalendarFragmentBinding
import com.example.rxpress10.databinding.ActivityProfileFragmentBinding
import com.example.rxpress10.ui.calendar.CalendarFragment
import com.example.rxpress10.ui.settings.ProfileFragment
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileInstrumentedTest {
    //	testEditProfileButton: Test checks if the user can navigate to edit Profile Fragment Screen  from Profile Fragment Screen.
    @Test
    fun testEditProfileButton() {
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val profileScenario = launchFragmentInContainer<ProfileFragment>()
        profileScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.mobile_navigation3)
            navController.navigate(R.id.profileFragment)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }

        Espresso.onView(ViewMatchers.withId(R.id.editProfile)).perform(ViewActions.click())
        Assert.assertEquals((navController.currentDestination?.id), (R.id.editProfileFragment))
    }
}