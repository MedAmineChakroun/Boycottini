package com.example.boycottini

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SuggestionsAdapter(
    private val suggests: MutableList<Suggest>,  // The list of Suggest objects
    private val context: Context  // To make API requests and show Toasts
) : RecyclerView.Adapter<SuggestionsAdapter.SuggestionViewHolder>() {

    // ViewHolder class to hold the views for each item
    class SuggestionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val brandName: TextView = itemView.findViewById(R.id.brandname)
        val userName: TextView = itemView.findViewById(R.id.suggestion_name)
        val deleteButton: ImageButton = itemView.findViewById(R.id.delete_button)
        val likeButton : ImageButton=itemView.findViewById(R.id.like_button)
    }

    // Create a new view holder for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuggestionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.suggest_item, parent, false) // Create item view from XML
        return SuggestionViewHolder(view)
    }

    // Bind the data to the views and set up the delete button click listener
    override fun onBindViewHolder(holder: SuggestionViewHolder, position: Int) {
        val suggest = suggests[position]

        // Set brand name and user name
        holder.brandName.text = suggest.brandName
        holder.userName.text = suggest.userName

        // Set the heart image based on the liked state
        if (suggest.liked) {
            holder.likeButton.setImageResource(R.drawable.favorite) // Liked state
        } else {
            holder.likeButton.setImageResource(R.drawable.notfavorite) // Not liked state
        }

        // Toggle like state on button click
        holder.likeButton.setOnClickListener {
            suggest.liked = !suggest.liked // Toggle liked state
            notifyItemChanged(position) // Notify RecyclerView to update the item
            updateLikedStateInBackend(suggest) // Update liked state in the backend

            // Show a toast message
            Toast.makeText(
                context,
                if (suggest.liked) "Added to favorites" else "Removed from favorites",
                Toast.LENGTH_SHORT
            ).show()
        }

        // Set delete button click listener
        holder.deleteButton.setOnClickListener {
            removeItem(position)
            deleteSuggestFromBackend(suggest)
        }

        // Show product details dialog on item click
        holder.itemView.setOnClickListener {
            showProductDetailsDialog(suggest)
        }
    }

    // Remove the item from the list and notify RecyclerView
    private fun removeItem(position: Int) {
        suggests.removeAt(position)
        notifyItemRemoved(position)
    }

    // Optionally, delete the suggest from the backend
    private fun deleteSuggestFromBackend(suggest: Suggest) {
        val adresseIP = context.getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/suggests/${suggest.id}"

        val requestQueue = Volley.newRequestQueue(context)

        // Make a DELETE request to remove the suggest from the backend
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.DELETE, url, null,
            { response ->
                Toast.makeText(context, "Suggestion deleted successfully", Toast.LENGTH_SHORT).show()
            },
            { error ->
                Toast.makeText(context, "Suggestion deleted successfully", Toast.LENGTH_SHORT).show()

            }
        )

        requestQueue.add(jsonObjectRequest)
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int {
        return suggests.size
    }

    // Show the product details dialog with correct fields
    private fun showProductDetailsDialog(suggest: Suggest) {
        // Inflate the custom layout
        val dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_product_details, null)

        // Access the views in the custom layout
        val titleTextView = dialogView.findViewById<TextView>(R.id.dialog_title)
        val brandNameTextView = dialogView.findViewById<TextView>(R.id.dialog_brand_name)
        val reasonOrAlternativeTextView = dialogView.findViewById<TextView>(R.id.dialog_reason_or_alternative)
        val proofUrlTextView = dialogView.findViewById<TextView>(R.id.dialog_proof_url)

        // Set data dynamically
        titleTextView.text = suggest.userName
        brandNameTextView.text = "Brand Name: ${suggest.brandName}"

        // Conditionally set Reason or Alternative
        reasonOrAlternativeTextView.text = if (suggest.type == "ALTERNATIVE") {
            "Alternative Of: ${suggest.alternativeOf ?: "N/A"}"
        } else {
            "Reason Why: ${suggest.reasonWhy ?: "N/A"}"
        }

        proofUrlTextView.text = "Proof URL: ${suggest.proofUrl ?: "N/A"}"

        // Create and show the dialog
        AlertDialog.Builder(context)
            .setView(dialogView)  // Use the custom layout
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
    private fun updateLikedStateInBackend(suggest: Suggest) {
        val adresseIP = context.getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/suggests/${suggest.id}"

        val requestQueue = Volley.newRequestQueue(context)

        // Create a JSON object with the new liked state
        val params = HashMap<String, Any>()
        params["liked"] = suggest.liked
        val jsonBody = JSONObject(params as Map<*, *>)

        // Make a PUT request to update the liked state
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.PUT, url, jsonBody,
            { response ->


            },
            { error ->
                Log.e("UpdateLikedState", "Error: ${error.message}")
                Toast.makeText(context, "Failed to update liked state", Toast.LENGTH_SHORT).show()
            }
        )

        // Add the request to the request queue
        requestQueue.add(jsonObjectRequest)
    }
}

