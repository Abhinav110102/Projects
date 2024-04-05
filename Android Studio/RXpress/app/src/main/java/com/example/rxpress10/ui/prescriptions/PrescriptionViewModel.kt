package com.example.rxpress10.ui.prescriptions

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rxpress10.R

class PrescriptionViewModel : ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is Prescription Fragment"
    }
    val text: LiveData<String> = _text
}