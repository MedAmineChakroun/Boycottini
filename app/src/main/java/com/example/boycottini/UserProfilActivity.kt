package com.example.boycottini

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale
import android.Manifest
class UserProfilActivity:AppCompatActivity() {
    @SuppressLint("MissingInflatedId")



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_profil)



        val nameField=findViewById<TextView>(R.id.nameInputId)
        val emailField=findViewById<TextView>(R.id.emailInputId)
        val passwordField=findViewById<TextView>(R.id.passwordInputId)

        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "") // Empty string if not found
        val email = sharedPreferences.getString("email", "") // Empty string if not found
        val password = sharedPreferences.getString("password", "") // Empty string if not found

        nameField.text = username.orEmpty()  // Use .orEmpty() to avoid null
        emailField.text = email.orEmpty()    // Use .orEmpty() to avoid null
        passwordField.text = password.orEmpty()

        val backbtn = findViewById<ImageView>(R.id.backIcon)
        backbtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        val logoutbtn = findViewById<Button>(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            handleLogout()
        }
        val earthMap=findViewById<ImageView>(R.id.earthIcon)
        earthMap.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        val userProfileIcon = findViewById<ImageView>(R.id.user_icon)

        // Retrieve the user icon from SharedPreferences
        val selectedIcon = sharedPreferences.getInt("userIcon", R.drawable.user_sample) // Default icon if not set
        userProfileIcon.setImageResource(selectedIcon)

        userProfileIcon.setOnClickListener {
            showIconSelectionDialog()
        }
    }
    private fun showIconSelectionDialog() {
        // Array of drawable resource ids for the suggested icons
        val icons = arrayOf(
            R.drawable.artist,
            R.drawable.assistant,
            R.drawable.avatar,
            R.drawable.employe,
            R.drawable.captain,
            R.drawable.maid,
            R.drawable.teacher,
            R.drawable.writer,
            R.drawable.painter,
            R.drawable.researcher,

            )
        val iconNames = arrayOf("artist", "assistant", "avatar", "employe","captain", "maid",
             "teacher", "writer", "painter", "researcher")

        // Inflate the custom dialog layout
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_icon_selection, null)
        val recyclerView: RecyclerView = dialogView.findViewById(R.id.iconRecyclerView)

        // Set up the RecyclerView with the adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        val adapter = IconSelectionAdapter(icons, iconNames) { selectedIcon ->
            // Save the selected icon in SharedPreferences
            val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
            with(sharedPreferences.edit()) {
                putInt("userIcon", selectedIcon) // Store the selected icon
                apply()
            }

            // Set the selected icon on the user profile image view
            val userProfileIcon = findViewById<ImageView>(R.id.user_icon)
            userProfileIcon.setImageResource(selectedIcon)
        }
        recyclerView.adapter = adapter

        // Create and show the dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select an Icon")
        builder.setView(dialogView)
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog.dismiss() }

        builder.show()
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

 }