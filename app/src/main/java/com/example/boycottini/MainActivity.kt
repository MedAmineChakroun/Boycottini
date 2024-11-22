package com.example.boycottini

import BoycottAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var hamburgerIcon: ImageView
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        drawerLayout = findViewById(R.id.drawerLayout)
        hamburgerIcon = findViewById(R.id.hamburger_icon)
        recyclerView = findViewById(R.id.recyclerView)

        // Handle user session
        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        if (userId != null) {
            // User is logged in, fetch user details
            fetchUserDetails(userId)
        } else {
            // Redirect to login screen if no user is logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Set up RecyclerView with sample data
        setupRecyclerView()

        // Set up button click listeners
        setupButtonListeners()

        // Set up drawer toggle
        /*hamburgerIcon.setOnClickListener {
            if (drawerLayout.isDrawerOpen(findViewById(R.id.navigation_view))) {
                drawerLayout.closeDrawer(findViewById(R.id.navigation_view))
            } else {
                drawerLayout.openDrawer(findViewById(R.id.navigation_view))
            }
        }*/
    }

    private fun setupRecyclerView() {
        val sampleBrands = listOf(
            "Apple", "Samsung", "Nike", "Adidas", "Sony", "Microsoft",
            "Coca-Cola", "Pepsi", "Toyota", "Honda", "Amazon",
            "Google", "Facebook", "Intel", "HP", "Dell",
            "BMW", "Mercedes", "Audi", "Tesla"
        )
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = BoycottAdapter(sampleBrands)
    }

    private fun setupButtonListeners() {
        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent(this, SuggestProductActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.button6).setOnClickListener {
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.account_btn).setOnClickListener {
            val shareIntent = Intent(this, UserProfilActivity::class.java)
            startActivity(shareIntent)
        }
        findViewById<Button>(R.id.button5).setOnClickListener {
            Toast.makeText(this, "Scan is not available now!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserDetails(userId: String) {
        val url = "http://192.168.1.15:8087/api/user/users/$userId"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val username = response.getString("username")
                    val email = response.getString("email")
                    val password = response.getString("password")

                    val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("username", username)
                        putString("email", email)
                        putString("password", password)
                        apply()
                    }

                } catch (e: JSONException) {
                    Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                handleLogout()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun handleLogout() {
        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        val intent = Intent(this, MainInterfaceActivity::class.java)
        startActivity(intent)
        finish()
    }
}
