package com.example.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.parking_tp3.Parking
import com.example.parking_tp3.ParkingAdapter


class HomeFragment : Fragment() {

    lateinit var adapter: ParkingAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_home, container, false)
        val parkings=loadData()
        recyclerView=view.findViewById(R.id.recycleParking)
        adapter= ParkingAdapter(this,parkings)
        val layoutManager= LinearLayoutManager(getContext())
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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