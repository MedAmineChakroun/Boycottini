package com.example.boycottini


import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.navigation.NavigationView
import org.json.JSONException

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val hamburgerBtn = findViewById<ImageView>(R.id.hamburger_icon)
        hamburgerBtn.setOnClickListener {
            val drawerLayout = findViewById<DrawerLayout>(R.id.drawerLayout)
            drawerLayout.openDrawer(GravityCompat.START)
        }

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_manage_products -> {
                    //val intent  = Intent(this,ManageProducts::class.java)
                    //startActivity(intent)
                }
                R.id.nav_manage_categories -> {
                    val intent  = Intent(this,ManageCategories::class.java)
                    startActivity(intent)
                }
                R.id.nav_suggestions -> {
                    //val intent  = Intent(this,ManageProducts::class.java)
                    //startActivity(intent)
                }
                R.id.nav_share -> handleShare()
                R.id.nav_logout -> handleLogout()
            }
            true
        }

        recyclerView = findViewById(R.id.recyclerView)

        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val userId = sharedPreferences.getString("userId", null)

        if (userId != null) {

            fetchUserDetails(userId)
            setupButtonListeners()

        } else {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        fetchBoycottedProducts()

    }




    private fun handleShare() {
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, "Join the boycott movement! Learn more: https://Boycottini.com")
        }
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }

    private fun setupRecyclerView(products:List<BoycottItem> ) {

        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = BoycottAdapter(this,products)
    }

    private fun setupButtonListeners() {
        findViewById<Button>(R.id.button3).setOnClickListener {
            val intent = Intent(this, SuggestProductActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.button6).setOnClickListener {
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
        }
        findViewById<ImageView>(R.id.account_btn).setOnClickListener {
            val shareIntent = Intent(this, UserProfilActivity::class.java)
            startActivity(shareIntent)
        }
        findViewById<Button>(R.id.button5).setOnClickListener {
            Toast.makeText(this, "Scan is not available now!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchUserDetails(userId: String) {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/user/users/$userId"

        val request = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val username = response.getString("username")
                    val email = response.getString("email")
                    val password = response.getString("password")

                    val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
                    with(sharedPreferences.edit()) {
                        putString("username", username)
                        putString("email", email)
                        putString("password", password)
                        apply()
                    }
                    setupUserDetails()
                } catch (e: JSONException) {
                    Toast.makeText(this, "Error fetching user details", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                handleLogout()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }
    private fun setupUserDetails() {
        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "Unknown User")
        val email = sharedPreferences.getString("email", "No Email Provided")

        val navigationView = findViewById<NavigationView>(R.id.navigation_view)
        val headerView = navigationView.getHeaderView(0) // Get the header layout (index 0)

        // Access TextViews from the header view
        val usernameTextView = headerView.findViewById<TextView>(R.id.username)
        val emailTextView = headerView.findViewById<TextView>(R.id.useremail)

        // Set the text
        usernameTextView.text = username
        emailTextView.text = email
    }
    private fun handleLogout() {
        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            clear()
            apply()
        }

        val intent = Intent(this, MainInterfaceActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun fetchBoycottedProducts() {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/boycott/products" // L'URL de votre API

        val request = JsonArrayRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val products = ArrayList<BoycottItem>()

                    // Boucle à travers la réponse JSON pour extraire les produits
                    for (i in 0 until response.length()) {
                        val product = response.getJSONObject(i)

                        // Extraire le nom et l'image
                        val name = product.getString("name")
                        val imageUrl = product.getString("imgUrl")

                        // Créer un objet BoycottItem avec les données extraites
                        products.add(BoycottItem(id = product.getLong("id"), name = name, imageUrl = imageUrl))
                    }

                    // Mettre à jour le RecyclerView avec les produits récupérés
                    setupRecyclerView(products)
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Erreur lors du parsing des données produit", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                error.printStackTrace()
                Toast.makeText(this, "Erreur lors de la récupération des données : ${error.localizedMessage}", Toast.LENGTH_SHORT).show()
            }
        )

        // Ajouter la requête à la file d'attente Volley
        Volley.newRequestQueue(this).add(request)
    }
}
