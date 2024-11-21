

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.boycottini.BrandByCategorie
import com.example.boycottini.Category
import com.example.boycottini.R

class CategorieAdapter(private val context: Context, private var categories: List<Category>) : RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>() {

    class CategorieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categorie_boycott, parent, false)
        return CategorieViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategorieViewHolder, position: Int) {
        val category = categories[position]
        holder.textView.text = category.name

        // Fetch and load the image using Glide
        val imageUrl = "http://192.168.1.15:8087/imageCategory/get/${category.id}.png"
        Glide.with(context)
            .asBitmap()
            .load(imageUrl)
            .error(R.drawable.brand_img_background)

            .into(holder.imageView)


        holder.itemView.setOnClickListener {
            val intent = Intent(context, BrandByCategorie::class.java)
            intent.putExtra("CATEGORY_NAME", category.name)  // Pass category name to the next activity
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    fun updateCategories(newCategories: List<Category>) {
        categories = newCategories
        notifyDataSetChanged()
    }
}
