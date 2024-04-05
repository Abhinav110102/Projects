package com.example.rxpress10.ui.prescriptions

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment

import androidx.navigation.fragment.navArgs

import com.example.android.sports.model.Medication
import com.example.rxpress10.API.Urls
import com.example.rxpress10.MainActivity
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivityPrescriptionFragmentBinding


import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_profile_fragment.*
import kotlinx.android.synthetic.main.user_card.*

import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException

// Prescription screen is able to add data to the database by implementing the RXnorm APIs available
open class PrescriptionFragment : Fragment(R.layout.activity_prescription_fragment) {

    // initializing binding database and authentication
    private lateinit var binding: ActivityPrescriptionFragmentBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // initializing the list for the adapter
    private var displayList = ArrayList<String>()

    // navigation arg init
    private lateinit var userNameArg: String

    // api init
    private val client = OkHttpClient()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionFragmentBinding.inflate(layoutInflater)
    }

    // onResume is when the user wants to edit again a dropdown it will reset the spinner
    override fun onResume() {
        super.onResume()
        val units = resources.getStringArray(R.array.Units)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, units)
        binding.unitsEditText.setAdapter(arrayAdapter)

        val frequency = resources.getStringArray(R.array.Frequency)
        val arrayFrequencyAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, frequency)
        binding.frequencyEditText.setAdapter(arrayFrequencyAdapter)

        val administered = resources.getStringArray(R.array.Adminstered)
        val arrayAdministeredAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, administered)
        binding.takenEditText.setAdapter(arrayAdministeredAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Navigation arguments passed from prescription home
        val args: PrescriptionFragmentArgs by navArgs()
        userNameArg = args.userName

        // all alphanumeric numbers will be replace the original userName if it has
        // non alphanumric chars for the database name.
        val re = "[^A-Za-z0-9 ]".toRegex()
        userNameArg = re.replace(userNameArg, "")

        Log.d("PrescriptionFragment", userNameArg)

        // hiding the softkeyboard when clicking the dropdowns to make it user friendly
        binding.unitsEditText.setOnClickListener {
            view?.let { activity?.hideKeyboard(it) }
        }

        binding.frequencyEditText.setOnClickListener {
            view?.let { activity?.hideKeyboard(it) }
        }

        binding.takenEditText.setOnClickListener {
            view?.let { activity?.hideKeyboard(it) }
        }

        // setting up the string array adapters for dropdowns
        val units = resources.getStringArray(R.array.Units)
        val arrayUnitAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, units)
        binding.unitsEditText.setAdapter(arrayUnitAdapter)

        val frequency = resources.getStringArray(R.array.Frequency)
        val arrayFrequencyAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, frequency)
        binding.frequencyEditText.setAdapter(arrayFrequencyAdapter)

        val administered = resources.getStringArray(R.array.Adminstered)
        val arrayAdministeredAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, administered)
        binding.takenEditText.setAdapter(arrayAdministeredAdapter)

        // on text listener for the prescriptionName getting suggestions from RXnorm suggestion API
        binding.prescriptionNameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                TODO("Not yet implemented")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                Log.d("Char Sequence", "$p0")

                // pO is the key entered each keyStroke

                // accessing the API Url for suggestions
                val url = Urls().getApproximateMatch + p0 + Urls().maxEntries

                // creating an suggestion array to display for each character typed so it is updated
                val suggestionArr = fetchSuggestion(url)

                // creating an adapter to display the values
                val arrayPrescriptionAdapter =
                    ArrayAdapter(
                        requireContext(),
                        R.layout.dropdown_item,
                        suggestionArr.toTypedArray()
                    )

                // inflating the suggestion list
                binding.prescriptionNameEditText.setAdapter(arrayPrescriptionAdapter)

                // clearing the suggestionArr per keystroke causes duplicate suggestions without
                suggestionArr.clear()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })


        // on click save the medication data
        binding.addMedicationButton.setOnClickListener { getQuery() }

        return binding.root

    }

    // calls the suggestion API and returns a Mutable list of the suggestions
    fun fetchSuggestion(url: String): MutableList<String> {
        val error = "Sorry, we could not find what you were looking for please try again"

        // initializing the request code for successful http request
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            // got a response code of 200
            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // parsing the jsonObject accessed from the API
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject

                // getting jsonObject approximateGroup
                val approximateGroup = jsonObject.getJSONObject("approximateGroup")

                // if there is a candidate continue
                if (approximateGroup.has("candidate")) {
                    val candidate = approximateGroup.getJSONArray("candidate")

                    // looping through each candidate
                    for (i in 0
                            until candidate.length()) {

                        // creating a thread for API call
                        activity?.runOnUiThread(Runnable {
                            kotlin.run {

                                // if the source is from RxNorm Continue
                                if (candidate.getJSONObject(i).getString("source")
                                        .equals("RXNORM", true)
                                ) {
                                    // if they have a name add to array.
                                    if (candidate.getJSONObject(i).has("name")) {

                                        val name = candidate.getJSONObject(i).getString("name")
                                        val rxcui = candidate.getJSONObject(i).getString("rxcui")
                                        val score = candidate.getJSONObject(i).getString("score")

                                        // adding rxcui to list if name is present
                                        displayList = populateSuggestionList(rxcui, name, score)
                                    }
                                }
                            }
                        })
                    }
                } /* other wise do nothing */
                else {
                    Log.i("fetMedData", "entered else statement")
                    activity?.runOnUiThread(Runnable {
                        kotlin.run {
                            Log.i("fetMedData", "entered if statement")
//                            Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                        }
                    })

                }
            }
        })

        // returns the suggestion list to display on the adapter
        Log.i("Suggestion List", "$displayList")
        return displayList
    }


    // populating the suggestion List with the names
    fun populateSuggestionList(rxcui: String, name: String, score: String): ArrayList<String> {
        displayList.add(name)
        return displayList
    }

    // Hides the soft keyboard programmatically
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun keyBoardCloseOnEnter() {
        binding.prescriptionNameEditText.setOnKeyListener { view, keyCode, _ ->
            MainActivity().handleKeyEvent(
                view,
                keyCode
            )
        }
        binding.dosageEditText.setOnKeyListener { view, keyCode, _ ->
            MainActivity().handleKeyEvent(
                view,
                keyCode
            )
        }
        binding.frequencyEditText.setOnKeyListener { view, keyCode, _ ->
            MainActivity().handleKeyEvent(
                view,
                keyCode
            )
        }
        binding.otherRequirementsEditText.setOnKeyListener { view, keyCode, _ ->
            MainActivity().handleKeyEvent(
                view,
                keyCode
            )
        }
        binding.rxNumEditText.setOnKeyListener { view, keyCode, _ ->
            MainActivity().handleKeyEvent(
                view,
                keyCode
            )
        }
    }

    // passes in the data and creates a medication object with all the inputed data
    fun createMedication(
        name: String, dosage: Double, units: String,
        frequency: Int, administered: String, other: String?, rxNum: String?
    ): Medication {
        // reads the data input on each text input and assigns it to a variable

        // creates an object named medication with the user input
        return Medication(
            name,
            dosage,
            units,
            frequency,
            administered,
            other,
            rxNum
        )
    }

    // checking if the string is a valid URL
    fun String.isValidUrl(): Boolean = Patterns.WEB_URL.matcher(this).matches()

    // replaces the spaces with %20 to make the URL valid
    fun replaceSpaces(input: String): String {
        val rep = "%20"
        val newQuery = input.replace(" ", rep)

        return newQuery
    }

    // creating a URL to call getDrugs
    private fun getQuery() {

        var drugQueryName = binding.prescriptionNameEditText.text.toString()
        drugQueryName = replaceSpaces(drugQueryName)
        var tempUrl = Urls().getDrugs + drugQueryName + Urls().key
        if (tempUrl.isValidUrl()) {
            Log.i("getQuery", "entered in valid url if")

            fetchMedData(tempUrl)
        } else {
            Log.i("getQuery", "entered in non valid url else")
            Toast.makeText(
                context,
                "Sorry we could not find the drug you are looking for",
                Toast.LENGTH_LONG
            ).show()
        }
        tempUrl = Urls().getDrugs

    }

    // Checking if the prescripiton entered is valid or not
    private fun fetchMedData(url: String) {
        // init variables
        var increment = 0
        val error = "Sorry, we could not find what you were looking for please try again"
        val request = okhttp3.Request.Builder().url(url).build()

        // access http request like fetch Suggestion with different url
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")
                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                // parsing the json file
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject

                val drugGroup = jsonObject.getJSONObject("drugGroup")

                if (drugGroup.has("conceptGroup")) {
                    val conceptGroup = drugGroup.getJSONArray("conceptGroup")
                    for (i in 0
                            until conceptGroup.length()) {

                        println("${conceptGroup.getJSONObject(i)}")

                        if (conceptGroup.getJSONObject(i).has("conceptProperties")) {

                            // Creating a JSON array
                            val conceptProperties =
                                conceptGroup.getJSONObject(i)
                                    .getJSONArray("conceptProperties") as JSONArray

                            for (j in 0 until conceptProperties.length()) {
                                activity?.runOnUiThread(Runnable {
                                    kotlin.run {
                                        val name =
                                            conceptProperties.getJSONObject(j).getString("name")
                                        Log.i("Valid Drug Name?", "True")

                                        increment += 1

                                        if (increment == 1) {
                                            // calls database if increment is one
                                            database()
                                        }
                                    }
                                })
                            }
                        }

                    }
                }
                // failed to find the prescription
                else {
                    Log.i("fetMedData", "entered else statement")
                    println(drugGroup.getString("name").isNullOrEmpty())
                    if (drugGroup.getString("name").toString().isNotEmpty()) {
                        activity?.runOnUiThread(Runnable {
                            kotlin.run {
                                Log.i("fetMedData", "entered if statement")
                                println(drugGroup.getString("name"))
                                binding.prescriptionNameEditText.text!!.clear()
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        })
    }

    // adding the prescription to the database
    private fun database() {
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        Log.d("Database", "Entered Database method")


        /* ADDING PRIMARY USER PRESCRIPTION TO DATABASE */
        if (userNameArg.equals("Your", true)) {
            Log.d("Database", "Primary User")

            // capitalize the first letter of the prescription name for standards
            var prescriptionName =
                binding.prescriptionNameEditText.text.toString().capitalize()

            // filtering the prescription name by trimming extra spaces
            val filteredPrescriptionName = prescriptionName.trim().replace("\\s+".toRegex(), " ")
            Log.d("Capitalizes?", "$prescriptionName")
            val prescriptionDosage = binding.dosageEditText.text.toString()
            val prescriptionUnits = binding.unitsEditText.text.toString()
            val prescriptionFrequency = binding.frequencyEditText.text.toString()
            val prescriptionAdminstered = binding.takenEditText.text.toString()
            val prescriptionOther = binding.otherRequirementsEditText.text.toString()
            val prescriptionRxNumber = binding.rxNumEditText.text.toString()

            // database reference
            database = FirebaseDatabase.getInstance()
                .getReference("users/$userId/ChildUsers/Primary/Medications")

            // checking if the values are not empty that are required
            if (prescriptionName.isNotEmpty() && prescriptionDosage.isNotEmpty()
                && prescriptionFrequency.isNotEmpty() && prescriptionFrequency.toInt() <= 4 && prescriptionAdminstered.isNotEmpty()
            ) {

                // checking for duplicate prescription values already in the database
                checkDuplicate(
                    filteredPrescriptionName,
                    prescriptionDosage.toDouble(),
                    prescriptionUnits,
                    prescriptionFrequency.toInt(),
                    prescriptionAdminstered,
                    prescriptionOther,
                    prescriptionRxNumber,
                    "Primary"
                )

            } else {
                // Providing error messages to null fields or improper imported data
                if (prescriptionName.isNullOrEmpty()) {
                    binding.prescriptionNameEditText.text!!.clear()
                    Toast.makeText(
                        context,
                        "Must enter value in \"Prescription Name\".",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (prescriptionDosage.isNullOrEmpty()) {
                    binding.dosageEditText.text!!.clear()
                    Toast.makeText(context, "Must enter value in \"Dosage\".", Toast.LENGTH_SHORT)
                        .show()
                }

                if (prescriptionUnits.isNullOrEmpty()) {
                    binding.unitsEditText.text!!.clear()
                    Toast.makeText(context, "Must enter value in \"Units\".", Toast.LENGTH_SHORT)
                        .show()
                }


                if (prescriptionFrequency.isNullOrEmpty()) {
                    binding.frequencyEditText.text!!.clear()
                    Toast.makeText(
                        context,
                        "Must enter value in \"How Many Times a day\".",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                if (prescriptionAdminstered.isNullOrEmpty()) {
                    binding.takenEditText.text!!.clear()
                    Toast.makeText(
                        context,
                        "Must enter value in \"How Is The Prescription Taken\".",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            /* ADDING OTHER USERS PRESCRIPTIONS TO DATABASE */
            Log.d("Database", "$userNameArg User")

            // capitalize the first letter of the prescription name for standards
            var prescriptionName =
                binding.prescriptionNameEditText.text.toString().capitalize()

            // filtering the prescription name by trimming extra spaces
            val filteredPrescriptionName = prescriptionName.trim().replace("\\s+".toRegex(), " ")
            val prescriptionDosage = binding.dosageEditText.text.toString()
            val prescriptionUnits = binding.unitsEditText.text.toString()
            val prescriptionFrequency = binding.frequencyEditText.text.toString()
            val prescriptionOther = binding.otherRequirementsEditText.text.toString()
            val prescriptionAdminstered = binding.takenEditText.text.toString()
            val prescriptionRxNumber = binding.rxNumEditText.text.toString()

            // database reference
            database = FirebaseDatabase.getInstance()
                .getReference("users/$userId/ChildUsers/$userNameArg/Medications")

            // checking if the values are not empty that are required
            if (prescriptionName.isNotEmpty() && prescriptionDosage.isNotEmpty()
                && prescriptionFrequency.isNotEmpty() && prescriptionFrequency.toInt() <= 4 && prescriptionAdminstered.isNotEmpty()
            ) {

                Log.d("Database", "About to Check Duplicate")

                // checking duplicates in the database
                checkDuplicate(
                    filteredPrescriptionName,
                    prescriptionDosage.toDouble(),
                    prescriptionUnits,
                    prescriptionFrequency.toInt(),
                    prescriptionAdminstered,
                    prescriptionOther,
                    prescriptionRxNumber,
                    userNameArg
                )
            } else {
                // Providing error messages to null fields or improper imported data
                if (prescriptionName.isNullOrEmpty()) {
                    binding.prescriptionNameEditText.text!!.clear()
                    Toast.makeText(
                        context,
                        "Must enter value in \"Prescription Name\".",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (prescriptionDosage.isNullOrEmpty()) {
                    binding.dosageEditText.text!!.clear()
                    Toast.makeText(context, "Must enter value in \"Dosage\".", Toast.LENGTH_SHORT)
                        .show()
                }
                if (prescriptionUnits.isNullOrEmpty()) {
                    binding.unitsEditText.text!!.clear()
                    Toast.makeText(context, "Must enter value in \"Units\".", Toast.LENGTH_SHORT)
                        .show()
                }

                if (prescriptionFrequency.isNullOrEmpty()) {
                    binding.frequencyEditText.text!!.clear()
                    Toast.makeText(
                        context,
                        "Must enter value in \" How Many Times a day\".",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                if (prescriptionAdminstered.isNullOrEmpty()) {
                    binding.takenEditText.text!!.clear()
                    Toast.makeText(
                        context,
                        "Must enter value in \" How Is The Prescription Taken\".",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }

    // checking duplicates in the database
    fun checkDuplicate(
        name: String,
        prescriptionDosage: Double,
        prescriptionUnits: String,
        prescriptionFrequency: Int,
        prescriptionAdministered: String,
        prescriptionOther: String,
        prescriptionRxNumber: String,
        user: String
    ) {

        // database name is replacing the . with nothing since database cannot have non alphanumeric values
        val databaseName = name.replace(".", "")

        Log.d("Checking Duplicates", "$user")

        // db reference
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance()
            .getReference("users/$userId/ChildUsers/$user/Medications")

        Log.d("Checking Duplicates", "$database")

        // looping through the database to add to the database
        database.get().addOnSuccessListener { it ->
            val users = it.childrenCount.toInt()

            // if there are no prescription creates the med no matter what
            if (users == 0) {
                val medication = createMedication(
                    name, prescriptionDosage,
                    prescriptionUnits, prescriptionFrequency, prescriptionAdministered,
                    prescriptionOther, prescriptionRxNumber
                )
                Log.d("Checking Duplicates", "$database")

                // successfully added the prescription clears the text fields
                database.child(databaseName).setValue(medication).addOnSuccessListener {
                    binding.prescriptionNameEditText.text!!.clear()
                    binding.dosageEditText.text!!.clear()
                    binding.unitsEditText.text!!.clear()
                    binding.frequencyEditText.text!!.clear()
                    binding.otherRequirementsEditText.text!!.clear()
                    binding.takenEditText.text!!.clear()
                    binding.rxNumEditText.text!!.clear()

                    Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                }
            }


            val count = it.childrenCount.toInt()
            Log.d("Checking Duplicates", "count = $count")
            val medName = it.children
            var i = 0

            // checking the duplicates
            medName.forEach {
                val preName = it.child("name").value.toString()
                Log.d("Checking Dups...", preName)
                i += 1

                // if the name is equal the the prescription name from the child
                // already in the database increases the i by count + 1
                if (name.equals(preName, true)) {
                    i = count + 1
                    Log.d("Checking Duplicates", "There is a duplicate")
                    Log.d("Checking Duplicates", " i = $i in if statement")
                    // if there is a duplicate do not change any data
                    binding.prescriptionNameEditText.text.clear()

                    // if i > count that means there is already that prescription
                    if (i > count)
                        Toast.makeText(
                            context,
                            "$name has already been added",
                            Toast.LENGTH_SHORT
                        ).show()

                // otherwise if the count is equal to i there are no duplicates
                // therefore adding the prescription
                } else if (i == count) {

                    Log.d("Checking Duplicates", " i = $i in else statement")

                    // if there is not a duplicate add the medication
                    Log.d("Checking Duplicates", "There is no duplicate")
                    val medication = createMedication(
                        name, prescriptionDosage,
                        prescriptionUnits, prescriptionFrequency, prescriptionAdministered,
                        prescriptionOther, prescriptionRxNumber
                    )
                    Log.d("Checking Duplicates", "$database")
                    database.child(databaseName).setValue(medication).addOnSuccessListener {
                        binding.prescriptionNameEditText.text!!.clear()
                        binding.dosageEditText.text!!.clear()
                        binding.unitsEditText.text!!.clear()
                        binding.frequencyEditText.text!!.clear()
                        binding.otherRequirementsEditText.text!!.clear()
                        binding.rxNumEditText.text!!.clear()
                        binding.takenEditText.text!!.clear()

                        Toast.makeText(context, "Successfully Saved", Toast.LENGTH_SHORT).show()
                    }.addOnFailureListener {
                        Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }

    }
}