package com.example.boycottini

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class AddCategory : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var categoryDescEditText: EditText
    private lateinit var addBtn: Button
    private lateinit var backIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_category_activity)

        categoryNameEditText = findViewById(R.id.category_name_input)
        categoryDescEditText = findViewById(R.id.category_description_input)
        addBtn = findViewById(R.id.addBtn)
        backIcon = findViewById(R.id.backIcon)

        // Back button functionality
        backIcon.setOnClickListener {
            finish()
        }

        // Add Category Button functionality
        addBtn.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString().trim()
            val categoryDesc = categoryDescEditText.text.toString().trim()

            // Check if fields are empty
            if (categoryName.isEmpty() || categoryDesc.isEmpty()) {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Call the method to send data to the server
                addCategoryToServer(categoryName, categoryDesc)
            }
        }
    }

    private fun addCategoryToServer(categoryName: String, categoryDesc: String) {


        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/categories"

        // Create a JSON object with category details
        val categoryJson = JSONObject().apply {
            put("name", categoryName)
            put("description", categoryDesc)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, categoryJson,
            { response ->

                try {
                    categoryNameEditText.setText("")
                    categoryDescEditText.setText("")
                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()

                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error processing response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->

                error.printStackTrace()
                Toast.makeText(this, "Failed to add category", Toast.LENGTH_SHORT).show()
            }
        )

        // Add request to the queue
        Volley.newRequestQueue(this).add(request)
    }

}
