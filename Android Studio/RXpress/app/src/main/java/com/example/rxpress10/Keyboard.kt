package com.example.rxpress10

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

open class Keyboard {
    fun Context.hideKeyboard(view: View){
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}