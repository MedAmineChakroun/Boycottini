package com.example.boycottini

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

import com.bumptech.glide.Glide  // Import Glide

class ManageCategoriesAdapter(
    private val context: Context,
    private var categories: List<Category>
) : RecyclerView.Adapter<ManageCategoriesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryImage: ImageView = view.findViewById(R.id.category_image)
        val categoryName: TextView = view.findViewById(R.id.category_name)
        val categoryDescription: TextView = view.findViewById(R.id.category_description)
        val editButton: ImageButton = view.findViewById(R.id.edit_button)
        val deleteButton: ImageButton = view.findViewById(R.id.delete_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_manage_categories, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = categories.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]

        // Use Glide to load the image from the URL
        Glide.with(context)
            .load(category.imgUrl)
            .error(R.drawable.brand_img_background)
            .into(holder.categoryImage)  // Set the image in the ImageView

        holder.categoryName.text = category.name
        holder.categoryDescription.text = category.description

        holder.editButton.setOnClickListener {
            val intent = Intent(context, EditCategory::class.java).apply {
                putExtra("CATEGORY_ID", category.id)
                putExtra("CATEGORY_NAME", category.name)
                putExtra("CATEGORY_DESC", category.description)
                putExtra("CATEGORY_IMG", category.imgUrl)
            }
            context.startActivity(intent)
        }

        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setTitle("Delete Category")
                .setMessage("Are you sure you want to delete ${category.name}?")
                .setPositiveButton("Yes") { dialog, _ ->
                    deleteCategoryFromServer(category.id, position)
                    dialog.dismiss()
                }
                .setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }

    private fun deleteCategoryFromServer(categoryId: Long, position: Int) {
        val adresseIP = context.getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/categories/$categoryId"

        val request = object : JsonObjectRequest(
            Request.Method.DELETE, url, null,
            {
                val updatedCategories = categories.toMutableList()
                updatedCategories.removeAt(position)
                updateCategories(updatedCategories)
                Toast.makeText(context, "Category deleted successfully", Toast.LENGTH_SHORT).show()
            },
            { error ->

                Toast.makeText(context, "Error deleting category", Toast.LENGTH_SHORT).show()
            }
        ) {
            override fun parseNetworkResponse(response: com.android.volley.NetworkResponse?): Response<JSONObject> {
                return if (response?.statusCode == 204) {
                    Response.success(JSONObject(), null)
                } else {
                    super.parseNetworkResponse(response)
                }
            }
        }

        Volley.newRequestQueue(context).add(request)
    }
}
