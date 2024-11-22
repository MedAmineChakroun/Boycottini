package com.example.boycottini

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login)

        val usernameEditText = findViewById<EditText>(R.id.usernameField)
        val passwordEditText = findViewById<EditText>(R.id.passwordField)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupButton)

        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                performLogin(username, password)
            }
        }

        signupButton.setOnClickListener {

            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performLogin(username: String, password: String) {
        val url = "http://192.168.1.15:8087/api/user/users/login"
        val requestBody = JSONObject().apply {
            put("username", username)
            put("password", password)
        }

        val request = JsonObjectRequest(Request.Method.POST, url, requestBody,
            { response ->
                try {

                    val userIdFromResponse = response.getString("id")
                    val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("userId", userIdFromResponse)
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()
                    val usernameFromResponse = response.getString("username")
                    Toast.makeText(this, "Login successful! Welcome, $usernameFromResponse", Toast.LENGTH_SHORT).show()

                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } catch (e: JSONException) {
                    // If there is an error parsing the response, we handle it
                    Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show()
                }
            },
            { error ->
                // If the response is a 401 Unauthorized, handle the error
                if (error.networkResponse != null && error.networkResponse.statusCode == 401) {
                    Toast.makeText(this, "Login failed: Invalid username or password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })

        // Add the request to the Volley request queue
        Volley.newRequestQueue(this).add(request)
    }
}
