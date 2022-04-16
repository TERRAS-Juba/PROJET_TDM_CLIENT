package com.example.tp3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.parking_tp3.Parking
import com.example.tp3.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var navController: NavController
    lateinit var viewModelParking: ViewModelParking
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val navHostFragment = supportFragmentManager. findFragmentById(R.id.navHost) as NavHostFragment
        navController = navHostFragment.navController
        var parkings=loadData()
        viewModelParking = ViewModelProvider(this).get(ViewModelParking::class.java)
        viewModelParking.SetParkings(parkings)
    }
    fun loadData():List<Parking>{
        var parkings=mutableListOf<Parking>()
        var noms=getResources().getStringArray(R.array.noms)
        var positions=getResources().getStringArray(R.array.positions)
        var status=getResources().getStringArray(R.array.status)
        var capacites=getResources().getStringArray(R.array.capacites)
        var distances=getResources().getStringArray(R.array.distances)
        var durees=getResources().getStringArray(R.array.durees)
        var longitudes=getResources().getStringArray(R.array.longitudes)
        var latitudes=getResources().getStringArray(R.array.latitudes)
        var tarifs=getResources().getStringArray(R.array.tarifs)
        var horaires=getResources().getStringArray(R.array.horaires)
        var heures_ouverture=getResources().getStringArray(R.array.heures_ouverture)
        var heures_fermeture=getResources().getStringArray(R.array.heures_fermeture)
        for (i in noms.indices){
            var parking= Parking(
                noms[i],
                positions[i],
                status[i],
                capacites[i],
                distances[i],
                durees[i],
                R.drawable.parking,
                longitudes[i].toDouble(),
                latitudes[i].toDouble(),
                tarifs[i].toDouble(),
                horaires[i],
                heures_ouverture[i],
                heures_fermeture[i]
            )
            parkings.add(parking)
        }
        return parkings
    }
}