import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.boycottini.ProductDetailActivity
import com.example.boycottini.R

class BoycottAdapter(private val brands: List<String>) : RecyclerView.Adapter<BoycottAdapter.BoycottViewHolder>() {

    class BoycottViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val textView: TextView = itemView.findViewById(R.id.textView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoycottViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_boycott, parent, false)
        return BoycottViewHolder(view)
    }

    override fun onBindViewHolder(holder: BoycottViewHolder, position: Int) {
        holder.textView.text = brands[position]
        holder.imageView.setImageResource(R.drawable.brand_img_background) // Replace with a real image

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, ProductDetailActivity::class.java)
            intent.putExtra("BRAND_NAME", brands[position])
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = brands.size
}
