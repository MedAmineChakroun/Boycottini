package com.example.boycottini

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley

class ManageCategories : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ManageCategoriesAdapter
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_categories)

        progressBar = findViewById(R.id.progressBar2)
        recyclerView = findViewById(R.id.manage_categories_recycler)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Initialize adapter with an empty list
        adapter = ManageCategoriesAdapter(this, emptyList())
        recyclerView.adapter = adapter
        fetchCategories()

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)

        val backIcon: ImageView = findViewById(R.id.backIcon)
        backIcon.setOnClickListener {
            callback.handleOnBackPressed()
        }

        // Handle Add button click
        val addBtn = findViewById<ImageView>(R.id.add_category_button)
        addBtn.setOnClickListener {
            val intent = Intent(this, AddCategory::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh categories every time the activity is resumed
        fetchCategories()
    }

    private fun fetchCategories() {
        progressBar.visibility = View.VISIBLE
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/categories"
        val queue = Volley.newRequestQueue(this)

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                progressBar.visibility = View.GONE
                try {
                    val categories = ArrayList<Category>()
                    for (i in 0 until response.length()) {
                        val category = response.getJSONObject(i)
                        val id = category.getLong("id")
                        val name = category.getString("name")
                        val description = category.getString("description")
                        val imgUrl = category.getString("imgUrl")
                        categories.add(Category(id, name, description, imgUrl ))
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
                Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
            }
        )

        queue.add(request)
    }
}
