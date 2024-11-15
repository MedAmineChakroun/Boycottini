package com.example.boycottini

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategorieAdapter(private val categories: List<String>) : RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>() {
    class CategorieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CategorieAdapter.CategorieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categorie_boycott, parent, false)
        return CategorieViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategorieAdapter.CategorieViewHolder, position: Int) {
        val categoryName = categories[position]
        holder.textView.text=categories[position]
        holder.imageView.setImageResource(R.drawable.brand_img_background)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BrandByCategorie::class.java)
            intent.putExtra("CATEGORY_NAME", categoryName) // Pass category name
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

}