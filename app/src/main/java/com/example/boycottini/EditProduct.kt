package com.example.boycottini

import android.annotation.SuppressLint
import android.content.Intent
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

class EditProduct : AppCompatActivity() {
    // lateinit variables for UI elements
    private lateinit var backIcon: ImageView
    private lateinit var imagePreview: ImageView
    private lateinit var imageUrlInput: EditText
    private lateinit var productNameInput: EditText
    private lateinit var brandNameInput: EditText
    private lateinit var barcodeInput: EditText
    private lateinit var reasonInput: EditText
    private lateinit var proofUrlInput: EditText
    private lateinit var alternativeInput: EditText
    private lateinit var saveButton: Button

    // Variable to store the category ID
    private var categoryId: Long = 1 // Default to 1

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.edit_product)

        // Initialize UI elements
        backIcon = findViewById(R.id.backIcon)
        imagePreview = findViewById(R.id.image_preview)
        imageUrlInput = findViewById(R.id.image_url_input)
        productNameInput = findViewById(R.id.product_name_input)
        brandNameInput = findViewById(R.id.brand_name_input)
        barcodeInput = findViewById(R.id.barcode_input)
        reasonInput = findViewById(R.id.reason_input)
        proofUrlInput = findViewById(R.id.proof_url_input)
        alternativeInput = findViewById(R.id.alternative_input)
        saveButton = findViewById(R.id.addBtn)

        // Back icon functionality
        backIcon.setOnClickListener {
            finish()
        }

        // Fetch product details by ID
        fetchProductById()

        // Save changes when save button is clicked
        saveButton.setOnClickListener {
            saveChanges()
        }
        imageUrlInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {
                val url = s.toString().trim()
                if (url.isNotEmpty()) {
                    loadImageFromUrl(url)
                } else {
                    imagePreview.setImageResource(R.drawable.imgplacehodler)
                }
            }
        })
    }
    private fun loadImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.imgplacehodler)
            .error(R.drawable.imgplacehodler)
            .into(imagePreview)
    }
    private fun fetchProductById() {
        val productID = intent.getLongExtra("PRODUCT_ID", -1L)
        val adresseIP = getString(R.string.adresseIP)

        val url = "http://$adresseIP:8087/api/boycott/products/$productID"
        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                populateFields(response)
            },
            { error ->
                Toast.makeText(this, "Error fetching product: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Add request to the Volley request queue
        Volley.newRequestQueue(this).add(request)
    }

    private fun populateFields(product: JSONObject) {
        try {
            productNameInput.setText(product.optString("name", ""))
            brandNameInput.setText(product.optString("brand", ""))
            barcodeInput.setText(product.optString("barcode", ""))
            reasonInput.setText(product.optString("raison", ""))
            proofUrlInput.setText(product.optString("alternativeSourceLink", ""))
            alternativeInput.setText(product.optString("alternative", ""))
            imageUrlInput.setText(product.optString("imgUrl", ""))
            categoryId = product.optJSONObject("category")?.optLong("id", -1L) ?: -1L


            // Load image preview
            val imageUrl = product.optString("imgUrl", "")
            Glide.with(this).load(imageUrl).error(R.drawable.imgplacehodler).into(imagePreview)


        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error parsing product details: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveChanges() {
        // Get the values from the input fields
        val productName = productNameInput.text.toString().trim()
        val brandName = brandNameInput.text.toString().trim()
        val barcode = barcodeInput.text.toString().trim()
        val reason = reasonInput.text.toString().trim()
        val proofUrl = proofUrlInput.text.toString().trim()
        val alternative = alternativeInput.text.toString().trim()
        val imageUrl = imageUrlInput.text.toString().trim()

        // Validate that none of the fields are empty using a single condition
        if (productName.isEmpty() || brandName.isEmpty() || barcode.isEmpty() || reason.isEmpty() ||
            proofUrl.isEmpty() || alternative.isEmpty() || imageUrl.isEmpty()) {
            Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        // If all fields are valid, proceed with saving the changes
        val productID = intent.getLongExtra("PRODUCT_ID", -1L)
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products/$productID?idCategory=$categoryId"  // Add the category ID as a query parameter

        // Create a JSON object for the updated product details
        val updatedProduct = JSONObject().apply {
            put("name", productName)
            put("brand", brandName)
            put("barcode", barcode.toLong())
            put("raison", reason)
            put("alternativeSourceLink", proofUrl)
            put("alternative", alternative)
            put("imgUrl", imageUrl)
        }

        // Create a PUT request
        val request = JsonObjectRequest(
            Request.Method.PUT, url, updatedProduct,
            { response ->
                Toast.makeText(this, "Product updated successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this,ManageProducts::class.java)
                finish()
                startActivity(intent)
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Failed to update product: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the Volley queue
        Volley.newRequestQueue(this).add(request)
    }

}
