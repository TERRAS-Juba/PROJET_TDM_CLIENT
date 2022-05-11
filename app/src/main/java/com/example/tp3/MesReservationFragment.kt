package com.example.tp3

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.findNavController
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Reservation
import kotlinx.android.synthetic.main.fragment_mes_reservation.*
import java.sql.Timestamp
import java.util.*

class MesReservationFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mes_reservation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pref = this.activity?.getSharedPreferences("db_privee", Context.MODE_PRIVATE)?.edit()
        logout.setOnClickListener{
            pref?.putBoolean("connected",false)
            pref?.putString("email","")
            pref?.putString("password","")
            pref?.apply ()
            view.findNavController().navigate(R.id.action_mesReservationFragment_to_homeFragment)
        }
        val bd: AppBD? = AppBD.buildDatabase(requireContext())
        //bd?.getReservationDao()?.insert(Reservation(date = Date(), heure_entree = System.currentTimeMillis(), heure_sortie = System.currentTimeMillis(), etat = "En cours", code_qr = "123456", numero_place = 10))
        //bd?.getReservationDao()?.insert(Reservation(date = Date(), heure_entree = System.currentTimeMillis(), heure_sortie = System.currentTimeMillis(), etat = "En cours", code_qr = "998877", numero_place = 22))
        val reservations:List<Reservation> = bd?.getReservationDao()?.getReservations()!!
        var toast:Toast
        var text:String = "La liste de toutes les reservations \n"
        for (item in reservations){
            text += item.code_qr + " | "
        }
        val duration:Int = Toast.LENGTH_LONG
        toast = Toast.makeText(context, text, duration)
        toast.show()
        text="La liste des reservation en cours \n"
        for (item in reservations){
            if(item.heure_sortie> System.currentTimeMillis() && item.date>=Date()){
                text += item.code_qr + " | "
            }
        }
        toast = Toast.makeText(context, text, duration)
        toast.show()
    }
}