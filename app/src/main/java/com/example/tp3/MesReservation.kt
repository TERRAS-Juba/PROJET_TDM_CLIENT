package com.example.tp3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_mes_reservation.*

class MesReservation : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mes_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var pref = this.activity?.getSharedPreferences("db_privee", Context.MODE_PRIVATE)?.edit()
        logout.setOnClickListener{
            pref?.putBoolean("connected",false)
            pref?.putString("email","")
            pref?.putString("password","")
            pref?.apply ()
            /*this.activity?.findNavController(R.id.navHost)
                ?.navigate(R.id.homeFragment)*/
            view.findNavController().navigate(R.id.action_mesReservationFragment_to_homeFragment)
        }
    }
}