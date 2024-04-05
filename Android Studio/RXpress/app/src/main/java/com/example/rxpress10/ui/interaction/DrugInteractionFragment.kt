package com.example.rxpress10.ui.interaction


import android.annotation.SuppressLint
import android.app.Activity
import android.app.SearchManager
import android.content.Context
import android.database.Cursor
import android.database.MatrixCursor
import android.os.Bundle
import android.provider.BaseColumns
import android.util.Log
import android.view.*
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AutoCompleteTextView
import android.widget.CursorAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.rxpress10.API.Urls
import com.example.rxpress10.API.Urls.*
import com.example.rxpress10.R
import com.example.rxpress10.databinding.FragmentDrugInteractionBinding
import kotlinx.android.synthetic.main.drug_card.*
import kotlinx.android.synthetic.main.drug_card.view.*
import kotlinx.android.synthetic.main.fragment_drug_interaction.*
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener
import java.io.IOException


const val SOURCES = "&sources=ONCHigh"


/* Drug Interactions uses the search view to find a list of interactions with a drug that was entered
* for example if cymbalta was typed in it will return a list of high severity interactions with that drug*/
class DrugInteractionFragment : Fragment() {

    private val client = OkHttpClient()
    private var _binding: FragmentDrugInteractionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: DrugInteractionViewModel

    private lateinit var recyclerView: RecyclerView
    private lateinit var interactionAdapter: CardAdapter
    private var drugList = ArrayList<Drugs>()

    private var displayList = ArrayList<String>()
    private var rxcuiList = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    /* uses createOptions menu to use the search view to find suggestions and from the suggestions
    * it will pass its value to find suggestions and use the drug Interactions*/
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        getActivity()?.getWindow()
            ?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        val drugInteractionViewModel =
            ViewModelProvider(this).get(DrugInteractionViewModel::class.java)

        _binding = FragmentDrugInteractionBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root

    }

    /* allows to access the menus views like the search bar*/
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val searchItem = menu.findItem(R.id.menu_search)
        val searchView = searchItem?.actionView as androidx.appcompat.widget.SearchView

        searchView.queryHint = getString(R.string.looking_for_drug_facts)
        searchView.findViewById<AutoCompleteTextView>(androidx.databinding.library.baseAdapters.R.id.search_src_text).threshold =
            1

        val from = arrayOf(SearchManager.SUGGEST_COLUMN_TEXT_1)
        val to = intArrayOf(R.id.item_label)
        val cursorAdapter = androidx.cursoradapter.widget.SimpleCursorAdapter(
            context,
            R.layout.search_item,
            null,
            from,
            to,
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER
        )

        searchView.suggestionsAdapter = cursorAdapter

        /* listens to each key entered to do something with that query entered it will fetch  prescription Suggestions*/
        searchView.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(context, "selected $query", Toast.LENGTH_SHORT)
                return true
            }

            override fun onQueryTextChange(query: String?): Boolean {
                val cursor =
                    MatrixCursor(arrayOf(BaseColumns._ID, SearchManager.SUGGEST_COLUMN_TEXT_1))
                if (query != null) {
                    var urlQuery = replaceSpaces(query)
                    var url = Urls().getApproximateMatch + urlQuery + Urls().maxEntries
                    fetchSuggestion(url)
                }
                query?.let {
                    displayList.forEachIndexed { index, rxcui ->
                        if (rxcui.contains(query, true))
                            cursor.addRow(arrayOf(index, rxcui))
                    }
                }

                displayList.clear()

                cursorAdapter.changeCursor(cursor)
                return true
            }
        })

        /* infaltes the suggestion adapter like a recycler view and allows the individual items to be clicked*/
        searchView.setOnSuggestionListener(object :
            androidx.appcompat.widget.SearchView.OnSuggestionListener {
            override fun onSuggestionSelect(position: Int): Boolean {
                return false
            }

            @SuppressLint("Range")
            override fun onSuggestionClick(position: Int): Boolean {

                view?.let { activity?.hideKeyboard(it) }
                val cursor = searchView.suggestionsAdapter.getItem(position) as Cursor
                val selection =
                    cursor.getString(cursor.getColumnIndex(SearchManager.SUGGEST_COLUMN_TEXT_1))
                searchView.setQuery(selection, false)

                binding.interactionDescription.visibility = View.GONE

                // Do something with selection
                fetchRxcui(selection)
                drugList.clear()
                return true
            }

        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    /* hides the softkeyboard */
    fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    /* this function inflates the recycler view of the interactions*/
    fun setRecyclerView() {
        recyclerView = binding.interactionRecyclerView
        recyclerView.adapter = CardAdapter(drugList)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.setHasFixedSize(true)

    }

    /* This function binds the text from the edittext view in at the top of the screen and pass the
    * name to a url which then calls the httpRequest function and then clears the edit text view*/
    fun replaceSpaces(input: String): String {
        val rep = "%20"

        return input.replace(" ", rep)
    }

    /* This function calls the httpRequest function to get the rxcui number for the APIs to be used*/
    private fun fetchRxcui(query: String) {
        val tag = "fetchRxcui"

        val drugQueryName = replaceSpaces(query)
        var tempDrugOneUrl = Urls().getDrugs + drugQueryName

        httpRequest(tempDrugOneUrl)
    }

    /* This function takes in a url and then parses the getDrug API json file from RxNorm it will
    * then pass the rxcui number to the fetchInteractions function*/

    fun httpRequest(url: String) {
        var rxcui = ""
        val error = "Sorry, we could not find the drug that you entered please try again"
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")

                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject

                val drugGroup = jsonObject.getJSONObject("drugGroup")
                if (drugGroup.has("conceptGroup")) {
                    val conceptGroup = drugGroup.getJSONArray("conceptGroup")
                    for (i in 0
                            until conceptGroup.length()) {

                        if (conceptGroup.getJSONObject(i).has("conceptProperties")) {
                            println("${conceptGroup.getJSONObject(i)}")
                            val conceptProperties =
                                conceptGroup.getJSONObject(i)
                                    .getJSONArray("conceptProperties") as JSONArray
                            for (j in 0 until conceptProperties.length()) {
                                //TODO Put rxcui's into an array and assign the drugs rxcui

                                /* Recieved the rxcui of the drug and will pass it as an arg into fetchInteractions*/
                                rxcui = conceptProperties.getJSONObject(j).getString("rxcui")
                                Log.d("RXCUI", rxcui)
                                if (j == conceptProperties.length() - 1)
                                    fetchInteractions(rxcui)
                            }
                        }

                    }
                } else {
                    if (drugGroup.getString("name").toString().isNotEmpty()) {
                        activity?.runOnUiThread(Runnable {
                            kotlin.run {
                                println(drugGroup.getString("name"))
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        })
    }


    /* This function takes in the rxcui and to parse the getInteraction api from drugInteractions
    * provided by RxNorm this will then parse the json file and display the two drug names that have
    * an interaction , the severity of that interaction and also the description of the interaction
    * for more information for the user*/
    fun fetchInteractions(rxcui: String) {
        var drugOne = ""
        var drugTwo = ""
        var drugOneSyn = ""
        var drugTwoSyn = ""
        var severity_ob = ""
        var interaction_ob = ""
        val tag = "fetchInteractions"

        Log.i(tag, "WELCOME TO FETCHINTERACTIONS")
        val url = Urls().getInteractions + rxcui + SOURCES
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @SuppressLint("SuspiciousIndentation")
            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")


                if (!response.isSuccessful) throw IOException("Unexpected code $response")

                /* Parsing the json file each object at a time format is
                * {
                *   "interactionTypeGroup": [
                *       {
                *           "interactionType": [
                *               {
                *                   "interactionPair": [
                *                       {
                *                           "interactionConcept": [
                *                               {
                *                                   l = 0 "minConceptItem": {...,"name": "name",...},
                *                                   l = 1 "minConceptItem": {...,"name": "name",...}
                *                               }
                *                           ],
                *                           "severity": String,
                *                           "description": String
                *                       },
                *                   ],
                *               },
                *           ],
                *       },
                *   ],
                * }*/
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject
                if (jsonObject.has("interactionTypeGroup")) {
                    val interactionTypeGroup = jsonObject.getJSONArray("interactionTypeGroup")
                    println("${interactionTypeGroup.toString()}")

                    // i is not incrementing causing recycler view to do duplicate entries

                    for (i in 0 until interactionTypeGroup.length()) {
                        Log.i("InteractionTypeGroup", "$i")

                        val interactionType =
                            interactionTypeGroup.getJSONObject(i)
                                .getJSONArray("interactionType") as JSONArray
                        for (j in 0 until interactionType.length()) {

                            Log.i("InteractionType", "$j")


                            val interactionPair =
                                interactionType.getJSONObject(j)
                                    .getJSONArray("interactionPair") as JSONArray

                            /* HOLDS severity and description*/
                            for (k in 0 until interactionPair.length()) {

                                Log.i("InteractionPair", "$k")


                                activity?.runOnUiThread(Runnable
                                {
                                    kotlin.run {
                                        val interactionConcept =
                                            interactionPair.getJSONObject(k)
                                                .getJSONArray("interactionConcept") as JSONArray
                                        for (l in 0 until interactionConcept.length()) {

                                            Log.i("InteractionConcept", "$l")

                                            val minConceptItem = interactionConcept.getJSONObject(l)
                                                .getJSONObject("minConceptItem") as JSONObject
                                            var name = minConceptItem.getString("name")


                                            // TODO call fetchSynonym
                                            if (l == 0) {
                                                drugOneSyn = name
                                                drugOne = getString(R.string.drug1_s, name)

                                            } else {
                                                drugTwoSyn = name
                                                drugTwo = getString(R.string.drug2_s, name)
                                            }
                                        }
                                        val severity = interactionPair.getJSONObject(i)
                                            .getString("severity")
                                        severity_ob = getString(R.string.severity_s, severity)

                                        val description = interactionPair.getJSONObject(i)
                                            .getString("description")
                                        getString(R.string.interaction, description)

                                        interaction_ob =
                                            getString(R.string.interaction, description)

                                        fetchSynonymOne(
                                            drugOneSyn,
                                            drugTwoSyn,
                                            severity_ob,
                                            interaction_ob
                                        )
                                    }
                                })
                            }
                        }
                    }
                } else {
                    activity?.runOnUiThread(Runnable {
                        kotlin.run {

                            val drugs = Drugs(
                                "drug not available",
                                "drug not available",
                                "drug not available",
                                "drug not available"
                            )
                            drugList.add(drugs)
                            binding.interactionRecyclerView.visibility = View.GONE
                            setRecyclerView()
                            binding.interactionDescription.visibility = View.VISIBLE
                            Toast.makeText(
                                context,
                                "There are no severe interactions with that drug",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
                }
            }
        })
    }

    /* This function takes in drugOne, drugTwo, severity, and interactions. It will use the getDrugs
    * Json file from RXnorm to find the synonym of a drug that was found from the drug interaction API
    * it will then call fetchSynonymTwo to find its synonym*/
    fun fetchSynonymOne(drugOne: String, drugTwo: String, severity: String, interaction: String) {
        var synonym = ""
        var synonymOne = ""
        val url = Urls().getDrugs + drugOne
        val error = "Sorry, we could not find the drug that you entered please try again"

        Log.d("URL", url)
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")

                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject

                val drugGroup = jsonObject.getJSONObject("drugGroup")
                if (drugGroup.has("conceptGroup")) {
                    val conceptGroup = drugGroup.getJSONArray("conceptGroup")
                    for (i in 0
                            until conceptGroup.length()) {

                        if (conceptGroup.getJSONObject(i).has("conceptProperties")) {
                            println("${conceptGroup.getJSONObject(i)}")
                            val conceptProperties =
                                conceptGroup.getJSONObject(i)
                                    .getJSONArray("conceptProperties") as JSONArray
                            Log.d("PropertiesLength", "${conceptProperties.length()}")

                            activity?.runOnUiThread(Runnable
                            {
                                kotlin.run {
                                    for (j in 0 until conceptProperties.length()) {
                                        //TODO Put rxcui's into an array and assign the drugs rxcui

                                        /* Recieved the rxcui of the drug and will pass it as an arg into fetchInteractions*/
                                        if (j == conceptProperties.length() - 1) {
                                            synonym =
                                                conceptProperties.getJSONObject(j)
                                                    .getString("synonym")
                                            Log.d("Drug One", drugOne)

                                            val mystring: String = "the quick brown fox"
                                            var arr: List<String> = synonym.split(" ")

                                            var firstWord = arr[0]
                                            Log.d("FirstWord", firstWord)

                                            if (!drugOne.equals(firstWord)) {
                                                if (firstWord.isNullOrEmpty()) {
                                                    synonymOne =
                                                        getString(R.string.drug1_s, firstWord)
                                                    fetchSynonymTwo(
                                                        "",
                                                        drugTwo,
                                                        severity,
                                                        interaction,
                                                        firstWord
                                                    )
                                                    Log.d("No synonym", synonymOne)
                                                } else {
                                                    if (!firstWord.contains("[0-9]".toRegex())) {
                                                        Log.d("Synonym", firstWord)
                                                        var newDrug = "$drugOne ($firstWord)"
                                                        synonymOne =
                                                            getString(R.string.drug1_s, newDrug)

                                                        var arrSyn: List<String> =
                                                            synonymOne.split("(")
                                                        var rawSynOne = arrSyn[1]
                                                        Log.d("Raw Syn One", rawSynOne)

                                                        fetchSynonymTwo(
                                                            synonymOne,
                                                            drugTwo,
                                                            severity,
                                                            interaction,
                                                            firstWord
                                                        )
                                                    } else {
                                                        Log.d("Has Number", "Has Number")
                                                        val thirdWord = arr[2]
                                                        val newDrugNum = "$drugOne ($thirdWord)"
                                                        synonymOne =
                                                            getString(
                                                                R.string.drug1_s,
                                                                newDrugNum
                                                            )
                                                        fetchSynonymTwo(
                                                            synonymOne,
                                                            drugTwo,
                                                            severity,
                                                            interaction,
                                                            firstWord
                                                        )
                                                    }
                                                }

                                            } else if (drugOne.equals(firstWord)) {
                                                synonymOne = getString(R.string.drug1_s, firstWord)

                                                Log.d("Redundant", synonymOne)
                                                fetchSynonymTwo(
                                                    synonymOne,
                                                    drugTwo,
                                                    severity,
                                                    interaction,
                                                    ""
                                                )
                                            }
                                        }
                                    }
                                }
                            })
                        }
                    }
                } else {
                    if (drugGroup.getString("name").toString().isNotEmpty()) {
                        activity?.runOnUiThread(Runnable {
                            kotlin.run {
                                println(drugGroup.getString("name"))
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        })
    }

    /* This function takes in drugOne, drugTwo, severity, interactions, and synonymOne. It will use the getDrugs
    * Json file from RXnorm to find the synonym of a drug that was found from the drug interaction API
    * it will then call populateDrugList to populate the list for the recycler view*/
    fun fetchSynonymTwo(
        drugOne: String,
        drugTwo: String,
        severity: String,
        interaction: String,
        synonymOne: String
    ) {
        var synonym = ""
        var synonymTwo = ""
        var rawSynTwo = ""

        println(drugOne)

        Log.d("Split?", synonymOne)

        val url = Urls().getDrugs + drugTwo
        val error = "Sorry, we could not find the drug that you entered please try again"

        Log.d("URL", url)
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")

                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject

                val drugGroup = jsonObject.getJSONObject("drugGroup")
                if (drugGroup.has("conceptGroup")) {
                    val conceptGroup = drugGroup.getJSONArray("conceptGroup")
                    for (i in 0
                            until conceptGroup.length()) {

                        if (conceptGroup.getJSONObject(i).has("conceptProperties")) {
                            println("${conceptGroup.getJSONObject(i)}")
                            val conceptProperties =
                                conceptGroup.getJSONObject(i)
                                    .getJSONArray("conceptProperties") as JSONArray
                            Log.d("PropertiesLength", "${conceptProperties.length()}")

                            activity?.runOnUiThread(Runnable
                            {
                                kotlin.run {
                                    for (j in 0 until conceptProperties.length()) {
                                        //TODO Put rxcui's into an array and assign the drugs rxcui

                                        /* Recieved the rxcui of the drug and will pass it as an arg into fetchInteractions*/
                                        if (j == conceptProperties.length() - 1) {
                                            synonym =
                                                conceptProperties.getJSONObject(j)
                                                    .getString("synonym")
                                            Log.d("Drug Two", drugTwo)
                                            var arr: List<String> = synonym.split(" ")

                                            var firstWord = arr[0]


                                            when (j) {

                                                0 -> {
                                                    setRecyclerView()
                                                    binding.interactionRecyclerView.visibility =
                                                        View.VISIBLE
                                                }
                                            }

                                            if (!drugTwo.equals(firstWord)) {
                                                if (firstWord.isNullOrEmpty()) {

                                                    synonymTwo = ""
                                                    Log.d("DrugTwoEmpty", synonymTwo)
                                                    rawSynTwo = "no syn"
                                                } else {
                                                    if (!firstWord.contains("[0-9]".toRegex())) {
                                                        Log.d("SynonymD2", firstWord)
                                                        firstWord = "$drugTwo ($firstWord)"
                                                        synonymTwo =
                                                            getString(
                                                                R.string.drug2_s,
                                                                firstWord
                                                            )
                                                        Log.d("Split this", synonymTwo)
                                                        Log.d(
                                                            "DrugTwoSplit",
                                                            "Drug 2: $drugTwo"
                                                        )

                                                        val arr = firstWord.split("(")
                                                        rawSynTwo = arr[1]
                                                        rawSynTwo = rawSynTwo.replace(")", "")

                                                        Log.d("Synonym 2", rawSynTwo)
                                                    } else {
                                                        Log.d("Has Number", "Has Number")
                                                        val thirdWord = arr[2]
                                                        val newDrugNum = "$drugTwo ($thirdWord)"
                                                        synonymTwo =
                                                            getString(
                                                                R.string.drug2_s,
                                                                newDrugNum
                                                            )
                                                        val arr = newDrugNum.split("(")
                                                        rawSynTwo = arr[1]
                                                        rawSynTwo = rawSynTwo.replace(")", "")
                                                    }
                                                }

                                            } else if (drugTwo.equals(firstWord)) {


                                                synonymTwo = getString(R.string.drug2_s, firstWord)

                                                Log.d("drugTwo", drugTwo)

                                                rawSynTwo = ""
                                            }
                                            populateDrugList(
                                                drugOne,
                                                synonymTwo,
                                                severity,
                                                interaction,
                                                synonymOne,
                                                rawSynTwo
                                            )
                                        }
                                    }
                                }
                            })
                        }
                    }
                } else {
                    if (drugGroup.getString("name").toString().isNotEmpty()) {
                        activity?.runOnUiThread(Runnable {
                            kotlin.run {
                                println(drugGroup.getString("name"))
                                Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }
        })
    }


    /* Uses the getApproxMatch API From RXNorm to get appropriate suggestions adds them to a list*/
    fun fetchSuggestion(url: String): MutableList<String> {
        val error = "Sorry, we could not find what you were looking for please try again"
        val request = okhttp3.Request.Builder().url(url).build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            override fun onResponse(call: Call, response: Response) {
                println("${response.code()}")
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val jsonObject = JSONTokener(response.body()?.string()).nextValue() as JSONObject

                val approximateGroup = jsonObject.getJSONObject("approximateGroup")

                if (approximateGroup.has("candidate")) {
                    val candidate = approximateGroup.getJSONArray("candidate")
                    for (i in 0
                            until candidate.length()) {

                        activity?.runOnUiThread(Runnable {
                            kotlin.run {
                                if (candidate.getJSONObject(i).getString("source")
                                        .equals("RXNORM", true)
                                )
                                // if they have a name add to array.
                                {
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
                } else {
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

        Log.i("Suggestion List", "$displayList")
        return displayList
    }

    /* This function takes in the drugs, severity, interactions and its synonyms in order to create
    * a drug object it then checks if the drugs names and synonyms are not empty to ensure the drug
    * displays a synonym then it will add the drug to the list
    * */
    fun populateDrugList(
        drugOne: String,
        drugTwo: String,
        severity: String,
        interaction: String,
        synOne: String,
        synTwo: String
    ) {

        var drug = Drugs(
            drugOne, drugTwo, severity, interaction
        )

        Log.d("PDL SYN ONE", synOne)
        Log.d("PDL SYN TWO", synTwo)

        if (drugOne.isNotEmpty() && drugTwo.isNotEmpty() && synOne.isNotEmpty() && synTwo.isNotEmpty()) {
            drugList.add(drug)
        }



        Log.i("populateDrugList", "$drugList")
    }

    /* this function adds the rxcui number to one list and the name to another since the get*/
    fun populateSuggestionList(rxcui: String, name: String, score: String): ArrayList<String> {
        val suggestion = Suggestions(rxcui, name, score)
        displayList.add(name)

        rxcuiList.add(rxcui)
        return displayList
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}