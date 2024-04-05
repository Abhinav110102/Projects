package com.example.rxpress10.ui.calendar



import android.R.attr
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivityCalendarFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_calendar_fragment.view.*
import kotlinx.android.synthetic.main.hour_cell.view.*
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*


class CalendarFragment : Fragment(com.example.rxpress10.R.layout.activity_calendar_fragment) {
    lateinit var binding: ActivityCalendarFragmentBinding
    val month = SimpleDateFormat("MMMM")
    val day = SimpleDateFormat("EEE")
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var monthAndYear: String
    private lateinit var currentDate: String
    private lateinit var currentMY: String
    private lateinit var date: String
    private var totalTime: Int = 0
    private lateinit var start: Button
    private lateinit var start2: Button
    private lateinit var start3: Button
    private lateinit var end: Button
    private lateinit var end2: Button
    private lateinit var end3: Button
    private var eTime: Int = 0
    private var sTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarFragmentBinding.inflate(layoutInflater)

    }

    //This function sets the month and year for a specific selected date
    //This is mainly for when there is a week that has two different months in it.
    //It is used with the date clicked function
    fun settingMonthAndYearOnDate(view: TextView, calen: Calendar) {
        //Gets the local date for comparison
        val localDate = LocalDate.now()
        val my = localDate.month.name
        currentMY = my[0] + my.substring(1).toLowerCase() + " " + localDate.year
        val calendar = Calendar.getInstance()

        //Gets the number on the date boxes for comparison
        val sun = binding.sun.text.toString().substringAfter("\n").toInt()
        val mon = binding.mon.text.toString().substringAfter("\n").toInt()
        val tue = binding.tue.text.toString().substringAfter("\n").toInt()
        val wed = binding.wed.text.toString().substringAfter("\n").toInt()
        val thu = binding.thur.text.toString().substringAfter("\n").toInt()
        val fri = binding.fri.text.toString().substringAfter("\n").toInt()
        val sat = binding.sat.text.toString().substringAfter("\n").toInt()

        //If the selected date is the current date and the current month and year do nothing
        //Otherwise see if any day but sunday is the first of another month
        if (binding.monthYear.text.toString() == currentMY && view.text.toString() == getString(R.string.monDate,
                day.format(calendar.time), calendar.get(Calendar.DAY_OF_MONTH))) {

        } else if (mon == 1 || tue == 1 || wed == 1 || thu == 1 || fri == 1 || sat == 1) {
            //If the selected date is Sunday
            //Otherwise if it is Monday
            //Otherwise if it is Tuesday
            //Otherwise if it is Wednesday
            //Otherwise if it is Thursday
            //Otherwise if it is Friday
            //Otherwise if it is Saturday
            if (view.text.toString().substringBefore("\n") == "Sun") {
                //If the number on the current day is greater than or equal to 23
                //This should be the only case since if Sunday is 1 there are not
                //2 months in one week. It then sets the month and year based off of
                //Whether the date clicked is the previous month or the current month
                if (sun >= 23) {
                    var cal = calen.add(Calendar.MONTH, -1)
                    if (binding.monthYear.text.toString().substringBefore(" ") == month.format(calen.time)) {
                        cal = calen.add(Calendar.MONTH, 1)
                    } else {
                        if (binding.monthYear.text.toString().substringBefore(" ") == month.format(calen.time)) {
                            binding.monthYear.text = month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                        } else {
                            cal = calen.add(Calendar.MONTH, 1)
                            if (binding.monthYear.text.toString().substringBefore(" ") == month.format(calen.time)) {
                                cal = calen.add(Calendar.MONTH, -1)
                                binding.monthYear.text = month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                                cal = calen.add(Calendar.MONTH, 1)
                            }
                        }
                    }
                }
            } else if (view.text.toString().substringBefore("\n") == "Mon") {
                //If the date is less than or equal to 6 (leaves one day for the previous month)
                //Set the month and year to the current month and year
                //Otherwise set the month and year to the previous month and potentially year
                if (mon <= 6) {
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                } else {
                    var cal = calen.add(Calendar.MONTH, -1)
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                    cal = calen.add(Calendar.MONTH, 1)
                }
            } else if (view.text.toString().substringBefore("\n") == "Tue") {
                //If the date is less than or equal to 6 (leaves one day for the previous month)
                //Set the month and year to the current month and year
                //Otherwise set the month and year to the previous month and potentially year
                if (tue <= 6) {
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                } else {
                    var cal = calen.add(Calendar.MONTH, -1)
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                    cal = calen.add(Calendar.MONTH, 1)
                }
            } else if (view.text.toString().substringBefore("\n") == "Wed") {
                //If the date is less than or equal to 6 (leaves one day for the previous month)
                //Set the month and year to the current month and year
                //Otherwise set the month and year to the previous month and potentially year
                if (wed <= 6) {
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                } else {
                    var cal = calen.add(Calendar.MONTH, -1)
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                    cal = calen.add(Calendar.MONTH, 1)
                }
            } else if (view.text.toString().substringBefore("\n") == "Thu") {
                //If the date is less than or equal to 6 (leaves one day for the previous month)
                //Set the month and year to the current month and year
                //Otherwise set the month and year to the previous month and potentially year
                if (thu <= 6) {
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                } else {
                    var cal = calen.add(Calendar.MONTH, -1)
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                    cal = calen.add(Calendar.MONTH, 1)
                }
            } else if (view.text.toString().substringBefore("\n") == "Fri") {
                //If the date is less than or equal to 6 (leaves one day for the previous month)
                //Set the month and year to the current month and year
                //Otherwise set the month and year to the previous month and potentially year
                if (fri <= 6) {
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                } else {
                    var cal = calen.add(Calendar.MONTH, -1)
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                    cal = calen.add(Calendar.MONTH, 1)
                }
            } else if (view.text.toString().substringBefore("\n") == "Sat") {
                //If the date is less than or equal to 6 (leaves one day for the previous month)
                //Set the month and year to the current month and year
                //Otherwise set the month and year to the previous month and potentially year
                if (sat <= 6) {
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                } else {
                    var cal = calen.add(Calendar.MONTH, -1)
                    binding.monthYear.text =
                        month.format(calen.time) + " " + calen.get(Calendar.YEAR)
                    cal = calen.add(Calendar.MONTH, 1)
                }
            }
        }
    }

    //This function handles what happens when a date (Sun, Mon, Tue, etc) is selected at the top of the calendar
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun dateClicked(dayName: String, calen: Calendar) {
        //First clear all of the events using the clearAllEvents funciton
        clearAllEvents()
        //If the day name input is Sun set the background of Sun to grey and all of the others to clear
        //Otherwise if the day name input is Mon set the background of Mon to grey and all of the others to clear
        //Otherwise if the day name input is Tue set the background of Tue to grey and all of the others to clear
        //Otherwise if the day name input is Wed set the background of Wed to grey and all of the others to clear
        //Otherwise if the day name input is Thu set the background of Thu to grey and all of the others to clear
        //Otherwise if the day name input is Fri set the background of Fri to grey and all of the others to clear
        //Otherwise if the day name input is Sat set the background of Sat to grey and all of the others to clear
        if (dayName == "Sun"){
            binding.sun.background = resources.getDrawable(com.example.rxpress10.R.drawable.selected)
            binding.mon.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.tue.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.wed.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.thur.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.fri.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.sat.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.mon.isSelected) {
                binding.mon.isSelected = false
            } else if (binding.tue.isSelected) {
                binding.tue.isSelected = false
            } else if (binding.wed.isSelected) {
                binding.wed.isSelected = false
            } else if (binding.thur.isSelected) {
                binding.thur.isSelected = false
            } else if (binding.fri.isSelected) {
                binding.fri.isSelected = false
            } else if (binding.sat.isSelected) {
                binding.sat.isSelected = false
            }
            //Set the selection of sunday to true
            binding.sun.isSelected = true
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
            settingMonthAndYearOnDate(binding.sun, calen)
        } else if (dayName == "Mon"){
            binding.mon.background = resources.getDrawable(com.example.rxpress10.R.drawable.selected)
            binding.sun.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.tue.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.wed.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.thur.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.fri.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.sat.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.sun.isSelected) {
                binding.sun.isSelected = false
            } else if (binding.tue.isSelected) {
                binding.tue.isSelected = false
            } else if (binding.wed.isSelected) {
                binding.wed.isSelected = false
            } else if (binding.thur.isSelected) {
                binding.thur.isSelected = false
            } else if (binding.fri.isSelected) {
                binding.fri.isSelected = false
            } else if (binding.sat.isSelected) {
                binding.sat.isSelected = false
            }
            //Set the selection of monday to true
            binding.mon.isSelected = true
            settingMonthAndYearOnDate(binding.mon, calen)
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
        }else if (dayName == "Tue"){
            binding.tue.background = resources.getDrawable(com.example.rxpress10.R.drawable.selected)
            binding.sun.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.mon.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.wed.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.thur.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.fri.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            binding.sat.background = resources.getDrawable(com.example.rxpress10.R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.mon.isSelected) {
                binding.mon.isSelected = false
            } else if (binding.sun.isSelected) {
                binding.sun.isSelected = false
            } else if (binding.wed.isSelected) {
                binding.wed.isSelected = false
            } else if (binding.thur.isSelected) {
                binding.thur.isSelected = false
            } else if (binding.fri.isSelected) {
                binding.fri.isSelected = false
            } else if (binding.sat.isSelected) {
                binding.sat.isSelected = false
            }
            //Set the selection of tuesday to true
            binding.tue.isSelected = true
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
            settingMonthAndYearOnDate(binding.tue, calen)
        } else if (dayName == "Wed"){
            binding.wed.background = resources.getDrawable(R.drawable.selected)
            binding.sun.background = resources.getDrawable(R.color.clear)
            binding.mon.background = resources.getDrawable(R.color.clear)
            binding.tue.background = resources.getDrawable(R.color.clear)
            binding.thur.background = resources.getDrawable(R.color.clear)
            binding.fri.background = resources.getDrawable(R.color.clear)
            binding.sat.background = resources.getDrawable(R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.mon.isSelected) {
                binding.mon.isSelected = false
            } else if (binding.tue.isSelected) {
                binding.tue.isSelected = false
            } else if (binding.sun.isSelected) {
                binding.sun.isSelected = false
            } else if (binding.thur.isSelected) {
                binding.thur.isSelected = false
            } else if (binding.fri.isSelected) {
                binding.fri.isSelected = false
            } else if (binding.sat.isSelected) {
                binding.sat.isSelected = false
            }
            //Set the selection of wednesday to true
            binding.wed.isSelected = true
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
            settingMonthAndYearOnDate(binding.wed, calen)
        } else if (dayName == "Thu"){
            binding.thur.background = resources.getDrawable(R.drawable.selected)
            binding.sun.background = resources.getDrawable(R.color.clear)
            binding.mon.background = resources.getDrawable(R.color.clear)
            binding.tue.background = resources.getDrawable(R.color.clear)
            binding.wed.background = resources.getDrawable(R.color.clear)
            binding.fri.background = resources.getDrawable(R.color.clear)
            binding.sat.background = resources.getDrawable(R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.mon.isSelected) {
                binding.mon.isSelected = false
            } else if (binding.tue.isSelected) {
                binding.tue.isSelected = false
            } else if (binding.wed.isSelected) {
                binding.wed.isSelected = false
            } else if (binding.sun.isSelected) {
                binding.sun.isSelected = false
            } else if (binding.fri.isSelected) {
                binding.fri.isSelected = false
            } else if (binding.sat.isSelected) {
                binding.sat.isSelected = false
            }
            //Set the selection of thursday to true
            binding.thur.isSelected = true
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
            settingMonthAndYearOnDate(binding.thur, calen)
        } else if (dayName == "Fri"){
            binding.fri.background = resources.getDrawable(R.drawable.selected)
            binding.sun.background = resources.getDrawable(R.color.clear)
            binding.mon.background = resources.getDrawable(R.color.clear)
            binding.tue.background = resources.getDrawable(R.color.clear)
            binding.wed.background = resources.getDrawable(R.color.clear)
            binding.thur.background = resources.getDrawable(R.color.clear)
            binding.sat.background = resources.getDrawable(R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.mon.isSelected) {
                binding.mon.isSelected = false
            } else if (binding.tue.isSelected) {
                binding.tue.isSelected = false
            } else if (binding.wed.isSelected) {
                binding.wed.isSelected = false
            } else if (binding.thur.isSelected) {
                binding.thur.isSelected = false
            } else if (binding.sun.isSelected) {
                binding.sun.isSelected = false
            } else if (binding.sat.isSelected) {
                binding.sat.isSelected = false
            }
            //Set the selection of friday to true
            binding.fri.isSelected = true
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
            settingMonthAndYearOnDate(binding.fri, calen)
        } else if (dayName == "Sat") {
            binding.sat.background = resources.getDrawable(R.drawable.selected)
            binding.sun.background = resources.getDrawable(R.color.clear)
            binding.mon.background = resources.getDrawable(R.color.clear)
            binding.tue.background = resources.getDrawable(R.color.clear)
            binding.wed.background = resources.getDrawable(R.color.clear)
            binding.thur.background = resources.getDrawable(R.color.clear)
            binding.fri.background = resources.getDrawable(R.color.clear)
            //If any of the other dates are currently selected, set their selection to false
            if (binding.mon.isSelected) {
                binding.mon.isSelected = false
            } else if (binding.tue.isSelected) {
                binding.tue.isSelected = false
            } else if (binding.wed.isSelected) {
                binding.wed.isSelected = false
            } else if (binding.thur.isSelected) {
                binding.thur.isSelected = false
            } else if (binding.fri.isSelected) {
                binding.fri.isSelected = false
            } else if (binding.sun.isSelected) {
                binding.sun.isSelected = false
            }
            //Set the selection of saturday to true
            binding.sat.isSelected = true
            //Use the settingMonthAndYearOnDate to set the month and year of the date clicked (previous month/year or current month/year)
            settingMonthAndYearOnDate(binding.sat, calen)
        }
        //Checks if the current date is in the current week and if so, change the background to a gradient for easy recognition
        setBackground()
        //Set any personal events for the selected day
        setPersonalEvents()
    }

    //Sets the month and year at the top of the screen at the beginning of opening the clanedar and on the forwards and backwards button click
    fun setMonthAndYear(calendar: Calendar, int: Int) {
        //First clear all events using the clearAllEvents function
        clearAllEvents()
        //Forward button was pressed
        if (int == 1) {
            //Go to the next week in the month, get the day name and the day num
            val dayNext = calendar.add(Calendar.WEEK_OF_MONTH, 1)
            var dayName = day.format(calendar.time)
            var dayNum = calendar.get(Calendar.DAY_OF_MONTH)
            //If the name is Sat change it to Sun and subtract 6 from the day so that sunday is always selected
            if (dayName == "Sat") {
                dayName = "Sun"
                var dNext = calendar.add(Calendar.DAY_OF_WEEK, -6)
                dayNum = calendar.get(Calendar.DAY_OF_MONTH)
                var monthName = month.format(calendar.time)
                var year = calendar.get(Calendar.YEAR)
                monthAndYear = monthName + " " + year
                binding.monthYear.text = getString(R.string.mYear, monthAndYear)

                //set the current date using the setCurrentDate function
                setCurrentDate(dayName, monthAndYear)

            }
            //Call the getDateBar function in order to set the dates on the date bar
            getDateBar(calendar, dayName, dayNum)
            //Make Sunday the selected day
            dateClicked("Sun", calendar)
        }
        //Back button was pressed
        else {
            //Get the previous week in the month, get the day name, day num, month name, and year
            val dayPrev = calendar.add(Calendar.WEEK_OF_MONTH, -1)
            var dayName = day.format(calendar.time)
            var dayNum = calendar.get(Calendar.DAY_OF_MONTH)
            val monthName = month.format(calendar.time)
            val year = calendar.get(Calendar.YEAR)

            //if the day name is Sat change it to Sun and subtract 6 from the day of the week so that it is sun
            if (dayName == "Sat") {
                dayName = "Sun"
                var dNext = calendar.add(Calendar.DAY_OF_WEEK, -6)
                dayNum = calendar.get(Calendar.DAY_OF_MONTH)
                var monthName = month.format(calendar.time)
                var year = calendar.get(Calendar.YEAR)
                //Set the month and year title
                monthAndYear = monthName + " " + year
                binding.monthYear.text = getString(R.string.mYear, monthAndYear)

                //set the current date using the setCurrentDate function
                setCurrentDate(dayName, monthAndYear)

            } else {
                //same as previous statement but not setting the day name to sunday since it is already sunday
                monthAndYear = monthName + " " + year
                binding.monthYear.text = getString(R.string.mYear, monthAndYear)
                setCurrentDate(dayName, monthAndYear)
            }
            //Use the getDateBar function in order to set the date bar at the top of the screen
            getDateBar(calendar, dayName, dayNum)
            //Make the selected day Sunday
            dateClicked("Sun", calendar)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //Clear all events using the clearAllEvents function
        clearAllEvents()
        //Set all days to not selected
        binding.sun.isSelected = false
        binding.mon.isSelected = false
        binding.tue.isSelected = false
        binding.wed.isSelected = false
        binding.thur.isSelected = false
        binding.fri.isSelected = false
        binding.sat.isSelected = false

        //Gets current month and year and sets the title to it
        val calendar = Calendar.getInstance()
        val localDate = LocalDate.now()
        val monthName = month.format(calendar.time)
        val year = calendar.get(Calendar.YEAR)
        val monthAndYear = monthName + " " + year
        //Sets format for dates and the days (i.e. Sun 11)
        val dayName = day.format(calendar.time)
        val dayNum = calendar.get(Calendar.DAY_OF_MONTH)

        //Set the month and year text at the top to the current month and year
        binding.monthYear.text = getString(com.example.rxpress10.R.string.mYear, monthAndYear)
        //Listens for a click on the back button then call the setMonthAndYear function
        binding.backButton.setOnClickListener { setMonthAndYear(calendar, 2) }
        //Listens for a click on the forward button then call the setMonthAndYear function
        binding.forwardButton.setOnClickListener { setMonthAndYear(calendar, 1) }

        //Listens for a click on any of the days at the top of the calendar and then calls the dateClicked function with the corresponding name
        binding.sun.setOnClickListener { dateClicked(dayName = "Sun", calendar) }
        binding.mon.setOnClickListener { dateClicked(dayName = "Mon", calendar) }
        binding.tue.setOnClickListener { dateClicked(dayName = "Tue", calendar) }
        binding.wed.setOnClickListener { dateClicked(dayName = "Wed", calendar) }
        binding.thur.setOnClickListener { dateClicked(dayName = "Thu", calendar) }
        binding.fri.setOnClickListener { dateClicked(dayName = "Fri", calendar) }
        binding.sat.setOnClickListener { dateClicked(dayName = "Sat", calendar) }

        //Listens for a click on the add event icon at the bottom of the calendar screen and then calls the addEventClicked function
        binding.addEvent.setOnClickListener { addEventClicked() }

        //Sets the current date using the day name, month, and year
        setCurrentDate(dayName, monthAndYear)
        //Sets the users personal events for the current date
        setPersonalEvents()

        //Listens for clicks on any of the events in the time portion of the clanedar and then calls the eventClicked function
        binding.all1.setOnClickListener { eventClicked(medName = binding.all1.text.toString(), binding.all1)}
        binding.all2.setOnClickListener { eventClicked(medName = binding.all2.text.toString(), binding.all2)}
        binding.all3.setOnClickListener { eventClicked(medName = binding.all3.text.toString(), binding.all3)}
        binding.prescript1a.setOnClickListener { eventClicked( medName = binding.prescript1a.text.toString(), binding.prescript1a)}
        binding.prescript1a2.setOnClickListener { eventClicked( medName = binding.prescript1a2.text.toString(), binding.prescript1a2)}
        binding.prescript1a3.setOnClickListener { eventClicked( medName = binding.prescript1a3.text.toString(), binding.prescript1a3)}
        binding.prescript2a.setOnClickListener { eventClicked( medName = binding.prescript2a.text.toString(), binding.prescript2a)}
        binding.prescript2a2.setOnClickListener { eventClicked( medName = binding.prescript2a2.text.toString(), binding.prescript2a2)}
        binding.prescript2a3.setOnClickListener { eventClicked( medName = binding.prescript2a3.text.toString(), binding.prescript2a3)}
        binding.prescript3a.setOnClickListener { eventClicked( medName = binding.prescript3a.text.toString(), binding.prescript3a)}
        binding.prescript3a2.setOnClickListener { eventClicked( medName = binding.prescript3a2.text.toString(), binding.prescript3a2)}
        binding.prescript3a3.setOnClickListener { eventClicked( medName = binding.prescript3a3.text.toString(), binding.prescript3a3)}
        binding.prescript4a.setOnClickListener { eventClicked( medName = binding.prescript4a.text.toString(), binding.prescript4a)}
        binding.prescript4a2.setOnClickListener { eventClicked( medName = binding.prescript4a2.text.toString(), binding.prescript4a2)}
        binding.prescript4a3.setOnClickListener { eventClicked( medName = binding.prescript4a3.text.toString(), binding.prescript4a3)}
        binding.prescript5a.setOnClickListener { eventClicked( medName = binding.prescript5a.text.toString(), binding.prescript5a)}
        binding.prescript5a2.setOnClickListener { eventClicked( medName = binding.prescript5a2.text.toString(), binding.prescript5a2)}
        binding.prescript5a3.setOnClickListener { eventClicked( medName = binding.prescript5a3.text.toString(), binding.prescript5a3)}
        binding.prescript6a.setOnClickListener { eventClicked( medName = binding.prescript6a.text.toString(), binding.prescript6a)}
        binding.prescript6a2.setOnClickListener { eventClicked( medName = binding.prescript6a2.text.toString(), binding.prescript6a2)}
        binding.prescript6a3.setOnClickListener { eventClicked( medName = binding.prescript6a3.text.toString(), binding.prescript6a3)}
        binding.prescript7a.setOnClickListener { eventClicked( medName = binding.prescript7a.text.toString(), binding.prescript7a)}
        binding.prescript7a2.setOnClickListener { eventClicked( medName = binding.prescript7a2.text.toString(), binding.prescript7a2)}
        binding.prescript7a3.setOnClickListener { eventClicked( medName = binding.prescript7a3.text.toString(), binding.prescript7a3)}
        binding.prescript8a.setOnClickListener { eventClicked( medName = binding.prescript8a.text.toString(), binding.prescript8a)}
        binding.prescript8a2.setOnClickListener { eventClicked( medName = binding.prescript8a2.text.toString(), binding.prescript8a2)}
        binding.prescript8a3.setOnClickListener { eventClicked( medName = binding.prescript8a3.text.toString(), binding.prescript8a3)}
        binding.prescript9a.setOnClickListener { eventClicked( medName = binding.prescript9a.text.toString(), binding.prescript9a)}
        binding.prescript9a2.setOnClickListener { eventClicked( medName = binding.prescript9a2.text.toString(), binding.prescript9a2)}
        binding.prescript9a3.setOnClickListener { eventClicked( medName = binding.prescript9a3.text.toString(), binding.prescript9a3)}
        binding.prescript10a.setOnClickListener { eventClicked( medName = binding.prescript10a.text.toString(), binding.prescript10a)}
        binding.prescript10a2.setOnClickListener { eventClicked( medName = binding.prescript10a2.text.toString(), binding.prescript10a2)}
        binding.prescript10a3.setOnClickListener { eventClicked( medName = binding.prescript10a3.text.toString(), binding.prescript10a3)}
        binding.prescript11a.setOnClickListener { eventClicked( medName = binding.prescript11a.text.toString(), binding.prescript11a)}
        binding.prescript11a2.setOnClickListener { eventClicked( medName = binding.prescript11a2.text.toString(), binding.prescript11a2)}
        binding.prescript11a3.setOnClickListener { eventClicked( medName = binding.prescript11a3.text.toString(), binding.prescript11a3)}
        binding.prescript12p.setOnClickListener { eventClicked( medName = binding.prescript12p.text.toString(), binding.prescript12p)}
        binding.prescript12p2.setOnClickListener { eventClicked( medName = binding.prescript12p2.text.toString(), binding.prescript12p2)}
        binding.prescript12p3.setOnClickListener { eventClicked( medName = binding.prescript12p3.text.toString(), binding.prescript12p3)}
        binding.prescript1p.setOnClickListener { eventClicked( medName = binding.prescript1p.text.toString(), binding.prescript1p)}
        binding.prescript1p2.setOnClickListener { eventClicked( medName = binding.prescript1p2.text.toString(), binding.prescript1p2)}
        binding.prescript1p3.setOnClickListener { eventClicked( medName = binding.prescript1p3.text.toString(), binding.prescript1p3)}
        binding.prescript2p.setOnClickListener { eventClicked( medName = binding.prescript2p.text.toString(), binding.prescript2p)}
        binding.prescript2p2.setOnClickListener { eventClicked( medName = binding.prescript2p2.text.toString(), binding.prescript2p2)}
        binding.prescript2p3.setOnClickListener { eventClicked( medName = binding.prescript2p3.text.toString(), binding.prescript2p3)}
        binding.prescript3p.setOnClickListener { eventClicked( medName = binding.prescript3p.text.toString(), binding.prescript3p)}
        binding.prescript3p2.setOnClickListener { eventClicked( medName = binding.prescript3p2.text.toString(), binding.prescript3p2)}
        binding.prescript3p3.setOnClickListener { eventClicked( medName = binding.prescript3p3.text.toString(), binding.prescript3p3)}
        binding.prescript4p.setOnClickListener { eventClicked( medName = binding.prescript4p.text.toString(), binding.prescript4p)}
        binding.prescript4p2.setOnClickListener { eventClicked( medName = binding.prescript4p2.text.toString(), binding.prescript4p2)}
        binding.prescript4p3.setOnClickListener { eventClicked( medName = binding.prescript4p3.text.toString(), binding.prescript4p3)}
        binding.prescript5p.setOnClickListener { eventClicked( medName = binding.prescript5p.text.toString(), binding.prescript5p)}
        binding.prescript5p2.setOnClickListener { eventClicked( medName = binding.prescript5p2.text.toString(), binding.prescript5p2)}
        binding.prescript5p3.setOnClickListener { eventClicked( medName = binding.prescript5p3.text.toString(), binding.prescript5p3)}
        binding.prescript6p.setOnClickListener { eventClicked( medName = binding.prescript6p.text.toString(), binding.prescript6p)}
        binding.prescript6p2.setOnClickListener { eventClicked( medName = binding.prescript6p2.text.toString(), binding.prescript6p2)}
        binding.prescript6p3.setOnClickListener { eventClicked( medName = binding.prescript6p3.text.toString(), binding.prescript6p3)}
        binding.prescript7p.setOnClickListener { eventClicked( medName = binding.prescript7p.text.toString(), binding.prescript7p)}
        binding.prescript7p2.setOnClickListener { eventClicked( medName = binding.prescript7p2.text.toString(), binding.prescript7p2)}
        binding.prescript7p3.setOnClickListener { eventClicked( medName = binding.prescript7p3.text.toString(), binding.prescript7p3)}
        binding.prescript8p.setOnClickListener { eventClicked( medName = binding.prescript8p.text.toString(), binding.prescript8p)}
        binding.prescript8p2.setOnClickListener { eventClicked( medName = binding.prescript8p2.text.toString(), binding.prescript8p2)}
        binding.prescript8p3.setOnClickListener { eventClicked( medName = binding.prescript8p3.text.toString(), binding.prescript8p3)}
        binding.prescript9p.setOnClickListener { eventClicked( medName = binding.prescript9p.text.toString(), binding.prescript9p)}
        binding.prescript9p2.setOnClickListener { eventClicked( medName = binding.prescript9p2.text.toString(), binding.prescript9p2)}
        binding.prescript9p3.setOnClickListener { eventClicked( medName = binding.prescript9p3.text.toString(), binding.prescript9p3)}
        binding.prescript10p.setOnClickListener { eventClicked( medName = binding.prescript10p.text.toString(), binding.prescript10p)}
        binding.prescript10p2.setOnClickListener { eventClicked( medName = binding.prescript10p2.text.toString(), binding.prescript10p2)}
        binding.prescript10p3.setOnClickListener { eventClicked( medName = binding.prescript10p3.text.toString(), binding.prescript10p3)}
        binding.prescript11p.setOnClickListener { eventClicked( medName = binding.prescript11p.text.toString(), binding.prescript11p)}
        binding.prescript11p2.setOnClickListener { eventClicked( medName = binding.prescript11p2.text.toString(), binding.prescript11p2)}
        binding.prescript11p3.setOnClickListener { eventClicked( medName = binding.prescript11p3.text.toString(), binding.prescript11p3)}
        binding.prescript12a.setOnClickListener { eventClicked( medName = binding.prescript12a.text.toString(), binding.prescript12a)}
        binding.prescript12a2.setOnClickListener { eventClicked( medName = binding.prescript12a2.text.toString(), binding.prescript12a2)}
        binding.prescript12a3.setOnClickListener { eventClicked( medName = binding.prescript12a3.text.toString(), binding.prescript12a3)}

        //Calls function to get the dates and days to show up under the month and year
        getDateBar(calendar, dayName, dayNum)

        return binding.root

    }

    //This function sets the background and selection of the current date on the date bar
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setCurrentDate (dayName: String, ma: String) {
        val localDate = LocalDate.now()
        val my = localDate.month.name
        currentMY = my[0]+my.substring(1).toLowerCase()+" "+localDate.year

        //If the day name is Sun and the month and year is the current month and year set the background to a gradient
        //Otherwise check for the other days
        if (dayName == "Sun" && ma == currentMY) {
            binding.sun.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set sunday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sat.background = resources.getDrawable(R.color.clear)
                binding.mon.background = resources.getDrawable(R.color.clear)
                binding.tue.background = resources.getDrawable(R.color.clear)
                binding.wed.background = resources.getDrawable(R.color.clear)
                binding.thur.background = resources.getDrawable(R.color.clear)
                binding.fri.background = resources.getDrawable(R.color.clear)
                binding.sun.isSelected = true
            }
        } else if (dayName == "Mon" && ma == currentMY) {
            binding.mon.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set monday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sun.background = resources.getDrawable(R.color.clear)
                binding.sat.background = resources.getDrawable(R.color.clear)
                binding.tue.background = resources.getDrawable(R.color.clear)
                binding.wed.background = resources.getDrawable(R.color.clear)
                binding.thur.background = resources.getDrawable(R.color.clear)
                binding.fri.background = resources.getDrawable(R.color.clear)
                binding.mon.isSelected = true
            }
        } else if (dayName == "Tue" && ma == currentMY) {
            binding.tue.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set tuesday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sun.background = resources.getDrawable(R.color.clear)
                binding.mon.background = resources.getDrawable(R.color.clear)
                binding.sat.background = resources.getDrawable(R.color.clear)
                binding.wed.background = resources.getDrawable(R.color.clear)
                binding.thur.background = resources.getDrawable(R.color.clear)
                binding.fri.background = resources.getDrawable(R.color.clear)
                binding.tue.isSelected = true
            }
        } else if (dayName == "Wed" && ma == currentMY) {
            binding.wed.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set wednesday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sun.background = resources.getDrawable(R.color.clear)
                binding.mon.background = resources.getDrawable(R.color.clear)
                binding.tue.background = resources.getDrawable(R.color.clear)
                binding.sat.background = resources.getDrawable(R.color.clear)
                binding.thur.background = resources.getDrawable(R.color.clear)
                binding.fri.background = resources.getDrawable(R.color.clear)
                binding.wed.isSelected = true
            }
        } else if (dayName == "Thu" && ma == currentMY) {
            binding.thur.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set thursday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sun.background = resources.getDrawable(R.color.clear)
                binding.mon.background = resources.getDrawable(R.color.clear)
                binding.tue.background = resources.getDrawable(R.color.clear)
                binding.wed.background = resources.getDrawable(R.color.clear)
                binding.sat.background = resources.getDrawable(R.color.clear)
                binding.fri.background = resources.getDrawable(R.color.clear)
                binding.thur.isSelected = true
            }
        } else if (dayName == "Fri" && ma == currentMY) {
            binding.fri.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set friday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sun.background = resources.getDrawable(R.color.clear)
                binding.mon.background = resources.getDrawable(R.color.clear)
                binding.tue.background = resources.getDrawable(R.color.clear)
                binding.wed.background = resources.getDrawable(R.color.clear)
                binding.thur.background = resources.getDrawable(R.color.clear)
                binding.sat.background = resources.getDrawable(R.color.clear)
                binding.fri.isSelected = true
            }
        } else if (dayName == "Sat" && ma == currentMY) {
            binding.sat.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
            //If any of the other dates are selected do nothing
            //Otherwise set the other backgrounds to clear and set saturday to selected
            if (binding.sun.isSelected || binding.mon.isSelected || binding.tue.isSelected || binding.wed.isSelected ||
                binding.thur.isSelected || binding.fri.isSelected || binding.sat.isSelected) {
            } else {
                binding.sun.background = resources.getDrawable(R.color.clear)
                binding.mon.background = resources.getDrawable(R.color.clear)
                binding.tue.background = resources.getDrawable(R.color.clear)
                binding.wed.background = resources.getDrawable(R.color.clear)
                binding.thur.background = resources.getDrawable(R.color.clear)
                binding.fri.background = resources.getDrawable(R.color.clear)
                binding.sat.isSelected = true
            }
        }
    }

    //Resets all buttons on the calendar to their initial state of invisible, initial string value, text color white, and background to teal_200
    fun clearAllEvents() {
        binding.all1.visibility = View.INVISIBLE
        binding.all1.text = getString(R.string.eventA)
        binding.all2.visibility = View.INVISIBLE
        binding.all2.text = getString(R.string.eventA)
        binding.all3.visibility = View.INVISIBLE
        binding.all3.text = getString(R.string.eventA)
        binding.prescript1a.visibility = View.INVISIBLE
        binding.prescript1a.text = getString(R.string.event1)
        binding.prescript1a.setTextColor(Color.WHITE)
        binding.prescript1a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript1a2.visibility = View.INVISIBLE
        binding.prescript1a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript1a2.text = getString(R.string.event1)
        binding.prescript1a2.setTextColor(Color.WHITE)
        binding.prescript1a3.visibility = View.INVISIBLE
        binding.prescript1a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript1a3.text = getString(R.string.event1)
        binding.prescript1a3.setTextColor(Color.WHITE)
        binding.prescript2a.visibility = View.INVISIBLE
        binding.prescript2a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript2a.text = getString(R.string.event1)
        binding.prescript2a.setTextColor(Color.WHITE)
        binding.prescript2a2.visibility = View.INVISIBLE
        binding.prescript2a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript2a2.text = getString(R.string.event1)
        binding.prescript2a2.setTextColor(Color.WHITE)
        binding.prescript2a3.visibility = View.INVISIBLE
        binding.prescript2a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript2a3.text = getString(R.string.event1)
        binding.prescript2a3.setTextColor(Color.WHITE)
        binding.prescript3a.visibility = View.INVISIBLE
        binding.prescript3a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript3a.text = getString(R.string.event1)
        binding.prescript3a.setTextColor(Color.WHITE)
        binding.prescript3a2.visibility = View.INVISIBLE
        binding.prescript3a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript3a2.text = getString(R.string.event1)
        binding.prescript3a2.setTextColor(Color.WHITE)
        binding.prescript3a3.visibility = View.INVISIBLE
        binding.prescript3a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript3a3.text = getString(R.string.event1)
        binding.prescript3a3.setTextColor(Color.WHITE)
        binding.prescript4a.visibility = View.INVISIBLE
        binding.prescript4a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript4a.text = getString(R.string.event1)
        binding.prescript4a.setTextColor(Color.WHITE)
        binding.prescript4a2.visibility = View.INVISIBLE
        binding.prescript4a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript4a2.text = getString(R.string.event1)
        binding.prescript4a2.setTextColor(Color.WHITE)
        binding.prescript4a3.visibility = View.INVISIBLE
        binding.prescript4a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript4a3.text = getString(R.string.event1)
        binding.prescript4a3.setTextColor(Color.WHITE)
        binding.prescript5a.visibility = View.INVISIBLE
        binding.prescript5a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript5a.text = getString(R.string.event1)
        binding.prescript5a.setTextColor(Color.WHITE)
        binding.prescript5a2.visibility = View.INVISIBLE
        binding.prescript5a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript5a2.text = getString(R.string.event1)
        binding.prescript5a2.setTextColor(Color.WHITE)
        binding.prescript5a3.visibility = View.INVISIBLE
        binding.prescript5a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript5a3.text = getString(R.string.event1)
        binding.prescript5a3.setTextColor(Color.WHITE)
        binding.prescript6a.visibility = View.INVISIBLE
        binding.prescript6a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript6a.text = getString(R.string.event1)
        binding.prescript6a.setTextColor(Color.WHITE)
        binding.prescript6a2.visibility = View.INVISIBLE
        binding.prescript6a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript6a2.text = getString(R.string.event1)
        binding.prescript6a2.setTextColor(Color.WHITE)
        binding.prescript6a3.visibility = View.INVISIBLE
        binding.prescript6a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript6a3.text = getString(R.string.event1)
        binding.prescript6a3.setTextColor(Color.WHITE)
        binding.prescript7a.visibility = View.INVISIBLE
        binding.prescript7a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript7a.text = getString(R.string.event1)
        binding.prescript7a.setTextColor(Color.WHITE)
        binding.prescript7a2.visibility = View.INVISIBLE
        binding.prescript7a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript7a2.text = getString(R.string.event1)
        binding.prescript7a2.setTextColor(Color.WHITE)
        binding.prescript7a3.visibility = View.INVISIBLE
        binding.prescript7a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript7a3.text = getString(R.string.event1)
        binding.prescript7a3.setTextColor(Color.WHITE)
        binding.prescript8a.visibility = View.INVISIBLE
        binding.prescript8a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript8a.text = getString(R.string.event1)
        binding.prescript8a.setTextColor(Color.WHITE)
        binding.prescript8a2.visibility = View.INVISIBLE
        binding.prescript8a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript8a2.text = getString(R.string.event1)
        binding.prescript8a2.setTextColor(Color.WHITE)
        binding.prescript8a3.visibility = View.INVISIBLE
        binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript8a3.text = getString(R.string.event1)
        binding.prescript8a3.setTextColor(Color.WHITE)
        binding.prescript9a.visibility = View.INVISIBLE
        binding.prescript9a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript9a.text = getString(R.string.event1)
        binding.prescript9a.setTextColor(Color.WHITE)
        binding.prescript9a2.visibility = View.INVISIBLE
        binding.prescript9a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript9a2.text = getString(R.string.event1)
        binding.prescript9a2.setTextColor(Color.WHITE)
        binding.prescript9a3.visibility = View.INVISIBLE
        binding.prescript9a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript9a3.text = getString(R.string.event1)
        binding.prescript9a3.setTextColor(Color.WHITE)
        binding.prescript10a.visibility = View.INVISIBLE
        binding.prescript10a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript10a.text = getString(R.string.event1)
        binding.prescript10a.setTextColor(Color.WHITE)
        binding.prescript10a2.visibility = View.INVISIBLE
        binding.prescript10a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript10a2.text = getString(R.string.event1)
        binding.prescript10a2.setTextColor(Color.WHITE)
        binding.prescript10a3.visibility = View.INVISIBLE
        binding.prescript10a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript10a3.text = getString(R.string.event1)
        binding.prescript10a3.setTextColor(Color.WHITE)
        binding.prescript11a.visibility = View.INVISIBLE
        binding.prescript11a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript11a.text = getString(R.string.event1)
        binding.prescript11a.setTextColor(Color.WHITE)
        binding.prescript11a2.visibility = View.INVISIBLE
        binding.prescript11a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript11a2.text = getString(R.string.event1)
        binding.prescript11a2.setTextColor(Color.WHITE)
        binding.prescript11a3.visibility = View.INVISIBLE
        binding.prescript11a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript11a3.text = getString(R.string.event1)
        binding.prescript11a3.setTextColor(Color.WHITE)
        binding.prescript12p.visibility = View.INVISIBLE
        binding.prescript12p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript12p.text = getString(R.string.event1)
        binding.prescript12p.setTextColor(Color.WHITE)
        binding.prescript12p2.visibility = View.INVISIBLE
        binding.prescript12p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript12p2.text = getString(R.string.event1)
        binding.prescript12p2.setTextColor(Color.WHITE)
        binding.prescript12p3.visibility = View.INVISIBLE
        binding.prescript12p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript12p3.text = getString(R.string.event1)
        binding.prescript12p3.setTextColor(Color.WHITE)
        binding.prescript1p.visibility = View.INVISIBLE
        binding.prescript1p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript1p.text = getString(R.string.event1)
        binding.prescript1p.setTextColor(Color.WHITE)
        binding.prescript1p2.visibility = View.INVISIBLE
        binding.prescript1p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript1p2.text = getString(R.string.event1)
        binding.prescript1p2.setTextColor(Color.WHITE)
        binding.prescript1p3.visibility = View.INVISIBLE
        binding.prescript1p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript1p3.text = getString(R.string.event1)
        binding.prescript1p3.setTextColor(Color.WHITE)
        binding.prescript2p.visibility = View.INVISIBLE
        binding.prescript2p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript2p.text = getString(R.string.event1)
        binding.prescript2p.setTextColor(Color.WHITE)
        binding.prescript2p2.visibility = View.INVISIBLE
        binding.prescript2p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript2p2.text = getString(R.string.event1)
        binding.prescript2p2.setTextColor(Color.WHITE)
        binding.prescript2p3.visibility = View.INVISIBLE
        binding.prescript2p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript2p3.text = getString(R.string.event1)
        binding.prescript2p3.setTextColor(Color.WHITE)
        binding.prescript3p.visibility = View.INVISIBLE
        binding.prescript3p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript3p.text = getString(R.string.event1)
        binding.prescript3p.setTextColor(Color.WHITE)
        binding.prescript3p2.visibility = View.INVISIBLE
        binding.prescript3p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript3p2.text = getString(R.string.event1)
        binding.prescript3p2.setTextColor(Color.WHITE)
        binding.prescript3p3.visibility = View.INVISIBLE
        binding.prescript3p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript3p3.text = getString(R.string.event1)
        binding.prescript3p3.setTextColor(Color.WHITE)
        binding.prescript4p.visibility = View.INVISIBLE
        binding.prescript4p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript4p.text = getString(R.string.event1)
        binding.prescript4p.setTextColor(Color.WHITE)
        binding.prescript4p2.visibility = View.INVISIBLE
        binding.prescript4p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript4p2.text = getString(R.string.event1)
        binding.prescript4p2.setTextColor(Color.WHITE)
        binding.prescript4p3.visibility = View.INVISIBLE
        binding.prescript4p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript4p3.text = getString(R.string.event1)
        binding.prescript4p3.setTextColor(Color.WHITE)
        binding.prescript5p.visibility = View.INVISIBLE
        binding.prescript5p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript5p.text = getString(R.string.event1)
        binding.prescript5p.setTextColor(Color.WHITE)
        binding.prescript5p2.visibility = View.INVISIBLE
        binding.prescript5p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript5p2.text = getString(R.string.event1)
        binding.prescript5p2.setTextColor(Color.WHITE)
        binding.prescript5p3.visibility = View.INVISIBLE
        binding.prescript5p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript5p3.text = getString(R.string.event1)
        binding.prescript5p3.setTextColor(Color.WHITE)
        binding.prescript6p.visibility = View.INVISIBLE
        binding.prescript6p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript6p.text = getString(R.string.event1)
        binding.prescript6p.setTextColor(Color.WHITE)
        binding.prescript6p2.visibility = View.INVISIBLE
        binding.prescript6p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript6p2.text = getString(R.string.event1)
        binding.prescript6p2.setTextColor(Color.WHITE)
        binding.prescript6p3.visibility = View.INVISIBLE
        binding.prescript6p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript6p3.text = getString(R.string.event1)
        binding.prescript6p3.setTextColor(Color.WHITE)
        binding.prescript7p.visibility = View.INVISIBLE
        binding.prescript7p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript7p.text = getString(R.string.event1)
        binding.prescript7p.setTextColor(Color.WHITE)
        binding.prescript7p2.visibility = View.INVISIBLE
        binding.prescript7p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript7p2.text = getString(R.string.event1)
        binding.prescript7p2.setTextColor(Color.WHITE)
        binding.prescript7p3.visibility = View.INVISIBLE
        binding.prescript7p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript7p3.text = getString(R.string.event1)
        binding.prescript7p3.setTextColor(Color.WHITE)
        binding.prescript8p.visibility = View.INVISIBLE
        binding.prescript8p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript8p.text = getString(R.string.event1)
        binding.prescript8p.setTextColor(Color.WHITE)
        binding.prescript8p2.visibility = View.INVISIBLE
        binding.prescript8p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript8p2.text = getString(R.string.event1)
        binding.prescript8p2.setTextColor(Color.WHITE)
        binding.prescript8p3.visibility = View.INVISIBLE
        binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript8p3.text = getString(R.string.event1)
        binding.prescript8p3.setTextColor(Color.WHITE)
        binding.prescript9p.visibility = View.INVISIBLE
        binding.prescript9p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript9p.text = getString(R.string.event1)
        binding.prescript9p.setTextColor(Color.WHITE)
        binding.prescript9p2.visibility = View.INVISIBLE
        binding.prescript9p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript9p2.text = getString(R.string.event1)
        binding.prescript9p2.setTextColor(Color.WHITE)
        binding.prescript9p3.visibility = View.INVISIBLE
        binding.prescript9p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript9p3.text = getString(R.string.event1)
        binding.prescript9p3.setTextColor(Color.WHITE)
        binding.prescript10p.visibility = View.INVISIBLE
        binding.prescript10p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript10p.text = getString(R.string.event1)
        binding.prescript10p.setTextColor(Color.WHITE)
        binding.prescript10p2.visibility = View.INVISIBLE
        binding.prescript10p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript10p2.text = getString(R.string.event1)
        binding.prescript10p2.setTextColor(Color.WHITE)
        binding.prescript10p3.visibility = View.INVISIBLE
        binding.prescript10p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript10p3.text = getString(R.string.event1)
        binding.prescript10p3.setTextColor(Color.WHITE)
        binding.prescript11p.visibility = View.INVISIBLE
        binding.prescript11p.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript11p.text = getString(R.string.event1)
        binding.prescript11p.setTextColor(Color.WHITE)
        binding.prescript11p2.visibility = View.INVISIBLE
        binding.prescript11p2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript11p2.text = getString(R.string.event1)
        binding.prescript11p2.setTextColor(Color.WHITE)
        binding.prescript11p3.visibility = View.INVISIBLE
        binding.prescript11p3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript11p3.text = getString(R.string.event1)
        binding.prescript11p3.setTextColor(Color.WHITE)
        binding.prescript12a.visibility = View.INVISIBLE
        binding.prescript12a.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript12a.text = getString(R.string.event1)
        binding.prescript12a.setTextColor(Color.WHITE)
        binding.prescript12a2.visibility = View.INVISIBLE
        binding.prescript12a2.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript12a2.text = getString(R.string.event1)
        binding.prescript12a2.setTextColor(Color.WHITE)
        binding.prescript12a3.visibility = View.INVISIBLE
        binding.prescript12a3.setBackgroundColor(resources.getColor(R.color.teal_200))
        binding.prescript12a3.text = getString(R.string.event1)
        binding.prescript12a3.setTextColor(Color.WHITE)
    }

    //Sets all of the Personal Events a User has on the Calendar
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun setPersonalEvents() {
        //Gets the userId from firebase authentication
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        //Gets the database instance for the users personal events
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/PersonalEvent")

        database.get().addOnSuccessListener {
            //Clear all events at the beginning
            clearAllEvents()
            //Get the count of events in the database, If there are not any events clear all events
            val events = it.childrenCount.toInt()
            if (events == 0) {
                clearAllEvents()
            } else {
                clearAllEvents()
                //Get the date from the selected date
                if (binding.sun.isSelected) {
                    date = binding.sun.text.toString().substringBefore("\n") + " " + binding.sun.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                } else if (binding.mon.isSelected) {
                    date = binding.mon.text.toString().substringBefore("\n") + " " + binding.mon.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                } else if (binding.tue.isSelected) {
                    date = binding.tue.text.toString().substringBefore("\n") + " " + binding.tue.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                } else if (binding.wed.isSelected) {
                    date = binding.wed.text.toString().substringBefore("\n") + " " + binding.wed.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                } else if (binding.thur.isSelected) {
                    date = binding.thur.text.toString().substringBefore("\n") + " " + binding.thur.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                } else if (binding.fri.isSelected) {
                    date = binding.fri.text.toString().substringBefore("\n") + " " + binding.fri.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                } else if (binding.sat.isSelected) {
                    date = binding.sat.text.toString().substringBefore("\n") + " " + binding.sat.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
                }

                val eventNames = it.children
                eventNames.forEach{
                    val d = it.child("date").value.toString()
                    //For each of the events in the personal event child in the database get the date
                    //If the date in the database is not the current date go to the next event
                    if (d != date) {
                        return@forEach
                    } else {
                        //If the selected date is the same as the date in the database get the name, start time, end time and description of the event
                        val eventN = it.child("name").value.toString()
                        val startTime = it.child("startTime").value.toString().substringBefore(" ")
                        val startTimeName = it.child("startTime").value.toString()
                        val endTime = it.child("endTime").value.toString().substringBefore(" ")
                        val endTimeName = it.child("endTime").value.toString()
                        //If the start time name is after 12pm then have sTime keep going up for future calculations
                        if (startTimeName == getString(R.string.p1)) {
                            sTime = 13
                        } else if (startTimeName == getString(R.string.p2)) {
                            sTime = 14
                        } else if (startTimeName == getString(R.string.p3)) {
                            sTime = 15
                        } else if (startTimeName == getString(R.string.p4)) {
                            sTime = 16
                        } else if (startTimeName == getString(R.string.p5)) {
                            sTime = 17
                        } else if (startTimeName == getString(R.string.p6)) {
                            sTime = 18
                        } else if (startTimeName == getString(R.string.p7)) {
                            sTime = 19
                        } else if (startTimeName == getString(R.string.p8)) {
                            sTime = 20
                        } else if (startTimeName == getString(R.string.p9)) {
                            sTime = 21
                        } else if (startTimeName == getString(R.string.p10)) {
                            sTime = 22
                        } else if (startTimeName == getString(R.string.p11)) {
                            sTime = 23
                        } else if (startTimeName == getString(R.string.a12)) {
                            sTime = 24
                        } else  {
                            sTime = startTime.toInt()
                        }

                        //If the end time is after 12pm then have eTime keep going up for future calculations
                        if (endTimeName == "1 pm") {
                            eTime = 13
                        } else if (endTimeName == "2 pm") {
                            eTime = 14
                        } else if (endTimeName == "3 pm") {
                            eTime = 15
                        } else if (endTimeName == "4 pm") {
                            eTime = 16
                        } else if (endTimeName == "5 pm") {
                            eTime = 17
                        } else if (endTimeName == "6 pm") {
                            eTime = 18
                        } else if (endTimeName == "7 pm") {
                            eTime = 19
                        } else if (endTimeName == "8 pm") {
                            eTime = 20
                        } else if (endTimeName == "9 pm") {
                            eTime = 21
                        } else if (endTimeName == "10 pm") {
                            eTime = 22
                        } else if (endTimeName == "11 pm") {
                            eTime = 23
                        } else if (endTimeName == "12 am") {
                            eTime = 24
                        } else  {
                            eTime = endTime.toInt()
                        }

                        //Calculate the total time of the event by subtracting the end time from the start time
                        totalTime = eTime-sTime

                        //Set the start buttons (all 3 for the three buttons at each of the time slots on the calendar)
                        if (getString(R.string.a1) == startTimeName) {
                            start = binding.prescript1a
                            start2 = binding.prescript1a2
                            start3 = binding.prescript1a3
                        } else if (getString(R.string.a2) == startTimeName) {
                            start = binding.prescript2a
                            start2 = binding.prescript2a2
                            start3 = binding.prescript2a3
                        } else if (getString(R.string.a3) == startTimeName) {
                            start = binding.prescript3a
                            start2 = binding.prescript3a2
                            start3 = binding.prescript3a3
                        } else if (getString(R.string.a4) == startTimeName) {
                            start = binding.prescript4a
                            start2 = binding.prescript4a2
                            start3 = binding.prescript4a3
                        } else if (getString(R.string.a5) == startTimeName) {
                            start = binding.prescript5a
                            start2 = binding.prescript5a2
                            start3 = binding.prescript5a3
                        } else if (getString(R.string.a6) == startTimeName) {
                            start = binding.prescript6a
                            start2 = binding.prescript6a2
                            start3 = binding.prescript6a3
                        } else if (getString(R.string.a7) == startTimeName) {
                            start = binding.prescript7a
                            start2 = binding.prescript7a2
                            start3 = binding.prescript7a3
                        } else if (getString(R.string.a8) == startTimeName) {
                            start = binding.prescript8a
                            start2 = binding.prescript8a2
                            start3 = binding.prescript8a3
                        } else if (getString(R.string.a9) == startTimeName) {
                            start = binding.prescript9a
                            start2 = binding.prescript9a2
                            start3 = binding.prescript9a3
                        } else if (getString(R.string.a10) == startTimeName) {
                            start = binding.prescript10a
                            start2 = binding.prescript10a2
                            start3 = binding.prescript10a3
                        } else if (getString(R.string.a11) == startTimeName) {
                            start = binding.prescript11a
                            start2 = binding.prescript11a2
                            start3 = binding.prescript11a3
                        } else if (getString(R.string.p12) == startTimeName) {
                            start = binding.prescript12p
                            start2 = binding.prescript12p2
                            start3 = binding.prescript12p3
                        } else if (getString(R.string.p1) == startTimeName) {
                            start = binding.prescript1p
                            start2 = binding.prescript1p2
                            start3 = binding.prescript1p3
                        } else if (getString(R.string.p2) == startTimeName) {
                            start = binding.prescript2p
                            start2 = binding.prescript2p2
                            start3 = binding.prescript2p3
                        } else if (getString(R.string.p3) == startTimeName) {
                            start = binding.prescript3p
                            start2 = binding.prescript3p2
                            start3 = binding.prescript3p3
                        } else if (getString(R.string.p4) == startTimeName) {
                            start = binding.prescript4p
                            start2 = binding.prescript4p2
                            start3 = binding.prescript4p3
                        } else if (getString(R.string.p5) == startTimeName) {
                            start = binding.prescript5p
                            start2 = binding.prescript5p2
                            start3 = binding.prescript5p3
                        } else if (getString(R.string.p6) == startTimeName) {
                            start = binding.prescript6p
                            start2 = binding.prescript6p2
                            start3 = binding.prescript6p3
                        } else if (getString(R.string.p7) == startTimeName) {
                            start = binding.prescript7p
                            start2 = binding.prescript7p2
                            start3 = binding.prescript7p3
                        } else if (getString(R.string.p8) == startTimeName) {
                            start = binding.prescript8p
                            start2 = binding.prescript8p2
                            start3 = binding.prescript8p3
                        } else if (getString(R.string.p9) == startTimeName) {
                            start = binding.prescript9p
                            start2 = binding.prescript9p2
                            start3 = binding.prescript9p3
                        } else if (getString(R.string.p10) == startTimeName) {
                            start = binding.prescript10p
                            start2 = binding.prescript10p2
                            start3 = binding.prescript10p3
                        } else if (getString(R.string.p11) == startTimeName) {
                            start = binding.prescript11p
                            start2 = binding.prescript11p2
                            start3 = binding.prescript11p3
                        } else if (getString(R.string.a12) == startTimeName) {
                            start = binding.prescript12a
                            start2 = binding.prescript12a2
                            start3 = binding.prescript12a3
                        }

                        //Set the end buttons (all 3 for the three buttons at each of the time slots on the calendar)
                        if (getString(R.string.a1) == endTimeName) {
                            end = binding.prescript1a
                            end2 = binding.prescript1a2
                            end3 = binding.prescript1a3
                        } else if (getString(R.string.a2) == endTimeName) {
                            end = binding.prescript2a
                            end2 = binding.prescript2a2
                            end3 = binding.prescript2a3
                        } else if (getString(R.string.a3) == startTimeName) {
                            end = binding.prescript3a
                            end2 = binding.prescript3a2
                            end3 = binding.prescript3a3
                        } else if (getString(R.string.a4) == endTimeName) {
                            end = binding.prescript4a
                            end2 = binding.prescript4a2
                            end3 = binding.prescript4a3
                        } else if (getString(R.string.a5) == endTimeName) {
                            end = binding.prescript5a
                            end2 = binding.prescript5a2
                            end3 = binding.prescript5a3
                        } else if (getString(R.string.a6) == endTimeName) {
                            end = binding.prescript6a
                            end2 = binding.prescript6a2
                            end3 = binding.prescript6a3
                        } else if (getString(R.string.a7) == endTimeName) {
                            end = binding.prescript7a
                            end2 = binding.prescript7a2
                            end3 = binding.prescript7a3
                        } else if (getString(R.string.a8) == endTimeName) {
                            end = binding.prescript8a
                            end2 = binding.prescript8a2
                            end3 = binding.prescript8a3
                        } else if (getString(R.string.a9) == endTimeName) {
                            end = binding.prescript9a
                            end2 = binding.prescript9a2
                            end3 = binding.prescript9a3
                        } else if (getString(R.string.a10) == endTimeName) {
                            end = binding.prescript10a
                            end2 = binding.prescript10a2
                            end3 = binding.prescript10a3
                        } else if (getString(R.string.a11) == endTimeName) {
                            end = binding.prescript11a
                            end2 = binding.prescript11a2
                            end3 = binding.prescript11a3
                        } else if (getString(R.string.p12) == endTimeName) {
                            end = binding.prescript12p
                            end2 = binding.prescript12p2
                            end3 = binding.prescript12p3
                        } else if (getString(R.string.p1) == endTimeName) {
                            end = binding.prescript1p
                            end2 = binding.prescript1p2
                            end3 = binding.prescript1p3
                        } else if (getString(R.string.p2) == endTimeName) {
                            end = binding.prescript2p
                            end2 = binding.prescript2p2
                            end3 = binding.prescript2p3
                        } else if (getString(R.string.p3) == endTimeName) {
                            end = binding.prescript3p
                            end2 = binding.prescript3p2
                            end3 = binding.prescript3p3
                        } else if (getString(R.string.p4) == endTimeName) {
                            end = binding.prescript4p
                            end2 = binding.prescript4p2
                            end3 = binding.prescript4p3
                        } else if (getString(R.string.p5) == endTimeName) {
                            end = binding.prescript5p
                            end2 = binding.prescript5p2
                            end3 = binding.prescript5p3
                        } else if (getString(R.string.p6) == endTimeName) {
                            end = binding.prescript6p
                            end2 = binding.prescript6p2
                            end3 = binding.prescript6p3
                        } else if (getString(R.string.p7) == endTimeName) {
                            end = binding.prescript7p
                            end2 = binding.prescript7p2
                            end3 = binding.prescript7p3
                        } else if (getString(R.string.p8) == endTimeName) {
                            end = binding.prescript8p
                            end2 = binding.prescript8p2
                            end3 = binding.prescript8p3
                        } else if (getString(R.string.p9) == endTimeName) {
                            end = binding.prescript9p
                            end2 = binding.prescript9p2
                            end3 = binding.prescript9p3
                        } else if (getString(R.string.p10) == endTimeName) {
                            end = binding.prescript10p
                            end2 = binding.prescript10p2
                            end3 = binding.prescript10p3
                        } else if (getString(R.string.p11) == endTimeName) {
                            end = binding.prescript11p
                            end2 = binding.prescript11p2
                            end3 = binding.prescript11p3
                        } else if (getString(R.string.a12) == endTimeName) {
                            end = binding.prescript12a
                            end2 = binding.prescript12a2
                            end3 = binding.prescript12a3

                        }

                        //Set button array for the first button at each time slot
                        var buttonArr: Array<Button> = arrayOf(binding.prescript1a,
                            binding.prescript2a,
                            binding.prescript3a,
                            binding.prescript4a,
                            binding.prescript5a,
                            binding.prescript6a,
                            binding.prescript7a,
                            binding.prescript8a,
                            binding.prescript9a,
                            binding.prescript10a,
                            binding.prescript11a,
                            binding.prescript12p,
                            binding.prescript1p,
                            binding.prescript2p,
                            binding.prescript3p,
                            binding.prescript4p,
                            binding.prescript5p,
                            binding.prescript6p,
                            binding.prescript7p,
                            binding.prescript8p,
                            binding.prescript9p,
                            binding.prescript10p,
                            binding.prescript11p,
                            binding.prescript12a)

                        //Set button array for the second button at each time slot
                        var buttonArr2: Array<Button> = arrayOf(binding.prescript1a2,
                            binding.prescript2a2,
                            binding.prescript3a2,
                            binding.prescript4a2,
                            binding.prescript5a2,
                            binding.prescript6a2,
                            binding.prescript7a2,
                            binding.prescript8a2,
                            binding.prescript9a2,
                            binding.prescript10a2,
                            binding.prescript11a2,
                            binding.prescript12p2,
                            binding.prescript1p2,
                            binding.prescript2p2,
                            binding.prescript3p2,
                            binding.prescript4p2,
                            binding.prescript5p2,
                            binding.prescript6p2,
                            binding.prescript7p2,
                            binding.prescript8p2,
                            binding.prescript9p2,
                            binding.prescript10p2,
                            binding.prescript11p2,
                            binding.prescript12a2)

                        //Set button array for the third button at each time slot
                        var buttonArr3: Array<Button> = arrayOf(binding.prescript1a3,
                            binding.prescript2a3,
                            binding.prescript3a3,
                            binding.prescript4a3,
                            binding.prescript5a3,
                            binding.prescript6a3,
                            binding.prescript7a3,
                            binding.prescript8a3,
                            binding.prescript9a3,
                            binding.prescript10a3,
                            binding.prescript11a3,
                            binding.prescript12p3,
                            binding.prescript1p3,
                            binding.prescript2p3,
                            binding.prescript3p3,
                            binding.prescript4p3,
                            binding.prescript5p3,
                            binding.prescript6p3,
                            binding.prescript7p3,
                            binding.prescript8p3,
                            binding.prescript9p3,
                            binding.prescript10p3,
                            binding.prescript11p3,
                            binding.prescript12a3)

                        //If the total time is 1 or 0 (0 being if the user selected the same time for end and start times set only the start button
                        if (totalTime == 1 || totalTime == 0) {
                            start.visibility = View.VISIBLE
                            //If the first button has the initial string in it set the text in the button to the event name, set alpha to 0.99F
                            //Otherwise make sure the text on that button is not the event name and move on to the second button at the same time slot
                            if (start.text == getString(R.string.event1)) {
                                start.text = eventN
                                start.alpha = 0.99F
                            } else if (start.text != eventN){
                                start2.visibility = View.VISIBLE
                                //If the second button has the initial string in it set the text in the button to the event name, set alpha to 0.99F
                                //Otherwise make sure the text on that button is not the event name and move on to the third button at the same time slot
                                if (start2.text == getString(R.string.event1)) {
                                    start2.text = eventN
                                    start2.alpha = 0.99F
                                } else if (start.text != eventN){
                                    start3.visibility = View.VISIBLE
                                    //If the third button has the initial string in it set the text in the button to the event name, set alpha to 0.99F no matter what
                                    //Otherwise make sure the text on that button is not the event name and set the text to "More"
                                    start3.alpha = 0.99F
                                    if (start3.text == getString(R.string.event1)) {
                                        start3.text = eventN
                                    } else if (start.text != eventN){
                                        start3.text = "More"
                                        start3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                    }
                                }
                            }
                        } else if (totalTime < 23 && totalTime != 1) {
                            //If the total time is less than 23 (not considered an "all day" event and the total time is not 1
                            //Get the button at the index of the start buttons for each of the three columns
                            val b = buttonArr.indexOf(start)
                            val b2 = buttonArr2.indexOf(start2)
                            val b3 = buttonArr3.indexOf(start3)
                            //If the first start button visibility is already visible and the text is not the initial text check the second start button
                            //Otherwise set the text to the event name and alpha to 0.99
                            if (start.visibility == View.VISIBLE && start.text != getString(R.string.event1)) {
                                //If the second start button visibility is already visible and the text is not the initial text check the third button
                                //Otherwise set the text to the event name and alpha to 0.99
                                if (start2.visibility == View.VISIBLE && start2.text != getString(R.string.event1)) {
                                    //If the third start button visibility is already visible and the text is not the initial text set the text of the
                                    //third button to "More", set the text color to white, and set the background to orange
                                    //Otherwise set the text to the event name and alpha to 0.99
                                    if (start3.visibility == View.VISIBLE && start3.text != getString(
                                            R.string.event1
                                        )
                                    ) {
                                        start3.text = "More"
                                        start3.setTextColor(Color.WHITE)
                                        start3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                        //For the number of buttons from start time to end time set the visibility to visible
                                        for (i in 1..totalTime) {
                                            buttonArr3[b3 + i].visibility = View.VISIBLE
                                            //If the button's text is the initial text, set the name to the event name, alpha to 0.99, and text color to clear
                                            //Otherwise set the text to "More", text color to white, and background to orange
                                            if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                buttonArr3[b3 + i].text = eventN
                                                buttonArr3[b3 + i].setTextColor(0)
                                                buttonArr3[b3 + i].alpha = 0.99F
                                            } else {
                                                buttonArr3[b3 + i].text = "More"
                                                buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                buttonArr3[b3 + i].setBackgroundColor(
                                                    resources.getColor(
                                                        R.color.yellowC
                                                    )
                                                )
                                            }
                                        }
                                    } else {
                                        start3.visibility = View.VISIBLE
                                        start3.text = eventN
                                        start3.alpha = 0.99F
                                        //For each button from the start button set the visibility to visible
                                        for (i in 1..totalTime) {
                                            buttonArr3[b3 + i].visibility = View.VISIBLE
                                            //If the text on the button is the initial text set the text to the event name, text color to clear, and alpha to 0.99
                                            //Otherwise set the text to "More", text color to white, and background to orange
                                            if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                buttonArr3[b3 + i].text = eventN
                                                buttonArr3[b3 + i].setTextColor(0)
                                                buttonArr3[b3 + i].alpha = 0.99F
                                            } else {
                                                buttonArr3[b3 + i].text = "More"
                                                buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                buttonArr3[b3 + i].setBackgroundColor(
                                                    resources.getColor(
                                                        R.color.yellowC
                                                    )
                                                )
                                            }
                                        }
                                    }
                                } else {
                                    start2.visibility = View.VISIBLE
                                    start2.text = eventN
                                    start2.alpha = 0.99F
                                    //For each button from start to end times set the visibility to visible
                                    for (i in 1..totalTime) {
                                        buttonArr2[b2 + i].visibility = View.VISIBLE
                                        //If the button text is the initial text set the text to the event name, text color to clear, and alpha to 0.99
                                        //Otherwise set the button to invisible and the text to the initial text
                                        if (buttonArr2[b2 + i].text == getString(R.string.event1)) {
                                            buttonArr2[b2 + i].text = eventN
                                            buttonArr2[b2 + i].setTextColor(0)
                                            buttonArr2[b2 + i].alpha = 0.99F
                                        } else {
                                            start2.visibility = View.INVISIBLE
                                            start2.text = getString(R.string.event1)
                                            //For each button from the beginning up to the button currently set to invisible and the text to the inital text then break out of the loop
                                            for (j in 1..i - 1) {
                                                buttonArr2[b2 + i - j].visibility = View.INVISIBLE
                                                buttonArr2[b2 + i - j].text =
                                                    getString(R.string.event1)
                                            }
                                            break
                                        }
                                    }
                                    //If the visibility of start 2 is invisible check the visibility of start 3
                                    if (start2.visibility == View.INVISIBLE) {
                                        //If the visibility of start 3 is visible and the text is not the initial text set the text to "More", text color to white, and background to orange
                                        //Otherwise set the text to the event name and alpha to 0.99
                                        if (start3.visibility == View.VISIBLE && start3.text != getString(
                                                R.string.event1
                                            )
                                        ) {
                                            start3.text = "More"
                                            start3.setTextColor(Color.WHITE)
                                            start3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            //For each button from the start set the visibility to visible
                                            for (i in 1..totalTime) {
                                                buttonArr3[b3 + i].visibility = View.VISIBLE
                                                //If the text is the initial text set the text to the event name, text color to clear, and alpha to 0.99
                                                //Otherwise set the text to "More", text color to white, and background to orange
                                                if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                    buttonArr3[b3 + i].text = eventN
                                                    buttonArr3[b3 + i].setTextColor(0)
                                                    buttonArr3[b3 + i].alpha = 0.99F
                                                } else {
                                                    buttonArr3[b3 + i].text = "More"
                                                    buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                    buttonArr3[b3 + i].setBackgroundColor(
                                                        resources.getColor(
                                                            R.color.yellowC
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            start3.visibility = View.VISIBLE
                                            start3.text = eventN
                                            start3.alpha = 0.99F
                                            //For each of the buttons from the start to the end time set the visibility to visible
                                            for (i in 1..totalTime) {
                                                buttonArr3[b3 + i].visibility = View.VISIBLE
                                                //If the text is the initial text set the text to the event name, set the text color to clear, and alpha to 0.99
                                                //Otherwise set the text to "More", text color to white, and background to orange
                                                if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                    buttonArr3[b3 + i].text = eventN
                                                    buttonArr3[b3 + i].setTextColor(0)
                                                    buttonArr3[b3 + i].alpha = 0.99F
                                                } else {
                                                    buttonArr3[b3 + i].text = "More"
                                                    buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                    buttonArr3[b3 + i].setBackgroundColor(
                                                        resources.getColor(
                                                            R.color.yellowC
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                start.visibility = View.VISIBLE
                                start.text = eventN
                                start.alpha = 0.99F
                                //For each of the times from start to end time set the visibility to visible on the buttons
                                for (i in 1..totalTime) {
                                    buttonArr[b + i].visibility = View.VISIBLE
                                    //If the text is the initial text set the text to the event name, text color to clear, and alpha to 0.99
                                    //Otherwise reset the visibility and text of the times from the current button back to the start button and break out of the loop
                                    if (buttonArr[b + i].text == getString(R.string.event1)) {
                                        buttonArr[b + i].text = eventN
                                        buttonArr[b + i].setTextColor(0)
                                        buttonArr[b + i].alpha = 0.99F
                                    } else {
                                        start.visibility = View.INVISIBLE
                                        start.text = getString(R.string.event1)
                                        for (j in 1..i - 1) {
                                            buttonArr[b + i - j].visibility = View.INVISIBLE
                                            buttonArr[b + i - j].text = getString(R.string.event1)
                                        }
                                        break
                                    }
                                }
                                //If the start 1 visibility is invisible check the visibility of start 2
                                if (start.visibility == View.INVISIBLE) {
                                    //If start 2 visibility is visible and the text is not the initial text check start 3
                                    //Otherwise set start 2 text to the event name and alpha to 0.99
                                    if (start2.visibility == View.VISIBLE && start2.text != getString(
                                            R.string.event1
                                        )
                                    ) {
                                        //If start 3 visibility is visible and text is not the inital text set the text to "More", text color to white, and background to orange
                                        //Otherwise set the text to the event name and alpha to 0.99
                                        if (start3.visibility == View.VISIBLE && start3.text != getString(
                                                R.string.event1
                                            )
                                        ) {
                                            start3.text = "More"
                                            start3.setTextColor(Color.WHITE)
                                            start3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            //For each time from start to end set the visibility to visible
                                            for (i in 1..totalTime) {
                                                buttonArr3[b3 + i].visibility = View.VISIBLE
                                                //If the text on the button is the initial text change to the event name, text color to clear and alpha to 0.99
                                                //Otherwise set to "More", text color to white, and background to orange
                                                if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                    buttonArr3[b3 + i].text = eventN
                                                    buttonArr3[b3 + i].setTextColor(0)
                                                    buttonArr3[b3 + i].alpha = 0.99F
                                                } else {
                                                    buttonArr3[b3 + i].text = "More"
                                                    buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                    buttonArr3[b3 + i].setBackgroundColor(
                                                        resources.getColor(
                                                            R.color.yellowC
                                                        )
                                                    )
                                                }
                                            }
                                        } else {
                                            start3.visibility = View.VISIBLE
                                            start3.text = eventN
                                            start3.alpha = 0.99F
                                            //For each time from start to end set the visibility to visible
                                            for (i in 1..totalTime) {
                                                buttonArr3[b3 + i].visibility = View.VISIBLE
                                                //If the text is the inital text set text to event name, text color to clear, and alpha to 0.99
                                                //Otherwise set text to "More", text color to white, and background to orange
                                                if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                    buttonArr3[b3 + i].text = eventN
                                                    buttonArr3[b3 + i].setTextColor(0)
                                                    buttonArr3[b3 + i].alpha = 0.99F
                                                } else {
                                                    buttonArr3[b3 + i].text = "More"
                                                    buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                    buttonArr3[b3 + i].setBackgroundColor(
                                                        resources.getColor(
                                                            R.color.yellowC
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    } else {
                                        start2.visibility = View.VISIBLE
                                        start2.text = eventN
                                        start2.alpha = 0.99F
                                        //For each time from start to end set the button to visible
                                        for (i in 1..totalTime) {
                                            buttonArr2[b2 + i].visibility = View.VISIBLE
                                            //If text is the initial text set the text to the event name, text color to clear, and alpha to 0.99
                                            //Otherwise reset the button states from current button to start and break out of the loop
                                            if (buttonArr2[b2 + i].text == getString(R.string.event1)) {
                                                buttonArr2[b2 + i].text = eventN
                                                buttonArr2[b2 + i].setTextColor(0)
                                                buttonArr2[b2 + i].alpha = 0.99F
                                            } else {
                                                start2.visibility = View.INVISIBLE
                                                start2.text = getString(R.string.event1)
                                                for (j in 1..i - 1) {
                                                    buttonArr2[b2 + i - j].visibility =
                                                        View.INVISIBLE
                                                    buttonArr2[b2 + i - j].text =
                                                        getString(R.string.event1)
                                                }
                                                break
                                            }
                                        }
                                        //If start 2 visibility is invisible check start 3
                                        if (start2.visibility == View.INVISIBLE) {
                                            //If start 3 is already visible and does not have the initial text set the text to "More, text color to white, and background to orange
                                            //Otherwise set text to the event name and alpha to 0.99
                                            if (start3.visibility == View.VISIBLE && start3.text != getString(
                                                    R.string.event1
                                                )
                                            ) {
                                                start3.text = "More"
                                                start3.setTextColor(Color.WHITE)
                                                start3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                                //For all times between start and end times set the visibility of buttons to visible
                                                for (i in 1..totalTime) {
                                                    buttonArr3[b3 + i].visibility = View.VISIBLE
                                                    //If text is initial text, set to event name, text color to clear, and alpha to 0.99
                                                    //Otherwise set text to "More", text color to white, and background to orange
                                                    if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                        buttonArr3[b3 + i].text = eventN
                                                        buttonArr3[b3 + i].setTextColor(0)
                                                        buttonArr3[b3 + i].alpha = 0.99F
                                                    } else {
                                                        buttonArr3[b3 + i].text = "More"
                                                        buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                        buttonArr3[b3 + i].setBackgroundColor(
                                                            resources.getColor(R.color.yellowC)
                                                        )
                                                    }
                                                }
                                            } else {
                                                start3.visibility = View.VISIBLE
                                                start3.text = eventN
                                                start3.alpha = 0.99F
                                                //For each time from start to end set visibility to visible
                                                for (i in 1..totalTime) {
                                                    buttonArr3[b3 + i].visibility = View.VISIBLE
                                                    //If text is initial text set to event name, text color to clear, and alpha to 0.99
                                                    //Otherwise set text to "More", text color to white, and background to orange
                                                    if (buttonArr3[b3 + i].text == getString(R.string.event1)) {
                                                        buttonArr3[b3 + i].text = eventN
                                                        buttonArr3[b3 + i].setTextColor(0)
                                                        buttonArr3[b3 + i].alpha = 0.99F
                                                    } else {
                                                        buttonArr3[b3 + i].text = "More"
                                                        buttonArr3[b3 + i].setTextColor(Color.WHITE)
                                                        buttonArr3[b3 + i].setBackgroundColor(
                                                            resources.getColor(R.color.yellowC)
                                                        )
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            //This is for events that are all day (1am to 12am next day)
                            //Set first all day button to visible
                            binding.all1.visibility = View.VISIBLE
                            //If text for first all day button is just %s and the selected date equals the date for the event
                            //Set the text to the event name and alpha to 0.99
                            if (binding.all1.text == "%s" && d == date) {
                                binding.all1.text = getString(R.string.eventA, eventN)
                                binding.all1.alpha = 0.99F
                            } else if (binding.all1.text != getString(R.string.eventA, eventN) && d == date) {
                                //Set second all day button to visible
                                binding.all2.visibility = View.VISIBLE
                                //If text is %s and date = selected date set text to event name and alpha to 0.99
                                if (binding.all2.text == "%s" && d == date) {
                                    binding.all2.text = getString(R.string.eventA, eventN)
                                    binding.all2.alpha = 0.99F
                                } else if (binding.all2.text != getString(R.string.eventA, eventN) && d == date){
                                    //Set third all day button to visible and alpha to 0.99
                                    binding.all3.visibility = View.VISIBLE
                                    binding.all3.alpha = 0.99F
                                    //If text is %s and date = selected day set text to event name
                                    //Otherwise set text to "More" and background to orange
                                    if (binding.all3.text == "%s" && d == date) {
                                        binding.all3.text = getString(R.string.eventA, eventN)
                                    } else if (binding.all3.text != getString(R.string.eventA, eventN) && d == date){
                                        binding.all3.text = "More"
                                        binding.all3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                    }
                                }
                            }
                       }
                    }
                }
            }
        }.addOnFailureListener {
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
        //Set prescription events
        setEventStates()
    }

    //Sets all of the medication events on the calendar
    fun setEventStates() {
        //Set an array of colors for events
        var colorArr: Array<Int> = arrayOf(R.color.tealC,
            R.color.purpleC,
            R.color.redC,
            R.color.greenC,
            R.color.magentaC,
            R.color.orangeC,
            R.color.yellowC)
        //Get uId for user from firebase authentication
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        //Get database instance for child users
        database = FirebaseDatabase.getInstance().getReference("users/" + userId + "/ChildUsers")

        database.get().addOnSuccessListener {
            //Get the number of child users (there will always be one)
            val num = it.childrenCount.toInt()
            val n = it.children

            //Get the primary child's (user who is logged in) medications
            val p = it.child("Primary").child("Medications").children
            p.forEach { pe ->
                //Get the frequency and name of each medication under the primary user
                val medFreq = pe.child("frequency").value.toString()
                val medName = pe.child("name").value.toString()
                //If the frequency is 1 only set one of the 3 8am buttons
                //Otherwise if frequency is 2 set one 8am button and one 8pm button
                //Otherwise if frequency is 3 set one 8am button, one 2pm button, and one 8pm button
                //Otherwise if frequency is 4 set one 8am button, one 12pm button, one 4pm button, and one 8pm button
                if (medFreq == "1") {
                    binding.prescript8a.visibility = View.VISIBLE
                    if (binding.prescript8a.text == getString(R.string.event1)) {
                        binding.prescript8a.setTextColor(Color.WHITE)
                        binding.prescript8a.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8a.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8a2.visibility = View.VISIBLE
                        if (binding.prescript8a2.text == getString(R.string.event1)) {
                            binding.prescript8a2.setTextColor(Color.WHITE)
                            binding.prescript8a2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8a2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8a3.visibility = View.VISIBLE
                            if (binding.prescript8a3.text == getString(R.string.event1)) {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8a3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8a3.text = "More"
                            }
                        }
                    }
                } else if (medFreq == "2") {
                    binding.prescript8a.visibility = View.VISIBLE
                    binding.prescript8p.visibility = View.VISIBLE
                    if (binding.prescript8a.text == getString(R.string.event1)) {
                        binding.prescript8a.setTextColor(Color.WHITE)
                        binding.prescript8a.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8a.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8a2.visibility = View.VISIBLE
                        if (binding.prescript8a2.text == getString(R.string.event1)) {
                            binding.prescript8a2.setTextColor(Color.WHITE)
                            binding.prescript8a2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8a2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8a3.visibility = View.VISIBLE
                            if (binding.prescript8a3.text == getString(R.string.event1)) {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8a3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8a3.text = "More"
                            }
                        }
                    }
                    if (binding.prescript8p.text == getString(R.string.event1)) {
                        binding.prescript8p.setTextColor(Color.WHITE)
                        binding.prescript8p.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8p.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8p2.visibility = View.VISIBLE
                        if (binding.prescript8p2.text == getString(R.string.event1)) {
                            binding.prescript8p2.setTextColor(Color.WHITE)
                            binding.prescript8p2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8p2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8p3.visibility = View.VISIBLE
                            if (binding.prescript8p3.text == getString(R.string.event1)) {
                                binding.prescript8p3.setTextColor(Color.WHITE)
                                binding.prescript8p3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8p3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8p3.setTextColor(Color.WHITE)
                                binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8p3.text = "More"
                            }
                        }
                    }
                } else if (medFreq == "3") {
                    binding.prescript8a.visibility = View.VISIBLE
                    binding.prescript2p.visibility = View.VISIBLE
                    binding.prescript8p.visibility = View.VISIBLE
                    if (binding.prescript8a.text == getString(R.string.event1)) {
                        binding.prescript8a.setTextColor(Color.WHITE)
                        binding.prescript8a.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8a.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8a2.visibility = View.VISIBLE
                        if (binding.prescript8a2.text == getString(R.string.event1)) {
                            binding.prescript8a2.setTextColor(Color.WHITE)
                            binding.prescript8a2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8a2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8a3.visibility = View.VISIBLE
                            if (binding.prescript8a3.text == getString(R.string.event1)) {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8a3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8a3.text = "More"
                            }
                        }
                    }
                    if (binding.prescript2p.text == getString(R.string.event1)) {
                        binding.prescript2p.setTextColor(Color.WHITE)
                        binding.prescript2p.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript2p.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript2p2.visibility = View.VISIBLE
                        if (binding.prescript2p2.text == getString(R.string.event1)) {
                            binding.prescript2p2.setTextColor(Color.WHITE)
                            binding.prescript2p2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript2p2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript2p3.visibility = View.VISIBLE
                            if (binding.prescript2p3.text == getString(R.string.event1)) {
                                binding.prescript2p3.setTextColor(Color.WHITE)
                                binding.prescript2p3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript2p3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript2p3.setTextColor(Color.WHITE)
                                binding.prescript2p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript2p3.text = "More"
                            }
                        }
                    }
                    if (binding.prescript8p.text == getString(R.string.event1)) {
                        binding.prescript8p.setTextColor(Color.WHITE)
                        binding.prescript8p.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8p.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8p2.visibility = View.VISIBLE
                        if (binding.prescript8p2.text == getString(R.string.event1)) {
                            binding.prescript8p2.setTextColor(Color.WHITE)
                            binding.prescript8p2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8p2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8p3.visibility = View.VISIBLE
                            if (binding.prescript8p3.text == getString(R.string.event1)) {
                                binding.prescript8p3.setTextColor(Color.WHITE)
                                binding.prescript8p3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8p3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8p3.setTextColor(Color.WHITE)
                                binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8p3.text = "More"
                            }
                        }
                    }
                } else {
                    binding.prescript8a.visibility = View.VISIBLE
                    binding.prescript12p.visibility = View.VISIBLE
                    binding.prescript4p.visibility = View.VISIBLE
                    binding.prescript8p.visibility = View.VISIBLE
                    if (binding.prescript8a.text == getString(R.string.event1)) {
                        binding.prescript8a.setTextColor(Color.WHITE)
                        binding.prescript8a.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8a.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8a2.visibility = View.VISIBLE
                        if (binding.prescript8a2.text == getString(R.string.event1)) {
                            binding.prescript8a2.setTextColor(Color.WHITE)
                            binding.prescript8a2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8a2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8a3.visibility = View.VISIBLE
                            if (binding.prescript8a3.text == getString(R.string.event1)) {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8a3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8a3.setTextColor(Color.WHITE)
                                binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8a3.text = "More"
                            }
                        }
                    }
                    if (binding.prescript12p.text == getString(R.string.event1)) {
                        binding.prescript12p.setTextColor(Color.WHITE)
                        binding.prescript12p.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript12p.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript12p2.visibility = View.VISIBLE
                        if (binding.prescript12p2.text == getString(R.string.event1)) {
                            binding.prescript12p2.setTextColor(Color.WHITE)
                            binding.prescript12p2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript12p2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript12p3.visibility = View.VISIBLE
                            if (binding.prescript12p3.text == getString(R.string.event1)) {
                                binding.prescript12p3.setTextColor(Color.WHITE)
                                binding.prescript12p3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript12p3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript12p3.setTextColor(Color.WHITE)
                                binding.prescript12p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript12p3.text = "More"
                            }
                        }
                    }
                    if (binding.prescript4p.text == getString(R.string.event1)) {
                        binding.prescript4p.setTextColor(Color.WHITE)
                        binding.prescript4p.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript4p.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript4p2.visibility = View.VISIBLE
                        if (binding.prescript4p2.text == getString(R.string.event1)) {
                            binding.prescript4p2.setTextColor(Color.WHITE)
                            binding.prescript4p2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript4p2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript4p3.visibility = View.VISIBLE
                            if (binding.prescript4p3.text == getString(R.string.event1)) {
                                binding.prescript4p3.setTextColor(Color.WHITE)
                                binding.prescript4p3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript4p3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript4p3.setTextColor(Color.WHITE)
                                binding.prescript4p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript4p3.text = "More"
                            }
                        }
                    }
                    if (binding.prescript8p.text == getString(R.string.event1)) {
                        binding.prescript8p.setTextColor(Color.WHITE)
                        binding.prescript8p.setBackgroundColor(resources.getColor(colorArr[0]))
                        binding.prescript8p.text = getString(R.string.event1, medName, "Your RX")
                    } else {
                        binding.prescript8p2.visibility = View.VISIBLE
                        if (binding.prescript8p2.text == getString(R.string.event1)) {
                            binding.prescript8p2.setTextColor(Color.WHITE)
                            binding.prescript8p2.setBackgroundColor(resources.getColor(colorArr[0]))
                            binding.prescript8p2.text = getString(R.string.event2, medName, "Your RX")
                        } else {
                            binding.prescript8p3.visibility = View.VISIBLE
                            if (binding.prescript8p3.text == getString(R.string.event1)) {
                                binding.prescript8p3.setTextColor(Color.WHITE)
                                binding.prescript8p3.setBackgroundColor(resources.getColor(colorArr[0]))
                                binding.prescript8p3.text =
                                    getString(R.string.event3, medName, "Your RX")
                            } else {
                                binding.prescript8p3.setTextColor(Color.WHITE)
                                binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                binding.prescript8p3.text = "More"
                            }
                        }
                    }
                }
            }
            if (num != 1) {
                n.forEachIndexed { index, na ->
                    val color = colorArr[index + 1]
                    val name = na.child("name").value.toString()
                    val meds = na.child("Medications").children
                    meds.forEach re@{ me ->
                        if (name == "Your") {
                            return@re
                        } else {
                            val medFreq = me.child("frequency").value.toString()
                            val medName = me.child("name").value.toString()
                            if (medFreq == "1") {
                                binding.prescript8a.visibility = View.VISIBLE
                                if (binding.prescript8a.text == getString(R.string.event1)) {
                                    binding.prescript8a.setTextColor(Color.WHITE)
                                    binding.prescript8a.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8a.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8a2.visibility = View.VISIBLE
                                    if (binding.prescript8a2.text == getString(R.string.event1)) {
                                        binding.prescript8a2.setTextColor(Color.WHITE)
                                        binding.prescript8a2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8a2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8a3.visibility = View.VISIBLE
                                        if (binding.prescript8a3.text == getString(R.string.event1)) {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8a3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8a3.text = "More"
                                        }
                                    }
                                }
                            } else if (medFreq == "2") {
                                binding.prescript8a.visibility = View.VISIBLE
                                binding.prescript8p.visibility = View.VISIBLE
                                if (binding.prescript8a.text == getString(R.string.event1)) {
                                    binding.prescript8a.setTextColor(Color.WHITE)
                                    binding.prescript8a.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8a.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8a2.visibility = View.VISIBLE
                                    if (binding.prescript8a2.text == getString(R.string.event1)) {
                                        binding.prescript8a2.setTextColor(Color.WHITE)
                                        binding.prescript8a2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8a2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8a3.visibility = View.VISIBLE
                                        if (binding.prescript8a3.text == getString(R.string.event1)) {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8a3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8a3.text = "More"
                                        }
                                    }
                                }
                                if (binding.prescript8p.text == getString(R.string.event1)) {
                                    binding.prescript8p.setTextColor(Color.WHITE)
                                    binding.prescript8p.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8p.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8p2.visibility = View.VISIBLE
                                    if (binding.prescript8p2.text == getString(R.string.event1)) {
                                        binding.prescript8p2.setTextColor(Color.WHITE)
                                        binding.prescript8p2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8p2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8p3.visibility = View.VISIBLE
                                        if (binding.prescript8p3.text == getString(R.string.event1)) {
                                            binding.prescript8p3.setTextColor(Color.WHITE)
                                            binding.prescript8p3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8p3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8p3.setTextColor(Color.WHITE)
                                            binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8p3.text = "More"
                                        }
                                    }
                                }
                            } else if (medFreq == "3") {
                                binding.prescript8a.visibility = View.VISIBLE
                                binding.prescript2p.visibility = View.VISIBLE
                                binding.prescript8p.visibility = View.VISIBLE
                                if (binding.prescript8a.text == getString(R.string.event1)) {
                                    binding.prescript8a.setTextColor(Color.WHITE)
                                    binding.prescript8a.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8a.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8a2.visibility = View.VISIBLE
                                    if (binding.prescript8a2.text == getString(R.string.event1)) {
                                        binding.prescript8a2.setTextColor(Color.WHITE)
                                        binding.prescript8a2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8a2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8a3.visibility = View.VISIBLE
                                        if (binding.prescript8a3.text == getString(R.string.event1)) {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8a3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8a3.text = "More"
                                        }
                                    }
                                }
                                if (binding.prescript2p.text == getString(R.string.event1)) {
                                    binding.prescript2p.setTextColor(Color.WHITE)
                                    binding.prescript2p.setBackgroundColor(resources.getColor(color))
                                    binding.prescript2p.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript2p2.visibility = View.VISIBLE
                                    if (binding.prescript2p2.text == getString(R.string.event1)) {
                                        binding.prescript2p2.setTextColor(Color.WHITE)
                                        binding.prescript2p2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript2p2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript2p3.visibility = View.VISIBLE
                                        if (binding.prescript2p3.text == getString(R.string.event1)) {
                                            binding.prescript2p3.setTextColor(Color.WHITE)
                                            binding.prescript2p3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript2p3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript2p3.setTextColor(Color.WHITE)
                                            binding.prescript2p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript2p3.text = "More"
                                        }
                                    }
                                }
                                if (binding.prescript8p.text == getString(R.string.event1)) {
                                    binding.prescript8p.setTextColor(Color.WHITE)
                                    binding.prescript8p.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8p.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8p2.visibility = View.VISIBLE
                                    if (binding.prescript8p2.text == getString(R.string.event1)) {
                                        binding.prescript8p2.setTextColor(Color.WHITE)
                                        binding.prescript8p2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8p2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8p3.visibility = View.VISIBLE
                                        if (binding.prescript8p3.text == getString(R.string.event1)) {
                                            binding.prescript8p3.setTextColor(Color.WHITE)
                                            binding.prescript8p3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8p3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8p3.setTextColor(Color.WHITE)
                                            binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8p3.text = "More"
                                        }
                                    }
                                }
                            } else {
                                binding.prescript8a.visibility = View.VISIBLE
                                binding.prescript12p.visibility = View.VISIBLE
                                binding.prescript4p.visibility = View.VISIBLE
                                binding.prescript8p.visibility = View.VISIBLE
                                if (binding.prescript8a.text == getString(R.string.event1)) {
                                    binding.prescript8a.setTextColor(Color.WHITE)
                                    binding.prescript8a.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8a.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8a2.visibility = View.VISIBLE
                                    if (binding.prescript8a2.text == getString(R.string.event1)) {
                                        binding.prescript8a2.setTextColor(Color.WHITE)
                                        binding.prescript8a2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8a2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8a3.visibility = View.VISIBLE
                                        if (binding.prescript8a3.text == getString(R.string.event1)) {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8a3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8a3.setTextColor(Color.WHITE)
                                            binding.prescript8a3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8a3.text = "More"
                                        }
                                    }
                                }
                                if (binding.prescript12p.text == getString(R.string.event1)) {
                                    binding.prescript12p.setTextColor(Color.WHITE)
                                    binding.prescript12p.setBackgroundColor(resources.getColor(color))
                                    binding.prescript12p.text =
                                        getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript12p2.visibility = View.VISIBLE
                                    if (binding.prescript12p2.text == getString(R.string.event1)) {
                                        binding.prescript12p2.setTextColor(Color.WHITE)
                                        binding.prescript12p2.setBackgroundColor(
                                            resources.getColor(
                                                color
                                            )
                                        )
                                        binding.prescript12p2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript12p3.visibility = View.VISIBLE
                                        if (binding.prescript12p3.text == getString(R.string.event1)) {
                                            binding.prescript12p3.setTextColor(Color.WHITE)
                                            binding.prescript12p3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript12p3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript12p3.setTextColor(Color.WHITE)
                                            binding.prescript12p3.setBackgroundColor(
                                                resources.getColor(
                                                    R.color.yellowC
                                                )
                                            )
                                            binding.prescript12p3.text = "More"
                                        }
                                    }
                                }
                                if (binding.prescript4p.text == getString(R.string.event1)) {
                                    binding.prescript4p.setTextColor(Color.WHITE)
                                    binding.prescript4p.setBackgroundColor(resources.getColor(color))
                                    binding.prescript4p.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript4p2.visibility = View.VISIBLE
                                    if (binding.prescript4p2.text == getString(R.string.event1)) {
                                        binding.prescript4p2.setTextColor(Color.WHITE)
                                        binding.prescript4p2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript4p2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript4p3.visibility = View.VISIBLE
                                        if (binding.prescript4p3.text == getString(R.string.event1)) {
                                            binding.prescript4p3.setTextColor(Color.WHITE)
                                            binding.prescript4p3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript4p3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript4p3.setTextColor(Color.WHITE)
                                            binding.prescript4p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript4p3.text = "More"
                                        }
                                    }
                                }
                                if (binding.prescript8p.text == getString(R.string.event1)) {
                                    binding.prescript8p.setTextColor(Color.WHITE)
                                    binding.prescript8p.setBackgroundColor(resources.getColor(color))
                                    binding.prescript8p.text = getString(R.string.event1, medName, name+" RX")
                                } else {
                                    binding.prescript8p2.visibility = View.VISIBLE
                                    if (binding.prescript8p2.text == getString(R.string.event1)) {
                                        binding.prescript8p2.setTextColor(Color.WHITE)
                                        binding.prescript8p2.setBackgroundColor(resources.getColor(color))
                                        binding.prescript8p2.text =
                                            getString(R.string.event2, medName, name+" RX")
                                    } else {
                                        binding.prescript8p3.visibility = View.VISIBLE
                                        if (binding.prescript8p3.text == getString(R.string.event1)) {
                                            binding.prescript8p3.setTextColor(Color.WHITE)
                                            binding.prescript8p3.setBackgroundColor(
                                                resources.getColor(
                                                    color
                                                )
                                            )
                                            binding.prescript8p3.text =
                                                getString(R.string.event3, medName, name+" RX")
                                        } else {
                                            binding.prescript8p3.setTextColor(Color.WHITE)
                                            binding.prescript8p3.setBackgroundColor(resources.getColor(R.color.yellowC))
                                            binding.prescript8p3.text = "More"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    //Gets the current date and calls the navigation function on the view clicked on
    fun addEventClicked(){
        //Get the date from the selected date
        if (binding.sun.isSelected) {
            date = binding.sun.text.toString().substringBefore("\n") + " " + binding.sun.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.mon.isSelected) {
            date = binding.mon.text.toString().substringBefore("\n") + " " + binding.mon.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.tue.isSelected) {
            date = binding.tue.text.toString().substringBefore("\n") + " " + binding.tue.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.wed.isSelected) {
            date = binding.wed.text.toString().substringBefore("\n") + " " + binding.wed.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.thur.isSelected) {
            date = binding.thur.text.toString().substringBefore("\n") + " " + binding.thur.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.fri.isSelected) {
            date = binding.fri.text.toString().substringBefore("\n") + " " + binding.fri.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.sat.isSelected) {
            date = binding.sat.text.toString().substringBefore("\n") + " " + binding.sat.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        }
        //Set the action of navigation to personal event fragment, passing in date
        val action = CalendarFragmentDirections.actionNavCalendarFragmentToPersonalEventFragment(date = date!!)
        //Navigate to action
        binding.addEvent.findNavController().navigate(action)
    }

    //Gets the current date and calls the navigation function on the event clicked to go to either medication details, personal event details or all details
    fun eventClicked(medName: String, button: Button){
        //Get the date from the selected date
        if (binding.sun.isSelected) {
            date = binding.sun.text.toString().substringBefore("\n") + " " + binding.sun.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.mon.isSelected) {
            date = binding.mon.text.toString().substringBefore("\n") + " " + binding.mon.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.tue.isSelected) {
            date = binding.tue.text.toString().substringBefore("\n") + " " + binding.tue.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.wed.isSelected) {
            date = binding.wed.text.toString().substringBefore("\n") + " " + binding.wed.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.thur.isSelected) {
            date = binding.thur.text.toString().substringBefore("\n") + " " + binding.thur.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.fri.isSelected) {
            date = binding.fri.text.toString().substringBefore("\n") + " " + binding.fri.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        } else if (binding.sat.isSelected) {
            date = binding.sat.text.toString().substringBefore("\n") + " " + binding.sat.text.toString().substringAfter("\n") + " " + binding.monthYear.text.toString()
        }

        //Get the alpha value
        val alph = button.alpha

        //If the beginning of the passed string is take and the alpha is 1.0 create the action going to the event fragment
        //Otherwise if the passed string is "More" set the action to go to event fragment
        //Otherwise set the action to go to personal event detail fragment
        if (medName.substringBefore(" ") == "Take" && alph == 1.0F) {
            val action =
                CalendarFragmentDirections.actionNavCalendarFragmentToEventFragment3(prescripName = medName, date)
            //Check which view was selected and then navigate
            for (i in 0..2) {
                if (i == 0) {
                    if (view === binding.prescript1a) {
                        binding.prescript1a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a) {
                        binding.prescript2a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a) {
                        binding.prescript3a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a) {
                        binding.prescript4a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a) {
                        binding.prescript5a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a) {
                        binding.prescript6a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a) {
                        binding.prescript7a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a) {
                        binding.prescript8a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a) {
                        binding.prescript9a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a) {
                        binding.prescript10a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a) {
                        binding.prescript11a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p) {
                        binding.prescript12p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p) {
                        binding.prescript1p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p) {
                        binding.prescript2p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p) {
                        binding.prescript3p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p) {
                        binding.prescript4p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p) {
                        binding.prescript5p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p) {
                        binding.prescript6p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p) {
                        binding.prescript7p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p) {
                        binding.prescript8p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p) {
                        binding.prescript9p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p) {
                        binding.prescript10p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p) {
                        binding.prescript11p.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a.findNavController().navigate(action)
                        break
                    }
                } else if (i == 1) {
                    if (view === binding.prescript1a2) {
                        binding.prescript1a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a2) {
                        binding.prescript2a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a2) {
                        binding.prescript3a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a2) {
                        binding.prescript4a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a2) {
                        binding.prescript5a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a2) {
                        binding.prescript6a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a2) {
                        binding.prescript7a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a2) {
                        binding.prescript8a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a2) {
                        binding.prescript9a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a2) {
                        binding.prescript10a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a2) {
                        binding.prescript11a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p2) {
                        binding.prescript12p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p2) {
                        binding.prescript1p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p2) {
                        binding.prescript2p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p2) {
                        binding.prescript3p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p2) {
                        binding.prescript4p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p2) {
                        binding.prescript5p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p2) {
                        binding.prescript6p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p2) {
                        binding.prescript7p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p2) {
                        binding.prescript8p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p2) {
                        binding.prescript9p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p2) {
                        binding.prescript10p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p2) {
                        binding.prescript11p2.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a2.findNavController().navigate(action)
                        break
                    }
                } else {
                    if (view === binding.prescript1a3) {
                        binding.prescript1a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a3) {
                        binding.prescript2a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a3) {
                        binding.prescript3a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a3) {
                        binding.prescript4a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a3) {
                        binding.prescript5a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a3) {
                        binding.prescript6a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a3) {
                        binding.prescript7a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a3) {
                        binding.prescript8a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a3) {
                        binding.prescript9a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a3) {
                        binding.prescript10a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a3) {
                        binding.prescript11a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p3) {
                        binding.prescript12p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p3) {
                        binding.prescript1p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p3) {
                        binding.prescript2p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p3) {
                        binding.prescript3p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p3) {
                        binding.prescript4p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p3) {
                        binding.prescript5p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p3) {
                        binding.prescript6p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p3) {
                        binding.prescript7p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p3) {
                        binding.prescript8p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p3) {
                        binding.prescript9p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p3) {
                        binding.prescript10p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p3) {
                        binding.prescript11p3.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a3.findNavController().navigate(action)
                        break
                    }
                }
            }
        } else if (medName == "More") {
            val action =
                CalendarFragmentDirections.actionNavCalendarFragmentToEventFragment3(prescripName = medName, date)
            //Check which view was selected and then navigate
            for (i in 0..2) {
                if (i == 0) {
                    if (view === binding.prescript1a) {
                        binding.prescript1a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a) {
                        binding.prescript2a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a) {
                        binding.prescript3a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a) {
                        binding.prescript4a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a) {
                        binding.prescript5a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a) {
                        binding.prescript6a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a) {
                        binding.prescript7a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a) {
                        binding.prescript8a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a) {
                        binding.prescript9a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a) {
                        binding.prescript10a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a) {
                        binding.prescript11a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p) {
                        binding.prescript12p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p) {
                        binding.prescript1p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p) {
                        binding.prescript2p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p) {
                        binding.prescript3p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p) {
                        binding.prescript4p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p) {
                        binding.prescript5p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p) {
                        binding.prescript6p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p) {
                        binding.prescript7p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p) {
                        binding.prescript8p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p) {
                        binding.prescript9p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p) {
                        binding.prescript10p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p) {
                        binding.prescript11p.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a.findNavController().navigate(action)
                        break
                    }
                } else if (i == 1) {
                    if (view === binding.prescript1a2) {
                        binding.prescript1a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a2) {
                        binding.prescript2a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a2) {
                        binding.prescript3a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a2) {
                        binding.prescript4a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a2) {
                        binding.prescript5a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a2) {
                        binding.prescript6a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a2) {
                        binding.prescript7a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a2) {
                        binding.prescript8a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a2) {
                        binding.prescript9a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a2) {
                        binding.prescript10a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a2) {
                        binding.prescript11a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p2) {
                        binding.prescript12p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p2) {
                        binding.prescript1p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p2) {
                        binding.prescript2p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p2) {
                        binding.prescript3p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p2) {
                        binding.prescript4p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p2) {
                        binding.prescript5p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p2) {
                        binding.prescript6p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p2) {
                        binding.prescript7p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p2) {
                        binding.prescript8p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p2) {
                        binding.prescript9p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p2) {
                        binding.prescript10p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p2) {
                        binding.prescript11p2.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a2.findNavController().navigate(action)
                        break
                    }
                } else {
                    if (view === binding.prescript1a3) {
                        binding.prescript1a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a3) {
                        binding.prescript2a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a3) {
                        binding.prescript3a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a3) {
                        binding.prescript4a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a3) {
                        binding.prescript5a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a3) {
                        binding.prescript6a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a3) {
                        binding.prescript7a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a3) {
                        binding.prescript8a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a3) {
                        binding.prescript9a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a3) {
                        binding.prescript10a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a3) {
                        binding.prescript11a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p3) {
                        binding.prescript12p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p3) {
                        binding.prescript1p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p3) {
                        binding.prescript2p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p3) {
                        binding.prescript3p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p3) {
                        binding.prescript4p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p3) {
                        binding.prescript5p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p3) {
                        binding.prescript6p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p3) {
                        binding.prescript7p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p3) {
                        binding.prescript8p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p3) {
                        binding.prescript9p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p3) {
                        binding.prescript10p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p3) {
                        binding.prescript11p3.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a3.findNavController().navigate(action)
                        break
                    }
                }
            }
        } else {
            //val re = "[^A-Za-z0-9 ]".toRegex()
            //val dName = re.replace(medName, "")
            val action =
                CalendarFragmentDirections.actionNavCalendarFragmentToPersonalEventDetailFragment(eventName = medName)
            //Check which view was selected and then navigate
            for (i in 0..2) {
                if (i == 0) {
                    if (view === binding.prescript1a) {
                        binding.prescript1a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a) {
                        binding.prescript2a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a) {
                        binding.prescript3a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a) {
                        binding.prescript4a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a) {
                        binding.prescript5a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a) {
                        binding.prescript6a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a) {
                        binding.prescript7a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a) {
                        binding.prescript8a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a) {
                        binding.prescript9a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a) {
                        binding.prescript10a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a) {
                        binding.prescript11a.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p) {
                        binding.prescript12p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p) {
                        binding.prescript1p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p) {
                        binding.prescript2p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p) {
                        binding.prescript3p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p) {
                        binding.prescript4p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p) {
                        binding.prescript5p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p) {
                        binding.prescript6p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p) {
                        binding.prescript7p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p) {
                        binding.prescript8p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p) {
                        binding.prescript9p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p) {
                        binding.prescript10p.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p) {
                        binding.prescript11p.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a.findNavController().navigate(action)
                        break
                    }
                } else if (i == 1) {
                    if (view === binding.prescript1a2) {
                        binding.prescript1a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a2) {
                        binding.prescript2a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a2) {
                        binding.prescript3a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a2) {
                        binding.prescript4a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a2) {
                        binding.prescript5a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a2) {
                        binding.prescript6a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a2) {
                        binding.prescript7a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a2) {
                        binding.prescript8a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a2) {
                        binding.prescript9a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a2) {
                        binding.prescript10a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a2) {
                        binding.prescript11a2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p2) {
                        binding.prescript12p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p2) {
                        binding.prescript1p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p2) {
                        binding.prescript2p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p2) {
                        binding.prescript3p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p2) {
                        binding.prescript4p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p2) {
                        binding.prescript5p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p2) {
                        binding.prescript6p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p2) {
                        binding.prescript7p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p2) {
                        binding.prescript8p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p2) {
                        binding.prescript9p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p2) {
                        binding.prescript10p2.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p2) {
                        binding.prescript11p2.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a2.findNavController().navigate(action)
                        break
                    }
                } else {
                    if (view === binding.prescript1a3) {
                        binding.prescript1a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2a3) {
                        binding.prescript2a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3a3) {
                        binding.prescript3a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4a3) {
                        binding.prescript4a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5a3) {
                        binding.prescript5a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6a3) {
                        binding.prescript6a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7a3) {
                        binding.prescript7a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8a3) {
                        binding.prescript8a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9a3) {
                        binding.prescript9a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10a3) {
                        binding.prescript10a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11a3) {
                        binding.prescript11a3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript12p3) {
                        binding.prescript12p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript1p3) {
                        binding.prescript1p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript2p3) {
                        binding.prescript2p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript3p3) {
                        binding.prescript3p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript4p3) {
                        binding.prescript4p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript5p3) {
                        binding.prescript5p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript6p3) {
                        binding.prescript6p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript7p3) {
                        binding.prescript7p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript8p3) {
                        binding.prescript8p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript9p3) {
                        binding.prescript9p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript10p3) {
                        binding.prescript10p3.findNavController().navigate(action)
                        break
                    } else if (view === binding.prescript11p3) {
                        binding.prescript11p3.findNavController().navigate(action)
                        break
                    } else {
                        binding.prescript12a3.findNavController().navigate(action)
                        break
                    }
                }
            }
        }
    }

    //Sets the background of the local date to a gradient background for easy recognition
    @RequiresApi(Build.VERSION_CODES.O)
    fun setBackground(){
        val localDate = LocalDate.now()
        val cal = Calendar.getInstance()
        currentDate = getString(R.string.sunDate, day.format(cal.time), localDate.dayOfMonth.toString())
        val my = localDate.month.name
        val pm = localDate.minusMonths(1).month.name
        currentMY = my[0]+my.substring(1).toLowerCase()+" "+localDate.year

        if(binding.sun.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)){
            binding.sun.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        } else if (binding.mon.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)){
            binding.mon.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        } else if (binding.tue.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)){
            binding.tue.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        } else if (binding.wed.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)){
            binding.wed.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        } else if (binding.thur.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)){
            binding.thur.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        } else if (binding.fri.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)){
            binding.fri.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        } else if (binding.sat.text == currentDate && (binding.monthYear.text == currentMY || binding.monthYear.text == pm)) {
            binding.sat.background = resources.getDrawable(R.drawable.green_teal_gradient_circle)
        }
    }

    //Function creates bar of days and dates below month and year
    fun getDateBar(calendar: Calendar, dayName: String, dayNum: Int){
        val day = SimpleDateFormat("EEE")
        //If day name is Sun add 1 to day of week to set the rest of the dates and set the dates in the date bar
        //Otherwise if day name is Mon add 1 to day of week to get Tue-Sat and subtract 1 for Sun and set dates in date bar
        //Otherwise if day name is Tue add 1 to day of week to get Wed-Sat and subtract 1 for Mon and subtract 2 for Sun and set dates in date bar
        //Otherwise if day name is Wed add 1 to day of week to get Thu-Sat and subtract 1 for Tue, 2 for Mon, 3 for Sun and set dates in date bar
        //Otherwise if day name is Thu add 1 to day of week to get Fri-Sat and subtract 1 for Wed, 2 for Tue, 3 for Mon, 4 for Sun and set dates in date bar
        //Otherwise if day name is Fri add 1 to day of week to get Sat and subtract 1 for Thu, 2 for Wed, 3 for Tue, 4 for Mon, 5 for Sun and set dates in date bar
        //Other wise suntract 1 for Fri, 2 for Thu, 3 for Wed, 4 for Tue, 5 for Mon, 6 for Sun and set dates in date bar
        if(dayName == "Sun") {
            val monday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val mondayName = day.format(calendar.time)
            val mondayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val tuesday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val tuesdayName = day.format(calendar.time)
            val tuesdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val wednesday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val wedName = day.format(calendar.time)
            val wedNum = calendar.get(Calendar.DAY_OF_MONTH)

            val thursday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val thursdayName = day.format(calendar.time)
            val thursdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val friday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val fridayName = day.format(calendar.time)
            val fridayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val saturday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val saturdayName = day.format(calendar.time)
            val saturdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, dayName, dayNum)
            binding.mon.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tue.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wed.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thur.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.fri.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.sat.text = getString(R.string.satDate, saturdayName, saturdayNum)
        } else if (dayName == "Mon"){
            val sunday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val sundayName = day.format(calendar.time)
            val sundayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val tuesday = calendar.add(Calendar.DAY_OF_WEEK, 2)
            val tuesdayName = day.format(calendar.time)
            val tuesdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val wednesday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val wedName = day.format(calendar.time)
            val wedNum = calendar.get(Calendar.DAY_OF_MONTH)

            val thursday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val thursdayName = day.format(calendar.time)
            val thursdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val friday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val fridayName = day.format(calendar.time)
            val fridayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val saturday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val saturdayName = day.format(calendar.time)
            val saturdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.mon.text = getString(R.string.monDate, dayName, dayNum)
            binding.tue.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wed.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thur.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.fri.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.sat.text = getString(R.string.satDate, saturdayName, saturdayNum)
        } else if (dayName == "Tue"){
            val monday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val mondayName = day.format(calendar.time)
            val mondayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val sunday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val sundayName = day.format(calendar.time)
            val sundayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val wednesday = calendar.add(Calendar.DAY_OF_WEEK, 3)
            val wedName = day.format(calendar.time)
            val wedNum = calendar.get(Calendar.DAY_OF_MONTH)

            val thursday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val thursdayName = day.format(calendar.time)
            val thursdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val friday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val fridayName = day.format(calendar.time)
            val fridayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val saturday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val saturdayName = day.format(calendar.time)
            val saturdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.mon.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tue.text = getString(R.string.tueDate, dayName, dayNum)
            binding.wed.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thur.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.fri.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.sat.text = getString(R.string.satDate, saturdayName, saturdayNum)
        } else if (dayName == "Wed"){
            val tuesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val tuesdayName = day.format(calendar.time)
            val tuesdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val monday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val mondayName = day.format(calendar.time)
            val mondayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val sunday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val sundayName = day.format(calendar.time)
            val sundayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val thursday = calendar.add(Calendar.DAY_OF_WEEK, 4)
            val thursdayName = day.format(calendar.time)
            val thursdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val friday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val fridayName = day.format(calendar.time)
            val fridayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val saturday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val saturdayName = day.format(calendar.time)
            val saturdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.mon.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tue.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wed.text = getString(R.string.wedDate, dayName, dayNum)
            binding.thur.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.fri.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.sat.text = getString(R.string.satDate, saturdayName, saturdayNum)
        } else if (dayName == "Fri"){
            val thursday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val thursdayName = day.format(calendar.time)
            val thursdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val wednesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val wedName = day.format(calendar.time)
            val wedNum = calendar.get(Calendar.DAY_OF_MONTH)

            val tuesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val tuesdayName = day.format(calendar.time)
            val tuesdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val monday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val mondayName = day.format(calendar.time)
            val mondayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val sunday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val sundayName = day.format(calendar.time)
            val sundayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val saturday = calendar.add(Calendar.DAY_OF_WEEK, 6)
            val saturdayName = day.format(calendar.time)
            val saturdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.mon.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tue.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wed.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thur.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.fri.text = getString(R.string.friDate, dayName, dayNum)
            binding.sat.text = getString(R.string.satDate, saturdayName, saturdayNum)
        } else if (dayName == "Sat"){
            val friday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val fridayName = day.format(calendar.time)
            val fridayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val thursday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val thursdayName = day.format(calendar.time)
            val thursdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val wednesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val wedName = day.format(calendar.time)
            val wedNum = calendar.get(Calendar.DAY_OF_MONTH)

            val tuesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val tuesdayName = day.format(calendar.time)
            val tuesdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val monday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val mondayName = day.format(calendar.time)
            val mondayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val sunday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val sundayName = day.format(calendar.time)
            val sundayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.mon.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tue.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wed.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thur.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.fri.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.sat.text = getString(R.string.satDate, dayName, dayNum)
        } else {
            val wednesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val wedName = day.format(calendar.time)
            val wedNum = calendar.get(Calendar.DAY_OF_MONTH)

            val tuesday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val tuesdayName = day.format(calendar.time)
            val tuesdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val monday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val mondayName = day.format(calendar.time)
            val mondayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val sunday = calendar.add(Calendar.DAY_OF_WEEK, -1)
            val sundayName = day.format(calendar.time)
            val sundayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val friday = calendar.add(Calendar.DAY_OF_WEEK, 5)
            val fridayName = day.format(calendar.time)
            val fridayNum = calendar.get(Calendar.DAY_OF_MONTH)

            val saturday = calendar.add(Calendar.DAY_OF_WEEK, 1)
            val saturdayName = day.format(calendar.time)
            val saturdayNum = calendar.get(Calendar.DAY_OF_MONTH)

            binding.sun.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.mon.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tue.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wed.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thur.text = getString(R.string.thurDate, dayName, dayNum)
            binding.fri.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.sat.text = getString(R.string.satDate, saturdayName, saturdayNum)
        }
    }

    //Formats the time from a calendar instance
    fun formatTime(calendar: Calendar): String {
        val time = SimpleDateFormat("hh:mm")
        val timeFormatted = time.format(calendar.time)
        return timeFormatted
    }

    //Formats the date from a calendar instance
    fun formatDate(calendar: Calendar): String {
        val date = SimpleDateFormat("mm-dd-yyyy")
        val dateFormatted = date.format(calendar.time)
        return dateFormatted
    }

    //Formats the month from a calendar instance
    fun formatMonth(calendar: Calendar): String {
        val month = SimpleDateFormat("MMMM")
        val monthFormatted = month.format(calendar.time)
        return monthFormatted
    }

    //Formats the day from a calendar instance
    fun formatDay(calendar: Calendar): String {
        val day = SimpleDateFormat("EEE")
        val dayFormatted = day.format(calendar.time)
        return dayFormatted
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //binding = null
    }

}