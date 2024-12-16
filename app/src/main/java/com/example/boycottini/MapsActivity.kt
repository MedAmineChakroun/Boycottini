package com.example.boycottini

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.Location
import android.media.Image
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.bumptech.glide.Glide
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.io.IOException
import java.util.Locale

class MapsActivity :AppCompatActivity(){
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationTextView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.map_activity)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationTextView = findViewById(R.id.locationTextView)
        if (checkLocationPermission()) {
            getCurrentLocation()
        } else {
            requestLocationPermission()
        }


        val backbtn=findViewById<ImageView>(R.id.backIcon)
        backbtn.setOnClickListener{
            finish()
        }
        val lottieAnimationView = findViewById<LottieAnimationView>(R.id.lottieAnimationView)

        // Play animation
        lottieAnimationView.playAnimation()
    }
    private fun checkLocationPermission(): Boolean {
        val fineLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarseLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fineLocation == PackageManager.PERMISSION_GRANTED && coarseLocation == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
            100
        )
    }

    private fun getCurrentLocation() {
        if (checkLocationPermission()) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    updateLocationUI(location)
                } else {
                    locationTextView.text = "turn on the location services in your device :)"
                    Toast.makeText(this, "Impossible de récupérer votre position", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                locationTextView.text = "Erreur de localisation"
                Toast.makeText(this, "Erreur : ${it.message}", Toast.LENGTH_SHORT).show()
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun updateLocationUI(location: Location) {
        val geocoder = Geocoder(this, Locale.getDefault())
        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (addresses != null && addresses.isNotEmpty()) {
                val address = addresses[0]
                //val locality = address.locality ?: "Localité inconnue"
                val country = address.countryName ?: "Pays inconnu"
                val adminArea = address.adminArea ?: "Région inconnue"

                // Afficher l'adresse complète
                val locationDetails = "$adminArea, $country"
                locationTextView.text = "Votre position : $locationDetails"
            } else {
                locationTextView.text = "Adresse introuvable"
                Toast.makeText(this, "Impossible de récupérer l'adresse", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            locationTextView.text = "Erreur lors de l'obtention de l'adresse"
            Toast.makeText(this, "Erreur : ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation()
        } else {
            Toast.makeText(this, "Permission de localisation refusée", Toast.LENGTH_SHORT).show()
        }
    }
}
