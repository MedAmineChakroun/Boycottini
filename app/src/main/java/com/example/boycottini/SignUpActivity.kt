package com.example.boycottini

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup)

        val usernameEditText = findViewById<EditText>(R.id.usernameField)
        val passwordEditText = findViewById<EditText>(R.id.passwordField)
        val emailEditText = findViewById<EditText>(R.id.emailField)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val signupButton = findViewById<Button>(R.id.signupButton)

        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Play animation
        lottieAnimationView.playAnimation()
        // Navigate to LoginActivity when login button is clicked
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        signupButton.setOnClickListener {
            val username = usernameEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val email = emailEditText.text.toString().trim()

            if (username.isEmpty() || password.isEmpty() || email.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "plz wait", Toast.LENGTH_SHORT).show()
                performSignup(username, email, password)
            }
        }
    }

    private fun performSignup(username: String, email: String, password: String) {
        val adresseIP = getString(R.string.adresseIP)
        val url = "http://$adresseIP:8087/api/user/users/signup"  // Ensure this URL is correct
        val requestBody = JSONObject().apply {
            put("username", username)
            put("email", email)
            put("password", password)
        }

        val request = JsonObjectRequest(Request.Method.POST, url, requestBody,
            { response ->
                try {
                    Log.d("SignupResponse", response.toString())
                    val userIdFromResponse = response.getString("userId")
                        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putString("userId", userIdFromResponse)
                        editor.putBoolean("isLoggedIn", true)
                        editor.apply()

                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()

                } catch (e: JSONException) {
                    Log.e("SignupError", "Error parsing response: ${e.message}")
                    Toast.makeText(this, "Error parsing response ${e.message}\"", Toast.LENGTH_SHORT).show()
                }
            },
            //exceptions d'erreurs
            { error ->
                if (error.networkResponse != null) {
                    when (error.networkResponse.statusCode) {
                        400 -> {
                            Toast.makeText(this, "Username or email already exists!", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    // Handle cases where there is no network response (e.g., no internet)
                    Toast.makeText(this, "Network error: ${error.message}", Toast.LENGTH_SHORT).show()
                }
            })


        Volley.newRequestQueue(this).add(request)
    }

}
