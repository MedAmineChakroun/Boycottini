package com.example.boycottini


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_product_detail)


        val webView: WebView = findViewById(R.id.boycottArticleWebView)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(true)


        val articleUrl = "https://www.bfmtv.com/economie/entreprises/mc-donald-s-ressent-toujours-les-effets-du-boycott-lie-a-la-guerre-a-gaza_AD-202404300862.html"
        webView.loadUrl(articleUrl)

        val seeProofbtn= findViewById<TextView>(R.id.proofbtn)
        seeProofbtn.setOnClickListener{
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(articleUrl)
            startActivity(intent)
        }

        val imageList = listOf(
            R.drawable.ic_alternative_brand_1,
            R.drawable.ic_alternative_brand_2,
            R.drawable.ic_alternative_brand_3,
            R.drawable.ic_alternative_brand_4,
            R.drawable.ic_alternative_brand_1,
            R.drawable.ic_alternative_brand_2,
            R.drawable.ic_alternative_brand_3,
            R.drawable.ic_alternative_brand_2,
            R.drawable.ic_alternative_brand_1,
            R.drawable.ic_alternative_brand_3
        )


        val recyclerView = findViewById<RecyclerView>(R.id.alternativeBrandsRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = AlternativeBrandsAdapter(imageList)


        val textView: TextView = findViewById(R.id.productNameTextView)
        textView.text ="brandName"

        //baaaaaaaaaaaaaaaaaaack
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, callback)


        val backIcon: ImageView = findViewById(R.id.backIcon)
        backIcon.setOnClickListener {
            callback.handleOnBackPressed()
        }
    }
}