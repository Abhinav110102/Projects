package com.example.rxpress10.ui.faq.model
//This is a model class for each individual faq in the recycler view.
class FAQ (
    val name : String = "",
    val description : String = "",
    val link: String = "",
    var expand : Boolean = false
)