package com.example.boycottini


import CategorieAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories)

        recyclerView = findViewById(R.id.recyclerView1)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        adapter = CategorieAdapter(emptyList())
        recyclerView.adapter = adapter


        fetchCategories()


        val boycottList = findViewById<Button>(R.id.button4)
        boycottList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchCategories() {
        val url = "http://192.168.1.15:8087/api/boycott/categories"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    Log.d("Volley", "Response: $response") // Log the response for debugging
                    val categories = ArrayList<String>()
                    for (i in 0 until response.length()) {
                        val category = response.getJSONObject(i)
                        val name = category.getString("name")
                        categories.add(name)
                    }

                    adapter.updateCategories(categories)
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing categories", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Log.e("Volley", "Error: ${error.localizedMessage}")
                Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
            }
        )


        queue.add(request)
    }
}
