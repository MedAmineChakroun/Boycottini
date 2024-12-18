package com.example.boycottini

import android.content.Context
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class SuggestProductActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suggest_product)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val reasonInput = findViewById<EditText>(R.id.reasonInput)
        val brandInput = findViewById<EditText>(R.id.brandNameInput) // Add input for Brand Name
        val proofInput = findViewById<EditText>(R.id.proofUriInput) // Add input for Proof URL
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioProductToBoycott = findViewById<RadioButton>(R.id.radioProductToBoycott)
        val radioAlternative = findViewById<RadioButton>(R.id.radioAlternative)
        val submitButton = findViewById<Button>(R.id.sendButton)

        // Change the hint based on selected radio button
        radioGroup.setOnCheckedChangeListener { _, checkedId ->
            if (checkedId == radioProductToBoycott.id) {
                reasonInput.hint = "Reason Why"
            } else if (checkedId == radioAlternative.id) {
                reasonInput.hint = "Alternative of"
            }
        }

        // Back button functionality
        backButton.setOnClickListener {
            finish()
            Toast.makeText(this, "See you Later!", Toast.LENGTH_SHORT).show()
        }

        // Submit button functionality
        submitButton.setOnClickListener {
            val reason = reasonInput.text.toString()
            val brandName = brandInput.text.toString()
            val proofUrl = proofInput.text.toString()
            val isProductToBoycott = radioProductToBoycott.isChecked

            if (brandName.isEmpty() || proofUrl.isEmpty() || reason.isEmpty()) {
                Toast.makeText(this, "Please fill all required fields!", Toast.LENGTH_SHORT).show()
            } else {
                val suggestionType = if (isProductToBoycott) "PRODUCT" else "ALTERNATIVE"
                sendSuggestRequest(brandName, proofUrl, reason, suggestionType,)
            }
        }
    }

    private fun sendSuggestRequest(
        brandName: String,
        proofUrl: String,
        reason: String?,
        type: String

    ) {
        // Retrieve IP from strings.xml
        val adresseIp = getString(R.string.adresseIP)
        val url = "http://$adresseIp:8087/api/boycott/suggests"

        // Retrieve userName from SharedPreferences
        val sharedPreferences = getSharedPreferences("userSession", MODE_PRIVATE)
        val username = sharedPreferences.getString("username", "")



        // Prepare JSON object based on type
        val jsonBody = JSONObject().apply {
            put("type", type)
            put("brandName", brandName)
            put("proofUrl", proofUrl)
            put("userName", username)
            if (type == "PRODUCT") {
                put("reasonWhy", reason)
                put("alternativeOf", JSONObject.NULL)
            } else {
                put("alternativeOf", reason)
                put("reasonWhy", JSONObject.NULL)
            }
        }

        // Create and send POST request
        val request = JsonObjectRequest(
            Request.Method.POST, url, jsonBody,
            { response ->
                Toast.makeText(this, "Thank you $username for your assistance!", Toast.LENGTH_SHORT).show()
                //clearFields
                clearFields()

            },
            { error ->
                Toast.makeText(this, "Error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        )

        Volley.newRequestQueue(this).add(request)
    }

    private fun clearFields(){
        val reasonInput = findViewById<EditText>(R.id.reasonInput)
        val brandInput = findViewById<EditText>(R.id.brandNameInput)
        val proofInput = findViewById<EditText>(R.id.proofUriInput)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        reasonInput.text.clear()
        brandInput.text.clear()
        proofInput.text.clear()
    }
}
