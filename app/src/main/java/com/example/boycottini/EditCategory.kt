package com.example.boycottini

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONObject

class EditCategory : AppCompatActivity() {

    private lateinit var categoryNameEditText: EditText
    private lateinit var categoryDescEditText: EditText
    private lateinit var categoryImageview: ImageView // Change to ImageView
    private lateinit var saveBtn: Button
    private lateinit var backIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_category_activity)

        // Initialize views
        categoryImageview = findViewById(R.id.category_image)
        categoryNameEditText = findViewById(R.id.category_name_input)
        categoryDescEditText = findViewById(R.id.category_description_input)
        saveBtn = findViewById(R.id.addBtn)
        backIcon = findViewById(R.id.backIcon)

        // Get data passed from the previous activity
        val categoryId = intent.getLongExtra("CATEGORY_ID", -1L)
        val categoryName = intent.getStringExtra("CATEGORY_NAME") ?: ""
        val categoryDesc = intent.getStringExtra("CATEGORY_DESC") ?: ""
        val categoryImage = intent.getStringExtra("CATEGORY_IMG") ?: ""

        // Set the data in the UI components
        categoryNameEditText.setText(categoryName)
        categoryDescEditText.setText(categoryDesc)

        // Load image using Glide
        Glide.with(this)
            .load(categoryImage)
            .error(R.drawable.brand_img_background) // Placeholder image in case of error
            .into(categoryImageview)  // Set the image into the ImageView

        // Back button listener
        backIcon.setOnClickListener {
            finish()
        }

        // Save button functionality
        saveBtn.setOnClickListener {
            val updatedName = categoryNameEditText.text.toString().trim()
            val updatedDesc = categoryDescEditText.text.toString().trim()

            // Validate inputs
            if (updatedName.isEmpty() || updatedDesc.isEmpty()) {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show()
            } else {
                // Call method to update the category on the server
                updateCategory(categoryId, updatedName, updatedDesc)
            }
        }
    }

    private fun updateCategory(categoryId: Long, updatedName: String, updatedDesc: String) {
        if (categoryId == -1L) {
            Toast.makeText(this, "Invalid category ID", Toast.LENGTH_SHORT).show()
            return
        }

        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/categories/$categoryId"

        // Create JSON object with updated category details
        val updatedCategoryJson = JSONObject().apply {
            put("name", updatedName)
            put("description", updatedDesc)
        }

        // Send PUT request to update category
        val request = JsonObjectRequest(
            Request.Method.PUT, url, updatedCategoryJson,
            { response ->
                Toast.makeText(this, "Category updated successfully", Toast.LENGTH_SHORT).show()
                // Go back to the previous screen
                finish()
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to update category", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the Volley queue
        Volley.newRequestQueue(this).add(request)
    }
}
