package com.example.tp3

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.tp3.databinding.ActivityMainBinding
import com.google.android.gms.location.*
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var parkingViewModel: ParkingViewModel
    lateinit var locationRequest:LocationRequest
    lateinit var locationCallback:LocationCallback
    lateinit var location:Location
    private lateinit var binding: ActivityMainBinding
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        locationRequest= LocationRequest.create().apply{
            interval = 10000 // Intervalle de mise à jour en millisecondes
            fastestInterval = 500 // Intervalle le plus rapide en millisecondes
            priority= PRIORITY_HIGH_ACCURACY
            maxWaitTime= 100 // Temps d’attente max en millisecondes
        }
        locationCallback= object: LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                location= p0.lastLocation!!
                if (location == null) {
                    Log.d("Erreur", "Emplacement invalid")
                } else {
                    var position = HashMap<String, Double>()
                    position["latitude"] = location.latitude
                    position["longitude"] = location.longitude
                    position["vitesse"]= location.speed.toDouble()
                    Log.d("Vitesse", location.speed.toString())
                    Log.d("Latitude", location.latitude.toString())
                    Log.d("Longitude", location.longitude.toString())
                    parkingViewModel.postion.value = position
                }
            }
        }
        getLastLocation()
        parkingViewModel = ViewModelProvider(this).get(ParkingViewModel::class.java)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navBottom, navController)
        binding.navBottom.setOnNavigationItemSelectedListener { item ->
            val pref = getSharedPreferences("db_privee", Context.MODE_PRIVATE)
            val isConnected = pref.getBoolean("connected", false)
            val currentEmail = pref.getString("email", "")
            val cuurentPassword = pref.getString("password", "")
            Log.d("Connexion", isConnected.toString())
            when (item.itemId) {
                R.id.homeFragment -> {
                    findNavController(R.id.navHost)
                        .navigate(R.id.homeFragment)
                    true
                }
                R.id.mesReservationFragment -> {
                    if (!isConnected) {
                        findNavController(R.id.navHost)
                            .navigate(R.id.loginFragment)
                    } else {
                        findNavController(R.id.navHost)
                            .navigate(R.id.mesReservationFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }

    private fun checkPermissions(): Boolean {
        if (
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }
    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }
    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.requestLocationUpdates(
                    locationRequest,
                    locationCallback, Looper.getMainLooper()
                )
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }
    override fun onPause() {
        mFusedLocationClient.removeLocationUpdates(locationCallback)
        super.onPause()
    }
}