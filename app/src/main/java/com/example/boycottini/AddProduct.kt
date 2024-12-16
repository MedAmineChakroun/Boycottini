package com.example.boycottini

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONArray
import org.json.JSONObject

class AddProduct : AppCompatActivity() {
    private lateinit var imagePreview: ImageView
    private lateinit var imageUrlInput: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var productNameInput: EditText
    private lateinit var brandNameInput: EditText
    private lateinit var barcodeInput: EditText
    private lateinit var reasonInput: EditText
    private lateinit var proofUrlInput: EditText
    private lateinit var alternativeInput: EditText
    private lateinit var addButton: Button

    // Map to store category names and their IDs
    private val categoryMap = mutableMapOf<String, Long>()
    private var selectedCategoryId: Long? = null // To store the selected category's ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.add_product)

        // Initialization
        imagePreview = findViewById(R.id.image_preview)
        imageUrlInput = findViewById(R.id.image_url_input)
        categorySpinner = findViewById(R.id.category_spinner)
        productNameInput = findViewById(R.id.product_name_input)
        brandNameInput = findViewById(R.id.brand_name_input)
        barcodeInput = findViewById(R.id.barcode_input)
        reasonInput = findViewById(R.id.reason_input)
        proofUrlInput = findViewById(R.id.proof_url_input)
        alternativeInput = findViewById(R.id.alternative_input)
        addButton = findViewById(R.id.addBtn)

        // Back button handling
        val backIcon: ImageView = findViewById(R.id.backIcon)
        backIcon.setOnClickListener {

            val intent = Intent(this,ManageProducts::class.java)
            finish()
            startActivity(intent)

        }

        // Fetch and populate categories
        fetchCategories()

        // Handle image URL input changes
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

        // Spinner item selection listener
        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val selectedCategory = parent.getItemAtPosition(position).toString()
                selectedCategoryId = categoryMap[selectedCategory] // Get the ID of the selected category
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                selectedCategoryId = null
            }
        }

        // Add product button handling
        addButton.setOnClickListener {
            val imageUrl = imageUrlInput.text.toString()
            val productName = productNameInput.text.toString()
            val brandName = brandNameInput.text.toString()
            val barcode = barcodeInput.text.toString()
            val reason = reasonInput.text.toString()
            val proofUrl = proofUrlInput.text.toString()
            val alternative = alternativeInput.text.toString()

            if (imageUrl.isEmpty() || productName.isEmpty() || brandName.isEmpty() || barcode.isEmpty() ||
                reason.isEmpty() || proofUrl.isEmpty() || alternative.isEmpty() || selectedCategoryId == null) {
                Toast.makeText(this, "Please fill all required fields", Toast.LENGTH_SHORT).show()
            } else {
                addProductToServer(
                    imageUrl, productName, brandName, barcode,
                    reason, proofUrl, alternative, selectedCategoryId!!
                )
            }
        }
    }

    private fun loadImageFromUrl(url: String) {
        Glide.with(this)
            .load(url)
            .placeholder(R.drawable.imgplacehodler)
            .error(R.drawable.imgplacehodler)
            .into(imagePreview)
    }

    private fun addProductToServer(
        imageUrl: String,
        productName: String,
        brandName: String,
        barcode: String,
        reason: String,
        proofUrl: String,
        alternative: String,
        category_id: Long
    ) {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products?idCategory=$category_id" // Pass category_id as a query parameter
        val productJson = JSONObject().apply {
            put("name", productName)
            put("brand", brandName)
            put("imgUrl", imageUrl)
            put("barcode", barcode)
            put("raison", reason)
            put("alternativeSourceLink", proofUrl)
            put("alternative", alternative)
        }

        val request = JsonObjectRequest(
            Request.Method.POST, url, productJson,
            {
                // Reset fields after adding
                imageUrlInput.text.clear()
                productNameInput.text.clear()
                brandNameInput.text.clear()
                barcodeInput.text.clear()
                reasonInput.text.clear()
                proofUrlInput.text.clear()
                alternativeInput.text.clear()
                Toast.makeText(this, "Product Added Successfully!", Toast.LENGTH_SHORT).show()
            },
            {
                Toast.makeText(this, "Something went wrong while adding the product!", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun fetchCategories() {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/categories"

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                val categories = parseCategories(response)
                populateSpinner(categories)
            },
            {
                Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
            }
        )
        Volley.newRequestQueue(this).add(request)
    }

    private fun parseCategories(response: JSONArray): List<String> {
        val categories = mutableListOf<String>()
        for (i in 0 until response.length()) {
            val category = response.getJSONObject(i)
            val name = category.getString("name") // Category name
            val id = category.getLong("id") // Category ID
            categories.add(name)
            categoryMap[name] = id // Map category name to ID
        }
        return categories
    }

    private fun populateSpinner(categories: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }
}
