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
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var parkingViewModel: ParkingViewModel
    private lateinit var binding: ActivityMainBinding
    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient
    var handler: Handler = Handler()
    var runnable: Runnable? = null
    var delay = 5000
    var cpt: Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                // Granted. Start getting the location information
            }
        }
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
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        Log.d("Erreur", "Emplacement invalid")
                    } else {
                        var position = HashMap<String, Double>()
                        position["latitude"] = location.latitude
                        position["longitude"] = location.longitude
                        Log.d("Latitude", location.latitude.toString())
                        Log.d("Longitude", location.longitude.toString())
                        parkingViewModel.postion.value = position
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    override fun onResume() {
        handler.postDelayed(Runnable {
            handler.postDelayed(runnable!!, delay.toLong())
            getLastLocation()
        }.also { runnable = it }, delay.toLong())
        super.onResume()
    }

    override fun onPause() {
        handler.removeCallbacks(runnable!!);
        super.onPause()
    }
}