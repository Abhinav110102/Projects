package com.example.rxpress10.ui.faq

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.rxpress10.databinding.ActivityFaqfragmentBinding
import com.example.rxpress10.databinding.FragmentFAQBinding
import com.example.rxpress10.ui.faq.model.FAQ
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxpress10.R
import kotlinx.android.synthetic.main.fragment_f_a_q.*
//This is the faq fragment, containing most functionality code for the faq page.
class FAQFragment : Fragment() {

    private var _binding: FragmentFAQBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private lateinit var rvAdapter: RvAdapter
    private var faqList = ArrayList<FAQ>()

    override fun onResume() {
        super.onResume()
    }
    //Initializes the faq instance
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        faqList.clear()
        super.onCreate(savedInstanceState)
        _binding = FragmentFAQBinding.inflate(layoutInflater)

        //setContentView(binding.root)
        //binding.rvList.layoutManager = LinearLayoutManager(this)

        //initializing the recycler view for the questions and answers
        rvAdapter = RvAdapter(faqList)
        binding.rvList.adapter = rvAdapter
        //defining each currently available question, answer, and link to the rv, defaulting them as closed.
        val question1 = FAQ(
            "How do I add prescriptions?",
            "Click on the menu button in the top left corner of the screen. " +
                    "Then click on the prescriptions tab. " +
                    "Once there, click on the user that you want to add prescriptions to. From there you will click the plus " +
                    "sign (+) at the bottom of the page and click the other plus sign (+) and fill out the form to add a new prescription.",
            getString(R.string.linkText, "Go To Prescription Page"),
            false
        )
        val question2 = FAQ(
            "How do I view the times to take my medication?",
            "Go to the menu in the top left of the screen and click on the calendar tab." +
                    "This will show you the days of the week and the events of the selected day.",
            getString(R.string.linkText, "Go To Calendar"),
            false
        )
        val question3 = FAQ(
            "How do I sign out of my account?",
            "Go to the menu button in the top left of the screen and click on the settings tab." +
                    "In order to sign out, click the sign out button.",
            getString(R.string.linkText, "Go To Settings"),
            false
        )
        val question4 = FAQ(
            "How do I submit a question?",
            "Go to the bottom of the FAQ page. Type a question in the textbox, then hit send." +
                    "Then, select your preferred email application that is signed in to your email." +
                    "A subject and recipient should automatically be filled as well as your question. Send the email and we will review the question.",
            "",
            false
        )
        val question5 = FAQ(
            "How do I put personal events on the calendar?",
            "Go to the calendar page, click on the date that you want to put the event on, click on the button at the bottom of the screen with a + sign." +
                    "Then, type in the name, start time, end time, and the description of the event you are adding then click the 'Add Event' button to submit." +
                    "An event will show up on your selected date in a light teal color. ",
            getString(R.string.linkText, "Go To Calendar"),
            false
        )
        val question6 = FAQ(
            "How do I edit or delete my personal events on the calendar?",
            "Click on the event you want to edit or delete. This will bring you to a screen that shows the details of the event." +
                    "Then, select either 'Edit' or 'Delete' at the bottom of the screen. If edit is selected it will bring you to a page" +
                    "where you can edit any of the event details. If delete is selected the event will be deleted from your account.",
            getString(R.string.linkText,"Go To Calendar"),
            false
        )
        val question7 = FAQ(
            "How do I tell on the calendar which medication is mine versus other users I have added?",
            "On the calendar each individual user has their own color event. The primary user's events are in a dark teal color.",
            getString(R.string.linkText, "Go To Calendar"),
            false
        )
        val question8 = FAQ(
            "How do I see my profile information?",
            "Go to the settings page and click on the button that says 'Profile'. This button will take you to a screen where you" +
                    "Can see your current profile information as well as select whether you want to edit your information or not. In order to" +
                    "edit your profile information, click on the 'Edit Profile' button. This will take you to a screen to edit the information about yourself.",
            getString(R.string.linkText, "Go To Profile Page"),
            false
        )
        val question9 = FAQ(
            "How do I delete other users?",
            "First go to the prescription page and click the user that you want to delete, Then click the button on the bottom right of your" +
                    "device. Then you will click the button that has a trash can to delete that user.",
            getString(R.string.linkText, "Go To Prescription Page"),
            false
        )
        val question10 = FAQ(
            "How do I delete prescriptions?",
            "First go to the prescription page and click the user that has the prescriptions you want to delete, Then click the prescription you want to" +
                    "delete. Finally click the bottom right button of your device and click the trashcan icon to delete that prescription.",
            getString(R.string.linkText, "Go To Prescription Page"),
            false
        )
        val question11 = FAQ(
            "How do I edit prescriptions?",
            "First go to the prescription page and click the user that has the prescriptions you want to edit, Then click the prescription you want to" +
                    "edit. Finally click the bottom right button of your device and click the pencil icon to edit that prescription. Enter the information that you want" +
                    "in your prescription.",
            getString(R.string.linkText, "Go To Prescription Page"),
            false
        )
        val question12 = FAQ(
            "How do I add users?",
            "Go to the top right to go to the prescription page and then click the plus button (+) and enter that users name and they will be added to the list." +
                    "How ever you are limited to only 6 users including yourself.",
            getString(R.string.linkText, "Go To Prescription Page"),
            false
        )
        //adding all defined questions to the list
        faqList.add(question4)
        faqList.add(question1)
        faqList.add(question12)
        faqList.add(question9)
        faqList.add(question10)
        faqList.add(question11)
        faqList.add(question2)
        faqList.add(question3)
        faqList.add(question5)
        faqList.add(question6)
        faqList.add(question7)
        faqList.add(question8)

        rvAdapter.notifyDataSetChanged()


        //initializing question send button
        val sendButton = binding.sendButton
        sendButton.setOnClickListener {
            //using intent to prefill an email with the question
            val message = binding.questionSubmission.text.toString()
            val intent = Intent().apply {
                action = Intent.ACTION_SENDTO
                data = Uri.parse("mailto:")
                //The following line contains the recipient email for all submitted questions
                putExtra(Intent.EXTRA_EMAIL, arrayOf("rxpresscapstone@gmail.com"))
                putExtra(Intent.EXTRA_SUBJECT, "User Question")
                putExtra(Intent.EXTRA_TEXT, message)
                //type = "message/rfc822"
            }

            startActivity(intent)
        }

    return _binding!!.root
    }

    //Destroy view
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}