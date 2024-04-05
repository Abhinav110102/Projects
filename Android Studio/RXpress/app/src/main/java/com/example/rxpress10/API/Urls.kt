package com.example.rxpress10.API

class Urls {
    val getDrugs = "https://rxnav.nlm.nih.gov/REST/drugs.json?name="
    val getInteractions = "https://rxnav.nlm.nih.gov/REST/interaction/interaction.json?rxcui="
    // TODO getSpelling
    val getSpelling = ""

    val getApproximateMatch = "https://rxnav.nlm.nih.gov/REST/approximateTerm.json?term="
    val maxEntries = "&maxEntries=3"

    val key = "&key=7c531dbe-d942-4837-8087-627b44e4f0d1"
}