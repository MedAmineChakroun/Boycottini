package com.example.boycottini

//import BoycottAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class BrandByCategorie : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.brands_by_categorie)

        val categoryName = intent.getStringExtra("CATEGORY_NAME")

        val textView = findViewById<TextView>(R.id.textView2)
        textView.text = categoryName ?: "Selected Brand Name"

        // Correct initialization of recyclerView
        recyclerView = findViewById(R.id.recyclerView1)

        fetchProductsByCategoryId()

        val boycottList = findViewById<Button>(R.id.button4)
        boycottList.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun fetchProductsByCategoryId() {
        val categoryId = intent.getLongExtra("CATEGORY_ID", -1L)
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products/byCategory/$categoryId" // L'URL de votre API

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val products = ArrayList<BoycottItem>()

                    // Loop through the response to extract product data
                    for (i in 0 until response.length()) {
                        val product = response.getJSONObject(i)

                        // Extract name and image URL
                        val name = product.getString("name")
                        val imageUrl = product.getString("imgUrl")

                        // Create BoycottItem object
                        products.add(BoycottItem(id = product.getLong("id"), name = name, imageUrl = imageUrl))
                    }

                    // Update RecyclerView with fetched products
                    setupRecyclerView(products)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error parsing product data", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Error fetching data: ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to Volley queue
        Volley.newRequestQueue(this).add(request)
    }

    private fun setupRecyclerView(products: List<BoycottItem>) {
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = BoycottAdapter(this, products)
    }
}
