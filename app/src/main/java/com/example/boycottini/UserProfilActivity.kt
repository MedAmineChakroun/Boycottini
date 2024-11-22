package com.example.boycottini

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class UserProfilActivity:AppCompatActivity() {
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
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        val logoutbtn = findViewById<Button>(R.id.logoutbtn)
        logoutbtn.setOnClickListener {
            val editor = sharedPreferences.edit()
            editor.clear()  // Clear all the stored data
            editor.apply()
            val intent = Intent(this, MainInterfaceActivity::class.java)
            startActivity(intent)
        }



    }
}