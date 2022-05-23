package com.example.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.Adapters.ParkingAdapter
import com.example.tp3.databinding.FragmentDetailsParkingBinding
import com.example.tp3.databinding.FragmentHomeBinding
class HomeFragment : Fragment() {
    lateinit var Binding: FragmentHomeBinding
    lateinit var adapter: ParkingAdapter
    lateinit var recyclerView: RecyclerView
    lateinit var parkingViewModel: ParkingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Binding = FragmentHomeBinding.inflate(inflater, container, false);
        return Binding.getRoot();
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        parkingViewModel.errorMessage.value=null
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView = Binding.recycleParking
        recyclerView.layoutManager = layoutManager
        adapter = ParkingAdapter(requireActivity())
        recyclerView.adapter = adapter
        parkingViewModel.getParkings()
        // add Observers
        // loading observer
        parkingViewModel.loading.observe(requireActivity(), Observer { loading ->
            if (loading) {
                Binding.progressBarHome.visibility = View.VISIBLE
            } else {
                Binding.progressBarHome.visibility = View.GONE
            }

        })
        // Error message observer
        parkingViewModel.errorMessage.observe(requireActivity(), Observer { message ->
            if(message!=null){
                Toast.makeText(requireContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT).show()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }


        })
        // List movies observer
        parkingViewModel.parkings.observe(requireActivity(), Observer { parkings ->
            adapter.setParkings(parkings)
        })
    }
}