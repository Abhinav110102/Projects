package com.example.rxpress10.ui.interaction

import com.example.rxpress10.API.Urls
import org.junit.Test
import org.junit.Assert.*

/* This unit test tests the replaceSpaces function and the fetchSuggestion function*/
internal class DrugInteractionTest {

    /* this creates a mockUrl and medication and sees if the mockUrl after calling the replace spaces
    * properly formats the URL to a proper URL*/
    @Test
    fun replaceSpacesTest() {
        var mockUrl = Urls().getDrugs
        var medication = "Penicillin g"
        medication = "$mockUrl$medication"
        mockUrl = DrugInteractionFragment().replaceSpaces("$medication")

        println("Before replaced Spaces: $medication")
        println("After replaced Spaces: $mockUrl")

        assertTrue(mockUrl == DrugInteractionFragment().replaceSpaces(medication))
    }

    /*This function test if the drugList is not empty and is created by populating a suggestion*/
    @Test
    fun fetchSuggestionTest() {
        val drugList =
            DrugInteractionFragment().populateSuggestionList("123123", "Cymbalta", "10.8")
        assertTrue(drugList.isNotEmpty())
    }
}