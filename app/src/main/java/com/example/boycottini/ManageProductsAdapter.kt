package com.example.boycottini

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.NetworkError
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide

class ManageProductsAdapter(
    private val context: Context,
    private var items: List<BoycottItem> // Initially pass your data here
) : RecyclerView.Adapter<ManageProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productImage: ImageView = itemView.findViewById(R.id.product_image)
        val productName: TextView = itemView.findViewById(R.id.product_name)
        val brand: TextView = itemView.findViewById(R.id.brand)
        val editButton: ImageView = itemView.findViewById(R.id.edit_button)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)
    }

    // Create new views (called by the LayoutManager)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.productName.text = item.name
        holder.brand.text = item.brand

        Glide.with(context)
            .load(item.imageUrl)  // Fallback to placeholder if URL is null
            .placeholder(R.drawable.imgplacehodler)
            .error(R.drawable.imgplacehodler)
            .into(holder.productImage)
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete ${item.name}?")
                .setPositiveButton("Yes") { dialog, _ ->
                    deleteProductFromServer(item.id, position)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
        holder.editButton.setOnClickListener {
            val intent = Intent(context,EditProduct::class.java).apply {
                putExtra("PRODUCT_ID",item.id)

            }

            context.startActivity(intent)
        }
    }
    fun updateProducts(newProducts: List<BoycottItem>) {
        items = newProducts
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }
    private fun deleteProductFromServer(productId: Long, position: Int) {
        val adresseIP = context.getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products/$productId"

        val request = object : JsonObjectRequest(
            Request.Method.DELETE, url, null,
            { response ->
                // Handle success
                //Mouch logic na3ref eme mchet

            },
            { error ->
                val updatedProducts = items.toMutableList()
                updatedProducts.removeAt(position)
                updateProducts(updatedProducts)
                Toast.makeText(context, "Product deleted successfully", Toast.LENGTH_SHORT).show()

            }
        ) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                return headers
            }
        }





        // Add the request to the queue
        Volley.newRequestQueue(context).add(request)
    }


    override fun getItemCount(): Int = items.size

}
