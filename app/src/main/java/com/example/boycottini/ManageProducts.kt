package com.example.boycottini

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ManageProducts : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var categorySpinner: Spinner
    private lateinit var addbtn: ImageView
    private var categoriesList: List<Category> = emptyList()

    private var selectedCategoryId: Long? = null // To hold the selected category ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.manage_products)

        addbtn = findViewById(R.id.add_product_button)
        addbtn.setOnClickListener {
            val intent = Intent(this, AddProduct::class.java)
            startActivity(intent)
            finish()
        }

        // Initializations
        progressBar = findViewById(R.id.progressBar2)
        recyclerView = findViewById(R.id.products_recycler_view)
        categorySpinner = findViewById(R.id.select_options_spinner)

        // Handle back press
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

        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // Fetch categories and populate Spinner
        fetchCategories()

        // Set Spinner item selection listener to fetch products by selected category
        categorySpinner.setOnItemSelectedListener(object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedCategory = categoriesList[position]
                selectedCategoryId = selectedCategory.id // Store selected category ID
                fetchProductsByCategory(selectedCategory.id) // Fetch products by selected category
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        })
    }



    private fun fetchCategories() {
        progressBar.visibility = View.VISIBLE

        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/categories"

        val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                categoriesList = parseCategoriesResponse(response)

                // Set up Spinner adapter with category names
                val spinnerAdapter = ArrayAdapter(
                    this,
                    android.R.layout.simple_spinner_item,
                    categoriesList.map { it.name }  // Only the name for display
                )
                spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                categorySpinner.adapter = spinnerAdapter

                progressBar.visibility = View.GONE
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to fetch categories", Toast.LENGTH_SHORT).show()
            })

        // Adding the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonRequest)
    }

    private fun parseCategoriesResponse(response: JSONArray): List<Category> {
        val categoryList = mutableListOf<Category>()

        for (i in 0 until response.length()) {
            val categoryObj = response.getJSONObject(i)
            val category = Category(
                id = categoryObj.getLong("id"),
                name = categoryObj.getString("name"),
                description = categoryObj.getString("description"),
                imgUrl = categoryObj.getString("imgUrl")
            )
            categoryList.add(category)
        }

        return categoryList
    }

    private fun fetchProductsByCategory(categoryId: Long) {
        progressBar.visibility = View.VISIBLE
        recyclerView.adapter = null  // Clear RecyclerView before loading new data

        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products/byCategory/$categoryId"

        val jsonRequest = JsonArrayRequest(Request.Method.GET, url, null,
            { response ->
                val productsList = if (response.length() > 0) {
                    parseProductsResponse(response)
                } else {
                    emptyList<BoycottItem>() // No products, return empty list
                }

                // Set the adapter with the products list
                val adapter = ManageProductsAdapter(this, productsList.toMutableList())
                recyclerView.adapter = adapter
                adapter.updateProducts(productsList)

                progressBar.visibility = View.GONE

                if (productsList.isEmpty()) {
                    Toast.makeText(this, "No products found for this category", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                progressBar.visibility = View.GONE
                Toast.makeText(this, "Failed to fetch products", Toast.LENGTH_SHORT).show()
            })

        // Adding the request to the RequestQueue
        Volley.newRequestQueue(this).add(jsonRequest)
    }

    private fun parseProductsResponse(response: JSONArray): List<BoycottItem> {
        val productList = mutableListOf<BoycottItem>()

        for (i in 0 until response.length()) {
            val productObj = response.getJSONObject(i)
            val product = BoycottItem(
                id = productObj.getLong("id"),
                name = productObj.getString("name"),
                brand = productObj.getString("brand"),
                imageUrl = productObj.getString("imgUrl")
            )
            productList.add(product)
        }

        return productList
    }
}
