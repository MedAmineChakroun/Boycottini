package com.example.boycottini

import BoycottAdapter
import android.content.Intent
import android.os.Bundle
import android.widget.Adapter
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BrandByCategorie:AppCompatActivity (){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.brands_by_categorie)

        val categoryName = intent.getStringExtra("CATEGORY_NAME")

        val textView = findViewById<TextView>(R.id.textView2)
        textView.text = categoryName ?: "Selected Brand Name"

        val sampleBrands = listOf(
            "Apple", "Samsung", "Nike", "Adidas", "Sony", "Microsoft",
            "Coca-Cola", "Pepsi", "Toyota", "Honda", "Amazon",
            "Google", "Facebook", "Intel", "HP", "Dell",
            "BMW", "Mercedes", "Audi", "Tesla"
        )
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView1)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = BoycottAdapter(sampleBrands)

        val boycottList= findViewById<Button>(R.id.button4)
        boycottList.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }
}