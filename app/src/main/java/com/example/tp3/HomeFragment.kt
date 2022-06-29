package com.example.tp3

import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.Adapters.ParkingAdapter
import com.example.tp3.Entites.Parking
import com.example.tp3.databinding.FragmentHomeBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.SphericalUtil
import org.w3c.dom.Text
import java.io.IOException

class HomeFragment : Fragment() {
    lateinit var Binding: FragmentHomeBinding
    lateinit var adapter: ParkingAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var parkingViewModel: ParkingViewModel
    var listParkingFiltre = mutableListOf<Parking>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Binding = FragmentHomeBinding.inflate(inflater, container, false);
        return Binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        parkingViewModel.errorMessage.value = null
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView = Binding.recycleParking
        recyclerView.layoutManager = layoutManager
        adapter = ParkingAdapter(requireActivity())
        recyclerView.adapter = adapter
        parkingViewModel.getParkings()
        parkingViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (loading) {
                Binding.progressBarHome.visibility = View.VISIBLE
            } else {
                Binding.progressBarHome.visibility = View.GONE
            }
        })
        Binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s!!.isEmpty()) {
                    if (Binding.switch2.isChecked) {
                        Binding.etSearch2.text.clear()
                        Binding.etSearch3.text.clear()
                    }
                    var Parkings = parkingViewModel.parkings.value
                    if (Parkings != null) {
                        adapter.setParkings(Parkings)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })
        Binding.switch2.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                Binding.etSearch2.visibility = View.VISIBLE
                Binding.etSearch3.visibility = View.VISIBLE

            } else {
                Binding.etSearch2.visibility = View.INVISIBLE
                Binding.etSearch3.visibility = View.INVISIBLE

            }
        }
        Binding.btnsearch.setOnClickListener {
            listParkingFiltre.clear()
            val locationSearch: EditText = Binding.etSearch


            var location: String = locationSearch.text.toString().trim()
            var addressList: List<Address>? = null
            if (location == null || location == "") {
                Toast.makeText(requireContext(), "provide location", Toast.LENGTH_SHORT).show()
                var Parkings = parkingViewModel.parkings.value
                if (Parkings != null) {
                    adapter.setParkings(Parkings)
                }
            } else {
                val geoCoder = Geocoder(requireContext())
                try {
                    addressList = geoCoder.getFromLocationName(location, 1)
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (addressList != null) {
                    val address = addressList!![0]
                    val pos1 = LatLng(address.latitude, address.longitude)
                    var Parkings = parkingViewModel.parkings.value
                    if (Binding.etSearch3.text.toString() != "" && Binding.etSearch2.text.toString() != "") {
                        if (Parkings != null) {
                            val prixMax = Binding.etSearch3.text.toString().toDouble()
                            val distanceMax = Binding.etSearch2.text.toString().toDouble()
                            for (i in Parkings.indices) {
                                val pos2 = LatLng(Parkings[i].latitude, Parkings[i].longitude)
                                var distance =
                                    SphericalUtil.computeDistanceBetween(pos1, pos2) / 1000;
                                println("---------$distance")


                                if (distance <= distanceMax && Parkings[i].tarif <= prixMax) {
                                    listParkingFiltre.add(Parkings[i])

                                }
                            }
                            adapter.setParkings(listParkingFiltre)
                        }
                    } else {
                        if (Parkings != null) {
                            for (i in Parkings.indices) {
                                val pos2 = LatLng(Parkings[i].latitude, Parkings[i].longitude)
                                var distance =
                                    SphericalUtil.computeDistanceBetween(pos1, pos2) / 1000;
                                if (distance <= 1) {

                                    listParkingFiltre.add(Parkings[i])

                                }
                            }
                            adapter.setParkings(listParkingFiltre)
                        }
                    }
                }else{
                    Toast.makeText(requireContext(), "Impossible de se connecter à Internet", Toast.LENGTH_SHORT).show()
                }
            }
        }
        // Error message observer
        parkingViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if (message != null) {
                Toast.makeText(requireContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
        // List movies observer
        parkingViewModel.parkings.observe(viewLifecycleOwner, Observer { parkings ->
            adapter.setParkings(parkings)
        })
        parkingViewModel.postion.observe(viewLifecycleOwner, Observer { position ->
            if (position != null) {
                adapter.setPostion(
                    position["latitude"]!!,
                    position["longitude"]!!,
                    position["vitesse"]!!
                )
            }
        })
    }


}