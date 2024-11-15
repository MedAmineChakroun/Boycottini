package com.example.boycottini

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView


class AlternativeBrandsAdapter(
    private val brandImages: List<Int>
) : RecyclerView.Adapter<AlternativeBrandsAdapter.BrandViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_alternative_brand, parent, false)
        return BrandViewHolder(view)
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val imageRes = brandImages[position]
        holder.brandImageView.setImageResource(imageRes)
    }

    override fun getItemCount(): Int = brandImages.size

    class BrandViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandImageView: ImageView = itemView.findViewById(R.id.brandImageView)
    }
}
