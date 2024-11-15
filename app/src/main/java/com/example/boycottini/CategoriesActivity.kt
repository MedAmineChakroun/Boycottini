package com.example.boycottini

import BoycottAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CategoriesActivity :AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.categories)

        val recyclerView= findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView.layoutManager=GridLayoutManager(this,2)

        val categories = listOf(
            "Electronics", "Groceries", "Clothing", "Beauty", "Home",
            "Toys", "Books", "Sports", "Automotive", "Furniture",
            "Jewelry", "pets", "Gardening", "Office", "Footwear",
            "Accessories", "Kitchenware", "Health Products", "Baby Products", "Tools",
            "Music Instruments", "Stationery", "Gaming", "Travel Gear",
            "Beverages", "Snacks", "Outdoor Equipment", "Arts & Crafts"
        )


        recyclerView.adapter=CategorieAdapter(categories)


        val boycottList= findViewById<Button>(R.id.button4)
        boycottList.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }

}