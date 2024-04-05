package com.example.rxpress10.ui.prescriptions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.rxpress10.R
import com.example.rxpress10.adapter.PrescriptionHomeAdapter
import com.example.rxpress10.data.PrescriptionData
import com.example.rxpress10.databinding.ActivityPrescriptionHomeFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.hour_cell.view.*


class PrescriptionHomeFragment : Fragment(com.example.rxpress10.R.layout.activity_prescription_home_fragment) {

    private lateinit var binding: ActivityPrescriptionHomeFragmentBinding

    private lateinit var database: DatabaseReference
    private lateinit var  auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrescriptionHomeFragmentBinding.inflate(layoutInflater)
//        binding.root.calenda.setOnClickListener(new View.OnClickListener(){ changeText(binding.root.calendarView3) }
//        setContentView(R.layout.activity_calendar_fragment)
//        binding.backButton.setOnClickListener { setMonthAndYear() }
        setRecyclerView()

        binding.AddingMedication.setOnClickListener {
            val action = PrescriptionHomeFragmentDirections.actionNavPrescriptionHomeToNavPrescriptions()
            binding.AddingMedication.findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val root: View = binding.root
        setRecyclerView()

        binding.AddingMedication.setOnClickListener {
            val action = PrescriptionHomeFragmentDirections.actionNavPrescriptionHomeToNavPrescriptions()
            binding.AddingMedication.findNavController().navigate(action)
        }

        return root
    }

    override fun onResume() {
        super.onResume()

        setRecyclerView()

        binding.AddingMedication.setOnClickListener {
            val action =
                PrescriptionHomeFragmentDirections.actionNavPrescriptionHomeToNavPrescriptions()
            binding.AddingMedication.findNavController().navigate(action)
        }
    }

    fun deleteMedication(medName: String){
        auth = FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/Medications")
        database.child("${medName}").removeValue()


    }

    fun setRecyclerView(){
        auth= FirebaseAuth.getInstance()
        var userId = auth.currentUser?.uid

        database = FirebaseDatabase.getInstance().getReference("users/"+userId+"/Medications")

        database.get().addOnSuccessListener {
            val meds = it.childrenCount.toInt()
            if (meds == 0){
                binding.PrescriptionHomeRecyclerView.visibility = View.GONE
            } else {
                val myDataset = PrescriptionData().loadPrescriptions()
                val recyclerView = binding.PrescriptionHomeRecyclerView
                recyclerView.adapter = PrescriptionHomeAdapter(this, myDataset)
                recyclerView.layoutManager = LinearLayoutManager(context)
                recyclerView.setHasFixedSize(true)
            }
        }.addOnFailureListener{
            Log.e("firebase", "Error getting data", it)
        }
    }

//    override fun onDestroyView() {
//        super.onDestroyView()
//        _binding = null
//    }
}