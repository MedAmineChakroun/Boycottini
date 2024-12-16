package com.example.boycottini

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SuggestProductActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.suggest_product)

        val backButton = findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            //optional if success
            Toast.makeText(this, "thank u for ur assistance!", Toast.LENGTH_SHORT).show()
        }
        val reasonInput = findViewById<EditText>(R.id.reasonInput)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val radioProductToBoycott = findViewById<RadioButton>(R.id.radioProductToBoycott)
        val radioAlternative = findViewById<RadioButton>(R.id.radioAlternative)


        radioGroup.setOnCheckedChangeListener { group,checkedId ->
            if (checkedId == radioProductToBoycott.id) {
                reasonInput.hint = "Reason Why"
            } else if (checkedId == radioAlternative.id) {
                reasonInput.hint = "Alternative of"
            }
        }
    }

}
