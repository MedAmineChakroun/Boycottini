package com.example.boycottini

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import org.json.JSONException

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var productNameTextView: TextView
    private lateinit var boycottReasonTextView: TextView
    private lateinit var productImageView: ImageView
    private lateinit var proofBtn: TextView
    private lateinit var webView: WebView
    private lateinit var alternative: TextView
    private lateinit var sharebtn:ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_detail)

        // Initialiser les vues
        productNameTextView = findViewById(R.id.productNameTextView)
        boycottReasonTextView = findViewById(R.id.productDetailsTextView)
        productImageView = findViewById(R.id.productImageView)
        proofBtn = findViewById(R.id.proofbtn)
        webView = findViewById(R.id.boycottArticleWebView)
        alternative=findViewById(R.id.alternative_text)
        sharebtn=findViewById(R.id.shareIcon)
        sharebtn.setOnClickListener {
            val productName = productNameTextView.text.toString() // Get the product name from the TextView
            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "$productName is boycotted. Check it out!")
            }
            startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
        val productId = intent.getLongExtra("PRODUCT_ID", -1L)
        if (productId == -1L) {
            Toast.makeText(this, "Invalid Product ID", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // Fetch product details
        fetchProductDetails(productId)

        // Configuration du bouton "back"
        val backIcon: ImageView = findViewById(R.id.backIcon)
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)
        backIcon.setOnClickListener {
            callback.handleOnBackPressed()
        }
    }

    private fun fetchProductDetails(productId: Long) {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products/$productId" // Endpoint pour un produit spécifique

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val productName = response.getString("name")
                    val productImageUrl = response.getString("imgUrl")
                    val boycottReason = response.getString("raison")
                    val proofUrl = response.optString("alternativeSourceLink","" )
                    alternative.setText(response.optString("alternative",""))
                    // Mettre à jour l'interface utilisateur
                    productNameTextView.text = productName
                    boycottReasonTextView.text = boycottReason
                    Glide.with(this)
                        .load(productImageUrl)
                        .error(R.drawable.brand_img_background)
                        .into(productImageView)

                    proofBtn.setOnClickListener {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(proofUrl))
                        startActivity(intent)
                    }

                    webView.webViewClient = WebViewClient()
                    webView.settings.javaScriptEnabled = true
                    webView.settings.setSupportZoom(true)
                    webView.loadUrl(proofUrl)

                } catch (e: JSONException) {
                    Toast.makeText(this, "Error parsing product details", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Failed to fetch product details: ${error.message}", Toast.LENGTH_SHORT).show()
                error.printStackTrace()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
}