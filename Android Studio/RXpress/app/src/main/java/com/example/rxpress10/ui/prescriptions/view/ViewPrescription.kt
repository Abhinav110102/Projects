package com.example.rxpress10.ui.prescriptions.view

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.renderscript.Sampler.Value
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.sports.model.Medication
import com.example.rxpress10.MenuActivity
import com.example.rxpress10.R
import com.example.rxpress10.databinding.FragmentUsersPrescriptionsBinding
import com.example.rxpress10.databinding.FragmentViewPrescriptionBinding
import com.example.rxpress10.ui.prescriptions.users.UserPrescriptionFragment
import com.example.rxpress10.ui.prescriptions.users.UserPrescriptionFragmentArgs
import com.example.rxpress10.ui.prescriptions.users.UserPrescriptionFragmentDirections
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_prescription_fragment.*
import kotlinx.android.synthetic.main.fragment_view_prescription.*
import kotlinx.android.synthetic.main.user_card.*

/*
* This class is the fragment that displays the details of a prescription and also the edit and delete buttons
*/
class ViewPrescription : Fragment(R.layout.fragment_view_prescription) {


    // initializing binding database and authentication

    private lateinit var binding: FragmentViewPrescriptionBinding
    private lateinit var database: DatabaseReference
    private lateinit var auth: FirebaseAuth

    // initializing variables for database and transferring data between screens

    private var prescriptionName: String = ""
    private var userName: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FragmentViewPrescriptionBinding.inflate(layoutInflater)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        // setting all visibilities to gone to reduce flashing
        binding.prescriptionViewAdminstered.visibility = View.GONE
        binding.prescriptionViewFrequency.visibility = View.GONE
        binding.prescriptionViewTitle.visibility = View.GONE
        binding.prescriptionViewOther.visibility = View.GONE
        binding.prescriptionViewRxNum.visibility = View.GONE
        binding.prescriptionViewDosage.visibility = View.GONE


        val root: View = binding.root


        // Navigation arguments
        val args: ViewPrescriptionArgs by navArgs()
        userName = args.userName

        // all alphanumeric numbers will be replace the original userName if it has
        // non alphanumric chars for the database name.
        val re = "[^A-Za-z0-9 ]".toRegex()
        userName = re.replace(userName, "")
        prescriptionName = args.prescriptionName

        Log.d("PrescriptionName", prescriptionName)

        // recieve data from the database
        getData()

        // we have recieved the data from the database make views visible
        binding.prescriptionViewAdminstered.visibility = View.VISIBLE
        binding.prescriptionViewFrequency.visibility = View.VISIBLE
        binding.prescriptionViewTitle.visibility = View.VISIBLE
        binding.prescriptionViewOther.visibility = View.VISIBLE
        binding.prescriptionViewRxNum.visibility = View.VISIBLE
        binding.prescriptionViewDosage.visibility = View.VISIBLE

        // Inflate the layout for this fragment
        return root
    }

    fun getData() {
        // initializing the values to recieve from the database
        var new = 0
        var dosage = ""
        var units = ""
        var frequency = ""
        var administered = ""
        var other = ""
        var rxNum = ""

        // database name is replacing the . with nothing since database cannot have non alphanumeric values
        val databaseName = prescriptionName.replace(".", "")

        Log.d("databaseName", databaseName)


        // getting the instance of the database
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid
        database = FirebaseDatabase.getInstance()
            .getReference("users/$userId/ChildUsers/$userName/Medications/$databaseName/")

        // recieving the values of each child within the database reference
        database.get().addOnSuccessListener {
            dosage = it.child("dosage").value.toString()
            units = it.child("units").value.toString()
            frequency = it.child("frequency").value.toString()
            administered = it.child("administered").value.toString()
            other = it.child("other").value.toString()
            rxNum = it.child("rxNum").value.toString()


            // if delete button is clicked do ...
            binding.deleteBtn.setOnClickListener {
                val builder = AlertDialog.Builder(context)

                // inflating the dialog box
                val dialogLayout = layoutInflater.inflate(R.layout.confirm_delete_dialog, null)

                with(builder) {
                    setTitle("Are you sure you want to delete?")
                    setPositiveButton("Confirm") { dialog, which ->
                        // delete user to database
                        database = FirebaseDatabase.getInstance()
                            .getReference("users/$userId/ChildUsers/$userName/Medications/")

                        // navigate back to the prescription home fragment after deleting
                        val action =
                            ViewPrescriptionDirections.actionViewPrescriptionToNavPrescriptionHome()
                        binding.deleteBtn.findNavController().navigate(action)

                        // removes the child from the database
                        database.child("$databaseName").removeValue()

                        // confirm it has been deleted with a toast
                        Toast.makeText(
                            context,
                            "$prescriptionName has been deleted!",
                            Toast.LENGTH_SHORT
                        ).show()

                    }

                    // cancel does nothing
                    setNegativeButton("Cancel") { dialog, which ->
                        Log.d("PrescriptionHomeFrag", "Negative button clicked")
                    }
                    // inflating the dialog box
                    setView(dialogLayout)
                    show()
                }
            }


            // if edit button is clicked do ...
            binding.editBtn.setOnClickListener {

                // initializing arguments to be passed to edit fragment
                val action =
                    ViewPrescriptionDirections.actionViewPrescriptionToEditFragment(
                        prescriptionName,
                        dosage,
                        units,
                        frequency,
                        administered,
                        other,
                        rxNum,
                        userName
                    )
                // navigate to the edit fragment
                binding.editBtn.findNavController().navigate(action)
            }


            // inflating the views that are on the view prescription fragment
            binding.prescriptionViewTitle.text =
                getString(R.string.prescription_view_title, prescriptionName)

            binding.prescriptionViewTitle.text =
                getString(R.string.prescription_view_title, prescriptionName)

            binding.prescriptionViewDosage.text =
                getString(R.string.dosage_s, "$dosage $units")

            binding.prescriptionViewAdminstered.text = getString(
                R.string.administered_s,
                administered
            )
            binding.prescriptionViewFrequency.text =
                getString(R.string.frequency_view, frequency)

            // optional values if they are null type in "N/A" otherwise assign its value
            if (other.isNullOrEmpty())
                binding.prescriptionViewOther.text = getString(R.string.requirements_s, "N/A")
            else
                binding.prescriptionViewOther.text = getString(R.string.requirements_s, other)
            if (rxNum.isNullOrEmpty())
                binding.prescriptionViewRxNum.text = getString(R.string.rx_number_s, "N/A")
            else
                binding.prescriptionViewRxNum.text = getString(R.string.rx_number_s, rxNum)

        }
    }
}