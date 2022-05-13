package com.example.tp3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.ui.NavigationUI
import com.example.tp3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        getSupportActionBar()?.hide();
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment = supportFragmentManager. findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navBottom,navController)
        binding.navBottom.setOnNavigationItemSelectedListener { item ->
            val pref = getSharedPreferences("db_privee",Context.MODE_PRIVATE)
            val isConnected = pref.getBoolean("connected", false)
            val currentEmail=pref.getString("email","")
            val cuurentPassword=pref.getString("password","")
            Log.d("Connexion",isConnected.toString())
            when(item.itemId) {
                R.id.homeFragment -> {
                    findNavController(R.id.navHost)
                        .navigate(R.id.homeFragment)
                    true
                }
                R.id.mesReservationFragment -> {
                    if(!isConnected){
                        findNavController(R.id.navHost)
                            .navigate(R.id.loginFragment)
                    }else{
                        findNavController(R.id.navHost)
                            .navigate(R.id.mesReservationFragment)
                    }
                    true
                }
                else -> false
            }
        }
    }
}