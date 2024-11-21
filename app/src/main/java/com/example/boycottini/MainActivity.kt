package com.example.boycottini

import BoycottAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.json.JSONException

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        if (userId != null) {
            // User is logged in, now fetch the user details using the user ID
            fetchUserDetails(userId)
        } else {
            // Redirect to login screen if no user is logged in
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        val sampleBrands = listOf(
            "Apple", "Samsung", "Nike", "Adidas", "Sony", "Microsoft",
            "Coca-Cola", "Pepsi", "Toyota", "Honda", "Amazon",
            "Google", "Facebook", "Intel", "HP", "Dell",
            "BMW", "Mercedes", "Audi", "Tesla"
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = BoycottAdapter(sampleBrands)

        val suggestButton = findViewById<Button>(R.id.button3)
        suggestButton.setOnClickListener {
            val intent = Intent(this, SuggestProductActivity::class.java)
            startActivity(intent)
        }
        val categoriesButton = findViewById<Button>(R.id.button6)
        categoriesButton.setOnClickListener{
            val intent = Intent(this,CategoriesActivity::class.java)
            startActivity(intent)
        }
        val profilButton: ImageView = findViewById(R.id.account_btn)

        profilButton.setOnClickListener {

            val shareIntent = Intent(this,UserProfilActivity::class.java)
            startActivity(shareIntent)
        }

        val scanBtn = findViewById<Button>(R.id.button5)
        scanBtn.setOnClickListener{
            Toast.makeText(this,"scan is not available now!",Toast.LENGTH_SHORT).show()
        }
    }
    private fun fetchUserDetails(userId: String) {
        val url = "http://192.168.1.15:8087/api/user/users/$userId" // Assuming an endpoint to get user details by ID

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val username = response.getString("username")
                    val email = response.getString("email")
                    val password= response.getString("password")
                    val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("username", username)
                    editor.putString("email", email)
                    editor.putString("password", password)
                    editor.apply()


                } catch (e: JSONException) {
                    Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                handleLogout()
            })

        Volley.newRequestQueue(this).add(request)
    }
    private fun handleLogout() {

        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(this, MainInterfaceActivity::class.java)
        startActivity(intent)
        finish()
    }
}
