package com.example.rxpress10

import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.ui.interaction.DrugInteractionFragment
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

/* This tests the views on drug interaction namely the description text box,
* the menu and the search view*/
@RunWith(AndroidJUnit4::class)
class DrugInteractionInstrumentedTest {

    /* Launches the fragment before testing*/
    @Before
    fun setUp() {
        ActivityScenario.launch(FragmentTestActivity::class.java).onActivity {
            it.supportFragmentManager.beginTransaction()
                .add(it.fragmentView.id, DrugInteractionFragment()).commit()
        }
    }

    /* This tests if the menu is clickable and be able to type in the search bar and checks if the text matches*/
    @Test
    fun searchInteractionTest() {
        onView(withId(R.id.menu_search)).perform(ViewActions.click())
        ViewAssertions.matches(ViewMatchers.isClickable())
        onView(withId(androidx.appcompat.R.id.search_src_text))
            .perform(
                ViewActions.typeText("Cymbalta"),
                ViewActions.pressImeActionButton(),
                ViewActions.closeSoftKeyboard()
            )
            .check(ViewAssertions.matches(ViewMatchers.withText("Cymbalta")))
    }

    /*Test the interaction description views*/
    @Test
    fun testViews() {
        onView(withId(R.id.interaction_description)).check(
            ViewAssertions.matches(
                ViewMatchers.withText(
                    R.string.interaction_help
                )
            )
        )
    }
}