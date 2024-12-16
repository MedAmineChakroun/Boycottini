package com.example.boycottini

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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

class AddCategory : AppCompatActivity() {


    private lateinit var categoryImg: EditText
    private lateinit var categoryNameEditText: EditText
    private lateinit var categoryDescEditText: EditText
    private lateinit var imageUrlEditText: EditText
    private lateinit var imagePreview: ImageView
    private lateinit var addBtn: Button
    private lateinit var backIcon: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_category_activity)

        // Initialize views
        categoryImg=findViewById(R.id.image_url_input)
        categoryNameEditText = findViewById(R.id.category_name_input)
        categoryDescEditText = findViewById(R.id.category_description_input)
        imageUrlEditText = findViewById(R.id.image_url_input)
        imagePreview = findViewById(R.id.image_preview)
        addBtn = findViewById(R.id.addBtn)
        backIcon = findViewById(R.id.backIcon)

        // Back button functionality
        backIcon.setOnClickListener {
            finish()
        }

        // Listen for changes in the URL input field
        imageUrlEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val url = s.toString().trim()
                if (url.isNotEmpty()) {

                    loadImageFromUrl(url)
                } else {
                    // Reset the ImageView to a placeholder
                    imagePreview.setImageResource(R.drawable.imgplacehodler)
                }
            }
        })

        // Add Category Button functionality
        addBtn.setOnClickListener {
            val categoryName = categoryNameEditText.text.toString().trim()
            val categoryDesc = categoryDescEditText.text.toString().trim()
            val categoryImage = categoryImg.text.toString().trim()
            // Check if fields are empty
            if (categoryName.isEmpty() || categoryDesc.isEmpty()) {
                Toast.makeText(this, "Please fill out all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Call the method to send data to the server
                addCategoryToServer(categoryName, categoryDesc , categoryImage)
            }
        }
    }

    private fun addCategoryToServer(
        categoryName: String,
        categoryDesc: String,
        categoryImage: String
    ) {
        val adresseIP = getString(R.string.adresseIP) // IP address from strings.xml
        val url = "http://$adresseIP:8087/api/boycott/categories"

        // Create a JSON object with category details
        val categoryJson = JSONObject().apply {
            put("name", categoryName)
            put("description", categoryDesc)
            put("imgUrl", categoryImage)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, categoryJson,
            { response ->
                try {
                    categoryNameEditText.text.clear()
                    categoryDescEditText.text.clear()
                    categoryImg.text.clear()

                    Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    e.printStackTrace()
                    Toast.makeText(this, "Cant Parse Category", Toast.LENGTH_SHORT).show()
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

    private fun loadImageFromUrl(url: String) {
        // Use Glide to load the image
        Glide.with(this)
            .load(url) // URL of the image
            .placeholder(R.drawable.imgplacehodler) // Placeholder while loading
            .error(R.drawable.imgplacehodler) // Error image if URL is invalid
            .into(imagePreview) // Target ImageView
    }
}
