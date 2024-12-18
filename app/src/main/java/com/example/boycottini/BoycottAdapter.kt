package com.example.boycottini

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boycottini.R

class BoycottAdapter(private val context: Context, private var products: List<BoycottItem>) :
    RecyclerView.Adapter<BoycottAdapter.BoycottViewHolder>() {

    class BoycottViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoycottViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_boycott, parent, false)
        return BoycottViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoycottViewHolder, position: Int) {
        val product = products[position]
        holder.textView.text = product.name

        Glide.with(context)
            .load(product.imageUrl)
            .error(R.drawable.brand_img_background)
            .into(holder.imageView)

        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("PRODUCT_ID", product.id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = products.size

    fun updateProducts(newProducts: List<BoycottItem>) {
        products = newProducts
        notifyDataSetChanged()
    }
}