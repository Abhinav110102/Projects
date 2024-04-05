package com.example.rxpress10.ui.home


import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxpress10.R
import com.example.rxpress10.adapter.PrescriptionAdapter
import com.example.rxpress10.adapter.ReminderAdapter
import com.example.rxpress10.databinding.FragmentHomeBinding
import okhttp3.OkHttpClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.rxpress10.model.Prescriptions
import com.example.rxpress10.model.Reminders

class HomeFragment : Fragment() {
    private val client = OkHttpClient()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth
    private lateinit var prescripts: ArrayList<String>
    var SELECT_PICTURE = 200


    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //Set binding
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root


        //Set the prescription recycler view
        setPrescriptionRecyclerView()
        //Set the reminder recycler view
        setReminderRecyclerView()

        //Get the current day name and day num
        val calendar = Calendar.getInstance()
        val day = SimpleDateFormat("EEE")
        val dayName = day.format(calendar.time)
        val dayNum = calendar.get(Calendar.DAY_OF_MONTH)
        //Set the weekly calendar
        getDateBar(calendar, dayName, dayNum)

        //initially set profile name to not visible
        binding.profileName.visibility = View.GONE

        //Get uId from firebase authentication
        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        //Get database instance of user info
        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/UserInfo")

        //Set the profile name from the database
        database.get().addOnSuccessListener {
            if (it.childrenCount.toInt() == 0) {
                binding.profileName.text = ""
            } else {
                binding.profileName.visibility = View.VISIBLE
                binding.profileName.text = getString(
                    R.string.userName,
                    it.child("firstName").value.toString(),
                    it.child("lastName").value.toString()
                )
            }
        }.addOnFailureListener {
            binding.profileName.text = ""
        }

        //Get database instance of pic info
        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/PicInfo")

        database.get().addOnSuccessListener {
            //If there is not any picture information do nothing
            //Else get the uri and if there is an error let the user know
            if (it.childrenCount.toInt() == 0) {

            } else {
                val image = it.child("picture").value.toString()
                val uri = Uri.parse(it.child("picture").value.toString())
                try {
                    val inputStream = activity?.contentResolver?.openInputStream(uri)
                    val drawable = Drawable.createFromStream(inputStream, uri.toString())
                    binding.profilePicture.background = drawable
                } catch (e: Exception) {
                    //Normally if user uninstalls the app and reinstalls or if the picture is not on the device the user is using
                    Toast.makeText(this.context, "Your profile picture has been lost. Please reset it.", Toast.LENGTH_LONG).show()
                }
            }
        }.addOnFailureListener {
            binding.profileName.text = ""
        }

        return root
    }

    //Setting the prescriptions in the recycler view
    fun setPrescriptionRecyclerView(){
        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/ChildUsers/Primary/Medications")

        database.get().addOnSuccessListener {
            val meds = it.childrenCount.toInt()
            //If no medications do not show anything
            //Otherwise dynamically add to list and then set the recycler view
            if (meds == 0){
                binding.prescriptionView.visibility = View.GONE
            } else {
                val list = ArrayList<Prescriptions>()
                for (i in 1..meds) {
                    list.add(Prescriptions(R.string.prescription))
                }
                val recyclerView = binding.prescriptionView
                recyclerView.adapter = PrescriptionAdapter(this, list)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
                recyclerView.setItemViewCacheSize(10)
                recyclerView.setDrawingCacheEnabled(true)
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    //Setting the reminders in the recycler view
    fun setReminderRecyclerView(){
        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/ChildUsers/Primary/Medications")

        database.get().addOnSuccessListener {
            val meds = it.childrenCount.toInt()
            //If there are not any medications do not show anything
            //Otherwise dynamically add to the list and set the recycler view
            if (meds == 0){
                binding.reminderview.visibility = View.GONE
            } else {
                val list = ArrayList<Reminders>()
                for (i in 1..meds) {
                    list.add(Reminders(R.string.reminders))
                }
                val recyclerView = binding.reminderview
                recyclerView.adapter = ReminderAdapter(this, list)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
                recyclerView.setItemViewCacheSize(10)
                recyclerView.setDrawingCacheEnabled(true)
                recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

    //Function creates bar of days and dates
    fun getDateBar(calendar: Calendar, dayName: String, dayNum: Int){
        val day = SimpleDateFormat("EEE")
        //Set the current date background as purple
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

            binding.sunday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, dayName, dayNum)
            binding.monday.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tuesday.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wednesday.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thursday.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.friday.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.saturday.text = getString(R.string.satDate, saturdayName, saturdayNum)
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

            binding.monday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.monday.text = getString(R.string.monDate, dayName, dayNum)
            binding.tuesday.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wednesday.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thursday.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.friday.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.saturday.text = getString(R.string.satDate, saturdayName, saturdayNum)
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

            binding.tuesday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.monday.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tuesday.text = getString(R.string.tueDate, dayName, dayNum)
            binding.wednesday.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thursday.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.friday.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.saturday.text = getString(R.string.satDate, saturdayName, saturdayNum)
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

            binding.wednesday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.monday.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tuesday.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wednesday.text = getString(R.string.wedDate, dayName, dayNum)
            binding.thursday.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.friday.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.saturday.text = getString(R.string.satDate, saturdayName, saturdayNum)
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

            binding.friday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.monday.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tuesday.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wednesday.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thursday.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.friday.text = getString(R.string.friDate, dayName, dayNum)
            binding.saturday.text = getString(R.string.satDate, saturdayName, saturdayNum)
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

            binding.saturday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.monday.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tuesday.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wednesday.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thursday.text = getString(R.string.thurDate, thursdayName, thursdayNum)
            binding.friday.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.saturday.text = getString(R.string.satDate, dayName, dayNum)
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

            binding.thursday.background = resources.getDrawable(R.drawable.purple)
            binding.sunday.text = getString(R.string.sunDate, sundayName, sundayNum)
            binding.monday.text = getString(R.string.monDate, mondayName, mondayNum)
            binding.tuesday.text = getString(R.string.tueDate, tuesdayName, tuesdayNum)
            binding.wednesday.text = getString(R.string.wedDate, wedName, wedNum)
            binding.thursday.text = getString(R.string.thurDate, dayName, dayNum)
            binding.friday.text = getString(R.string.friDate, fridayName, fridayNum)
            binding.saturday.text = getString(R.string.satDate, saturdayName, saturdayNum)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}