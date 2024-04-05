package com.example.rxpress10
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.ui.settings.EditProfileFragment
import com.example.rxpress10.ui.settings.ProfileFragment
import com.example.rxpress10.ui.settings.SettingsFragment
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SettingInstrumentedTest {
    private lateinit var auth: FirebaseAuth

    @Before
    fun setUp() {
        ActivityScenario.launch(FragmentTestActivity::class.java)
            .onActivity { it.supportFragmentManager.beginTransaction().add(it.fragmentView.id, SettingsFragment()).commit() }
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword("coco@gmail.com", "123123")
    }
   //signOut: Test checks if user can sign out of their account.
    @Test
    fun signOut() {
        Espresso.onView(ViewMatchers.withId(R.id.btnLogout))
            .perform(ViewActions.click())
        assert(auth.currentUser == null)
    }
    //goToProfileFromSettings: Test checks if user can navigate from Setting Fragment to Profile Fragment
    @Test
    fun goToProfileFromSettings(){
        val navController = TestNavHostController(ApplicationProvider.getApplicationContext())
        val profileScenario = launchFragmentInContainer<SettingsFragment>()
        profileScenario.onFragment { fragment ->
            navController.setGraph(R.navigation.mobile_navigation3)
            navController.navigate(R.id.nav_settings)
            Navigation.setViewNavController(fragment.requireView(), navController)
        }
        Espresso.onView(withId(R.id.btnProfile)).perform(ViewActions.click())
        Assert.assertEquals((navController.currentDestination?.id), (R.id.profileFragment))

//        ActivityScenario.launch(FragmentTestActivity::class.java)
//        Espresso.onView(ViewMatchers.withId(R.id.btnProfile))
//            .perform(ViewActions.click())
    }

    @After
    fun tearDown() {
        // Sign out the user after the test is finished
        auth.signOut()
    }

}