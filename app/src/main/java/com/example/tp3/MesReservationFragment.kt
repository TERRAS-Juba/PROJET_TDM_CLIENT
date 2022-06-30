package com.example.tp3

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.Adapters.ReservationAdapter
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Reservation
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.example.tp3.databinding.FragmentMesReservationBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_mes_reservation.*
import java.util.*


class MesReservationFragment : Fragment() {

    lateinit var Binding: FragmentMesReservationBinding
    lateinit var utilisateurViewModel: UtilisateurViewModel
    lateinit var parkingViewModel: ParkingViewModel
    lateinit var adapter: ReservationAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Binding = FragmentMesReservationBinding.inflate(inflater, container, false);
        return Binding.root;
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utilisateurViewModel =
            ViewModelProvider(requireActivity()).get(UtilisateurViewModel::class.java)
        val pref = requireActivity().getSharedPreferences("db_privee", Context.MODE_PRIVATE)
        val id_utilisateur = pref.getString("id_utilisateur", "")!!.toInt()
        val bd: AppBD? = AppBD.buildDatabase(requireContext())
        val reservations: List<Reservation>? =
            bd?.getReservationDao()?.getReservationsUtilisateur(id_utilisateur)
        val reservationsEnCours: MutableList<Reservation> = mutableListOf()
        if (reservations != null) {
            for (item in reservations) {
                Log.d("Date de reservation",item.date_reservation.date.toString() )
                Log.d("Date actuelle",Date().date.toString() )
                if ((item.heure_sortie > System.currentTimeMillis() && item.date_reservation.year == Date().year && item.date_reservation.month == Date().month && item.date_reservation.date == Date().date) || (item.date_reservation > Date())) {
                    reservationsEnCours.add(item)
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView = Binding.recycleReservation
        recyclerView.layoutManager = layoutManager
        adapter = ReservationAdapter(requireActivity())
        recyclerView.adapter = adapter
        parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        if (parkingViewModel.parkings.value != null) {
            adapter.setParkings(parkingViewModel.parkings.value!!)
        }
        if (reservations != null) {
            adapter.setReservation(reservationsEnCours)
        }

    }

}