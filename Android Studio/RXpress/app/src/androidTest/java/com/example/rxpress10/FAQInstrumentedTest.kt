package com.example.rxpress10

import androidx.recyclerview.widget.RecyclerView
import androidx.test.core.app.ActivityScenario
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.rxpress10.ui.faq.FAQFragment
import kotlinx.android.synthetic.main.activity_fragment_test.*
import org.hamcrest.CoreMatchers.allOf
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FAQInstrumentedTest {
    //The setUp instantiates the activity at the faq page.
    @Before
    fun setUp() {
        ActivityScenario.launch(FragmentTestActivity::class.java)
            .onActivity { it.supportFragmentManager.beginTransaction().add(it.fragmentView.id, FAQFragment()).commit() }
    }
    //This is a test to see if the question tabs expand
    @Test
    fun testRecyclerView() {
        Espresso.onView((withId(R.id.rv_list)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0,click()))
    }
    //This tests if the navigation links under the question tabs properly navigate to the corresponding page.
    @Test
    fun testFAQNavigation() {
        Espresso.onView((withId(R.id.rv_list)))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(11,click()))
        Espresso.onView(allOf(withId(R.id.tv_link), withText("Go To Profile Page")))
            .perform(ViewActions.scrollTo())
            .check(matches(isDisplayed()))
    }
}