package com.example.rxpress10.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.rxpress10.LoginActivity
import com.example.rxpress10.MainActivity
import com.example.rxpress10.R
import com.example.rxpress10.databinding.ActivitySettingsFragmentBinding
import com.example.rxpress10.databinding.FragmentSettingsBinding
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings_fragment.*
import kotlinx.android.synthetic.main.activity_settings_fragment.view.*


class SettingsFragment : Fragment(R.layout.activity_settings_fragment) {

    private var _binding: FragmentSettingsBinding? = null
    private lateinit var binding: ActivitySettingsFragmentBinding

    // This property is only valid between onCreateView and
    // onDestroyView.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsFragmentBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
//        val settingsViewModel =
//            ViewModelProvider(this).get(SettingsViewModel::class.java)

//        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
//        val root: View = binding.root
        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
//            activity?.supportFragmentManager?.popBackStack()
            val intent = Intent(getActivity(), LoginActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)

            Toast.makeText(activity, "Logout Successfully.", Toast.LENGTH_LONG).show()
        }

        binding.btnProfile.setOnClickListener {
            profileClicked()
        }
        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner) {
        //    textView.text = it
        //}
//        settingsViewModel.text.observe(viewLifecycleOwner) {
//            //          textView.text = it
//        }
        return binding.root
    }

    fun profileClicked() {
        val action = SettingsFragmentDirections.actionNavSettingsToProfileFragment()

        binding.btnProfile.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}