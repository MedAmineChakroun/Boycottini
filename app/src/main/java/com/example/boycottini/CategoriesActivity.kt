package com.example.boycottini

import CategorieAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class CategoriesActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CategorieAdapter
    private lateinit var progressBar: ProgressBar // Declare progressBar at the class level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories)

        // Initialize progressBar
        progressBar = findViewById(R.id.progressBar)

        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = CategorieAdapter(this, emptyList())  // Pass context here
        recyclerView.adapter = adapter

        fetchCategories()

        val boycottList = findViewById<Button>(R.id.button4)
        boycottList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCategories() {
        progressBar.visibility = View.VISIBLE
        val url = "http://192.168.41.146:8087/api/boycott/categories"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                progressBar.visibility = View.GONE
                try {
                    Log.d("Volley", "Response: $response")  // Log the response for debugging
                    val categories = ArrayList<Category>()
                    for (i in 0 until response.length()) {
                        val category = response.getJSONObject(i)
                        val id = category.getString("id")  // Assuming `id` is the unique identifier
                        val name = category.getString("name")
                        categories.add(Category(id, name))
                    }

                    adapter.updateCategories(categories)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing categories", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressBar.visibility = View.GONE
                error.printStackTrace()
                Log.e("Volley", "Error: ${error.localizedMessage}")
                Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
}
