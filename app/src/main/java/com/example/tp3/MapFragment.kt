package com.example.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.tp3.Entites.Parking
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

lateinit var parkingViewModel: ParkingViewModel
class MapFragment : Fragment() {

    private lateinit var mMap: GoogleMap
    val didouche= LatLng(36.7065938,3.0400831000000004);
    val sofia = LatLng(36.770493699999996,3.0423723999999996)
    val place= LatLng(36.7481885,3.0423723999999996)
    private var locationArrayList : ArrayList<LatLng>?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
private var mapReady = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rootView= inflater.inflate(R.layout.fragment_map2, container, false)
        val map=childFragmentManager.findFragmentById(R.id.myMap) as SupportMapFragment
map.getMapAsync{
    googleMap -> mMap= googleMap
    mapReady=true
    updateMap()
}
        return rootView
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity.let {
            parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        }

    }

    private fun updateMap() {

        var Parkings= parkingViewModel.parkings.value
        var toast:Toast



        if(mapReady && Parkings!=null){

            for (i in Parkings.indices){

                if (!Parkings[i].nom.isEmpty()){
                val marker = LatLng(Parkings[i].latitude,Parkings[i].longitude)
                mMap.addMarker(MarkerOptions().position(marker).title(Parkings[i].nom))

                mMap.animateCamera(CameraUpdateFactory.zoomTo(30.0f))


                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))

            }

        }
    }



}}




