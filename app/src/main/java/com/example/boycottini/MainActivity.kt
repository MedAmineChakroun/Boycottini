package com.example.boycottini

import BoycottAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Sample data
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
        val shareButton: FloatingActionButton = findViewById(R.id.floatingActionButton)

        // Set click listener on the share button
        shareButton.setOnClickListener {
            // Create an implicit intent to share the app link
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.type = "text/plain"

            // The text you want to share, e.g., app link or message
            val appLink = "https://play.google.com/store/apps/details?id=com.example.myapp" // Replace with your app's URL
            shareIntent.putExtra(Intent.EXTRA_TEXT, "Check out this awesome app: $appLink")

            // Start the activity to share
            startActivity(Intent.createChooser(shareIntent, "Share app via"))
        }
    }
}
