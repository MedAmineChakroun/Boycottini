package com.example.boycottini

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class Suggestions : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SuggestionsAdapter
    private var allSuggests = mutableListOf<Suggest>() // Store all suggestions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suggestions_activity)

        recyclerView = findViewById(R.id.suggestsRecyclerId)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch the suggestions and display default "To Boycott" suggestions
        fetchSuggests { suggests ->
            allSuggests = suggests.toMutableList()
            updateRecyclerView(filterSuggests("PRODUCT")) // Default filter
        }

        // Handle the back button
        val backIcon: ImageView = findViewById(R.id.backIcon)
        backIcon.setOnClickListener {
            onBackPressed()
        }

        // Handle "To Boycott" button click
        val btnToBoycott: Button = findViewById(R.id.btnToBoycott)
        btnToBoycott.setOnClickListener {
            updateRecyclerView(filterSuggests("PRODUCT"))
        }

        // Handle "Alternative" button click
        val btnAlternative: Button = findViewById(R.id.btnAlternative)
        btnAlternative.setOnClickListener {
            updateRecyclerView(filterSuggests("ALTERNATIVE"))
        }
    }

    private fun fetchSuggests(callback: (List<Suggest>) -> Unit) {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/suggests"

        val requestQueue = Volley.newRequestQueue(this)

        val jsonArrayRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val suggests = mutableListOf<Suggest>()
                for (i in 0 until response.length()) {
                    val suggestJson: JSONObject = response.getJSONObject(i)

                    val suggest = Suggest(
                        id = suggestJson.getLong("id"),
                        type = suggestJson.getString("type"),
                        brandName = suggestJson.getString("brandName"),
                        proofUrl = suggestJson.getString("proofUrl"),
                        reasonWhy = suggestJson.optString("reasonWhy", null),
                        alternativeOf = suggestJson.optString("alternativeOf", null),
                        userName = suggestJson.getString("userName"),
                        liked = suggestJson.getBoolean("liked")
                    )
                    suggests.add(suggest)
                }
                callback(suggests)
            },
            { error ->
                Toast.makeText(this, "Failed to load suggestions", Toast.LENGTH_SHORT).show()
            }
        )

        requestQueue.add(jsonArrayRequest)
    }

    private fun filterSuggests(type: String): List<Suggest> {
        return allSuggests.filter { it.type == type }
    }

    private fun updateRecyclerView(filteredSuggests: List<Suggest>) {
        adapter = SuggestionsAdapter(filteredSuggests.toMutableList(), this)
        recyclerView.adapter = adapter
    }
}
