package com.example.tp_intents_and_dialog

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.app.ProgressDialog
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var editPrenom: EditText
    private lateinit var editNom: EditText
    private lateinit var editEmail: EditText
    private lateinit var btnValider: Button
    private lateinit var webView: WebView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        editPrenom = findViewById(R.id.edit_email4)
        editNom = findViewById(R.id.edit_email2)
        editEmail = findViewById(R.id.edit_email)
        btnValider = findViewById(R.id.btn_valider)
        webView = findViewById(R.id.webView)

        // Instanciate WebView and set settings
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true // Enable JavaScript
        webView.settings.setSupportZoom(true) // Allow zoom

        // Load the URL in WebView
        webView.loadUrl("https://www.google.com/")

        btnValider.setOnClickListener {
            valider(it)
        }
    }

    private fun valider(v: View) {
        if (editPrenom.text.isEmpty() || editNom.text.isEmpty() || editEmail.text.isEmpty()) {
            showAlertDialog("Erreur", "Les champs ne doivent pas être vides")
        } else {
            showProgressDialog()
            showSnackbar(v, "Validation réussie!")
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        val ad: AlertDialog.Builder = AlertDialog.Builder(this)
        ad.setMessage(message)
        ad.setTitle(title)
        ad.setIcon(android.R.drawable.ic_dialog_alert)
        ad.setPositiveButton("OK") { dialogInterface, _ -> dialogInterface.dismiss() }
        val alertDialog = ad.create()
        alertDialog.show()
    }

    private fun showProgressDialog() {
        val progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Kotlin Progress Dialog")
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL)
        progressDialog.setMessage("Downloading music, please wait")
        progressDialog.isIndeterminate = true
        progressDialog.setProgress(0)
        progressDialog.show()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            progressDialog.dismiss()
            showAlertDialog("Téléchargement", "Téléchargement terminé avec succès!")
        }, 3000)
    }

    private fun showSnackbar(view: View, message: String) {
        val snack = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
        snack.setAction("Annuler") {
            Toast.makeText(this, "Action annulée!", Toast.LENGTH_SHORT).show()
        }
        snack.show()
    }

}
