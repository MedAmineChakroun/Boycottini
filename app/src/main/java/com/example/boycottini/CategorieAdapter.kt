

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.boycottini.BrandByCategorie
import com.example.boycottini.R

class CategorieAdapter(private var categories: List<String>) : RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>() {

    class CategorieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categorie_boycott, parent, false)
        return CategorieViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategorieViewHolder, position: Int) {
        val categoryName = categories[position]
        holder.textView.text = categoryName
        holder.imageView.setImageResource(R.drawable.brand_img_background)

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, BrandByCategorie::class.java)
            intent.putExtra("CATEGORY_NAME", categoryName)  // Pass category name
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }


    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
        notifyDataSetChanged()


    }
}
