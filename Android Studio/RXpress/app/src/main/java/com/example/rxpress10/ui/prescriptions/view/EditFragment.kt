package com.example.rxpress10.ui.prescriptions.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.renderscript.ScriptGroup.Input
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.adapters.TextViewBindingAdapter.setText
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.sports.model.Medication
import com.example.rxpress10.MenuActivity
import com.example.rxpress10.R
import com.example.rxpress10.databinding.FragmentEditBinding
import com.example.rxpress10.databinding.FragmentViewPrescriptionBinding
import com.example.rxpress10.ui.prescriptions.PrescriptionHomeFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.user_card.*

// Edits the prescription that was from view prescription screen and passes its data to the edit
// screen and can be edited
class EditFragment : Fragment(R.layout.fragment_edit) {
    // initializing binding database and authentication
    private lateinit var binding: FragmentEditBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    // initializing variables for database and transferring data between screens
    private var name = ""
    private var dosage = ""
    private var units = ""
    private var frequency = ""
    private var administered = ""
    private var other = ""
    private var rxNum = ""
    private var userNameArg = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentEditBinding.inflate(layoutInflater)
    }


    // onResume is when the user wants to edit again a dropdown it will reset the spinner
    override fun onResume() {
        super.onResume()
        val unitsArray = resources.getStringArray(R.array.Units)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, unitsArray)
        binding.unitsEditText.setAdapter(arrayAdapter)

        val frequencyArray = resources.getStringArray(R.array.Frequency)
        val arrayFrequencyAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, frequencyArray)
        binding.frequencyEditText.setAdapter(arrayFrequencyAdapter)

        val administeredArray = resources.getStringArray(R.array.Adminstered)
        val arrayAdministeredAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, administeredArray)
        binding.takenEditText.setAdapter(arrayAdministeredAdapter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val root: View = binding.root

        // setting up the string array adapters for dropdowns
        val unitsArray = resources.getStringArray(R.array.Units)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, unitsArray)
        binding.unitsEditText.setAdapter(arrayAdapter)

        val frequencyArray = resources.getStringArray(R.array.Frequency)
        val arrayFrequencyAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, frequencyArray)
        binding.frequencyEditText.setAdapter(arrayFrequencyAdapter)

        val administeredArray = resources.getStringArray(R.array.Adminstered)
        val arrayAdministeredAdapter =
            ArrayAdapter(requireContext(), R.layout.dropdown_item, administeredArray)
        binding.takenEditText.setAdapter(arrayAdministeredAdapter)

        // Navigation arguments passed from prescription View
        val args: EditFragmentArgs by navArgs()
        name = args.name
        dosage = args.dosage
        units = args.units
        frequency = args.frequency
        administered = args.administered
        other = args.other
        rxNum = args.rxNum
        userNameArg = args.userName

        // prepopulating the edit text with the arguments passed
        binding.dosageEditText.setText(dosage)
        binding.unitsEditText.setText(units)
        binding.frequencyEditText.setText(frequency)
        binding.takenEditText.setText(administered)
        binding.otherRequirementsEditText.setText(other)
        binding.rxNumEditText.setText(rxNum)

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

        // setting the title to the prescription the user is editing
        binding.prescriptionEditTitle.text = getString(R.string.prescription_view_title, name)

        // if the editBtn has been clicked it will save the data
        binding.editBtn.setOnClickListener {
            editPrescription()
        }
        return root

    }

    // Hides the soft keyboard programmatically
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    // saves the information entered by the user in the database updates its values
    private fun editPrescription() {

        // initializing firebase authentication and databse to access its child values
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance()
            .getReference("users/$userId/ChildUsers/$userNameArg/Medications")

        // values entered in the editText fields being saved into variables
        val prescriptionDosage = binding.dosageEditText.text.toString()
        val prescriptionUnits = binding.unitsEditText.text.toString()
        val prescriptionFrequency = binding.frequencyEditText.text.toString()
        val prescriptionAdministered = binding.takenEditText.text.toString()
        val prescriptionOther = binding.otherRequirementsEditText.text.toString()
        val prescriptionRxNumber = binding.rxNumEditText.text.toString()

        // database name is replacing the . with nothing since database cannot have non alphanumeric values
        val databaseName = name.replace(".", "")

        // checking if the values are not empty that are required
        if (prescriptionDosage.isNotEmpty() && prescriptionUnits.isNotEmpty() && prescriptionFrequency.isNotEmpty() && prescriptionFrequency.toInt() <= 4) {
            // the child name will be the prescription name and it will set the value of medication to that in the data base
            // the success listener will clear the text inputs after being added to the data base and will return a Toast
            // stating if it successfully saved to the database or not
            val medication = Medication(
                name,
                prescriptionDosage.toDouble(),
                prescriptionUnits,
                prescriptionFrequency.toInt(),
                prescriptionAdministered,
                prescriptionOther,
                prescriptionRxNumber
            )
            database.child(databaseName).setValue(medication).addOnSuccessListener {
                // if successful the data has been added and clears the fields
                binding.dosageEditText.text!!.clear()
                binding.unitsEditText.text!!.clear()
                binding.frequencyEditText.text!!.clear()
                binding.takenEditText.text!!.clear()
                binding.otherRequirementsEditText.text!!.clear()
                binding.rxNumEditText.text!!.clear()


                // navigates to Prescription View with the updated values
                val action =
                    EditFragmentDirections.actionEditFragmentToViewPrescription(userNameArg, name)
                binding.editBtn.findNavController().navigate(action)

                Toast.makeText(context, "Successfully Edited $name", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

        } else {
            // Providing error messages to null fields or improper imported data
            if (prescriptionFrequency.toInt() > 4) {
                binding.frequencyEditText.text!!.clear()
                Toast.makeText(
                    context,
                    "Frequency cannot be more than 4 times a day, please try again.",
                    Toast.LENGTH_LONG
                ).show()
            }

            if (prescriptionDosage.isNullOrEmpty()) {
                binding.dosageEditText.text!!.clear()
                Toast.makeText(context, "Must enter value in \"Dosage\".", Toast.LENGTH_SHORT)
                    .show()
            }

            if (prescriptionUnits.isNullOrEmpty()) {
                binding.unitsEditText.text!!.clear()
                Toast.makeText(context, "Must enter value in \"Units\".", Toast.LENGTH_SHORT).show()
            }

            if (prescriptionFrequency.isNullOrEmpty()) {
                binding.frequencyEditText.text!!.clear()
                Toast.makeText(
                    context, "Must enter value in \"How Many Times a day\".", Toast.LENGTH_SHORT
                ).show()
            }

            if (prescriptionAdministered.isNullOrEmpty()) {
                binding.frequencyEditText.text!!.clear()
                Toast.makeText(
                    context,
                    "Must enter value in \"How Is The Prescription Taken\".",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}