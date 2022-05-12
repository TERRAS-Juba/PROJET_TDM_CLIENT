package com.example.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.Adapters.ParkingAdapter


class HomeFragment : Fragment() {

    lateinit var adapter: ParkingAdapter
    lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view:View=inflater.inflate(R.layout.fragment_home, container, false)
        val vm=ViewModelProvider(requireActivity()).get(ViewModelParking::class.java)
        recyclerView=view.findViewById(R.id.recycleParking)
        adapter= ParkingAdapter(this,vm.FetchParkings())
        val layoutManager= LinearLayoutManager(getContext())
        recyclerView.layoutManager=layoutManager
        recyclerView.adapter=adapter
        return view
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}