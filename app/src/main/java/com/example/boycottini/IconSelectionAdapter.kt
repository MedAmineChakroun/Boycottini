package com.example.boycottini

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class IconSelectionAdapter(
    private val iconList: Array<Int>,
    private val iconNames: Array<String>,
    private val onItemClick: (Int) -> Unit
) : RecyclerView.Adapter<IconSelectionAdapter.IconViewHolder>() {

    inner class IconViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val iconImageView: ImageView = itemView.findViewById(R.id.iconImageView)
        val iconNameTextView: TextView = itemView.findViewById(R.id.iconNameTextView)

        init {
            itemView.setOnClickListener {
                onItemClick(iconList[adapterPosition]) // Invoke the click listener when an item is clicked
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IconViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.dialog_item, parent, false)
        return IconViewHolder(view)
    }

    override fun onBindViewHolder(holder: IconViewHolder, position: Int) {
        holder.iconImageView.setImageResource(iconList[position])
        holder.iconNameTextView.text = iconNames[position]
    }

    override fun getItemCount(): Int {
        return iconList.size
    }
}
