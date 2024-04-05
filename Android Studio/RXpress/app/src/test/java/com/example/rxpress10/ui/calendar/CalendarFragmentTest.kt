package com.example.rxpress10.ui.calendar

import android.content.Context
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import kotlinx.android.synthetic.main.activity_calendar_fragment.*
import kotlinx.android.synthetic.main.activity_calendar_fragment.view.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.*
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import java.text.SimpleDateFormat
import java.util.*

//Unit test for calendar fragment
private const val FAKE_DAY_NAME = "Sun"

@RunWith(MockitoJUnitRunner::class)
internal class CalendarFragmentTest {
    //Test to ensure proper time formatting
    @Test
    fun testFormatTime() {
        val cal = Calendar.getInstance()
        val t = SimpleDateFormat("hh:mm")
        val formattedTime = CalendarFragment().formatTime(cal)
        assertEquals(formattedTime, t.format(cal.time))
    }
    //Test to ensure proper date formatting
    @Test
    fun testFormatDate() {
        val cal = Calendar.getInstance()
        val d = SimpleDateFormat("mm-dd-yyyy")
        val formattedDate = CalendarFragment().formatDate(cal)
        assertEquals(formattedDate, d.format(cal.time))
    }
    //Test to ensure proper month formatting
    @Test
    fun testFormatMonth() {
        val cal = Calendar.getInstance()
        val m = SimpleDateFormat("MMMM")
        val formattedMonth = CalendarFragment().formatMonth(cal)
        assertEquals(formattedMonth, m.format(cal.time))
    }
    //Test to ensure proper day formatting
    @Test
    fun testFormatDay() {
        val cal = Calendar.getInstance()
        val d = SimpleDateFormat("EEE")
        val formattedDay = CalendarFragment().formatDay(cal)
        assertEquals(formattedDay, d.format(cal.time))
    }
}