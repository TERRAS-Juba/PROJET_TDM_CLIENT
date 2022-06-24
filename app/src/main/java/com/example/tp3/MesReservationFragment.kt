package com.example.tp3

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tp3.Adapters.ReservationAdapter
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Reservation
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.example.tp3.databinding.FragmentMesReservationBinding
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
        val utilisateur: Utilisateur = Utilisateur(
            id_utilisateur = pref.getString("id_utilisateur", "")!!
                .toInt(),
            numero_telephone = pref.getString("numero_telephone", "")!!,
            nom = pref.getString("nom", "")!!,
            prenom = pref.getString("prenom", "")!!,
            mot_de_passe = pref.getString("id_utilisateur", "")!!,
            email = pref.getString("email", "")!!
        )
        val bd: AppBD? = AppBD.buildDatabase(requireContext())
        //bd?.getReservationDao()?.insert(Reservation(date_reservation = Date(), heure_entree = System.currentTimeMillis().toDouble(), heure_sortie = System.currentTimeMillis().toDouble(), etat = true, numero_place = 1,id_parking=6,id_utilisateur=3, id_paiement = 12, synchronise = false))
        //bd?.getReservationDao()?.insert(Reservation(date_reservation = Date(), heure_entree = System.currentTimeMillis().toDouble(), heure_sortie = System.currentTimeMillis().toDouble(), etat = false, numero_place = 2,id_parking=6,id_utilisateur=3, id_paiement = 12, synchronise = false))
        //bd?.getEvaluationDao()?.insert(Evaluation(note = 1, commentaire = "Parking dans un etat catastrophique", id_utilisateur = 3, id_parking = 6,synchronise = false))
        //bd?.getEvaluationDao()?.insert(Evaluation(note = 5, commentaire = "Parking tres moderne", id_utilisateur = 3, id_parking = 1,synchronise = false))
        val reservations: List<Reservation>? =
            bd?.getReservationDao()?.getReservationsUtilisateur(utilisateur.id_utilisateur)
        if (reservations != null) {
            for (item in reservations) {
                if ((item.heure_sortie > System.currentTimeMillis() && item.date_reservation == Date()) || (item.date_reservation > Date())) {
                }
            }
        }
        val layoutManager = LinearLayoutManager(requireActivity())
        recyclerView = Binding.recycleReservation
        recyclerView.layoutManager = layoutManager
        adapter = ReservationAdapter(requireActivity())
        recyclerView.adapter = adapter
        parkingViewModel=ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        if(parkingViewModel.parkings.value!=null){
            adapter.setParkings(parkingViewModel.parkings.value!!)
        }
        if (reservations != null) {
            adapter.setReservation(reservations)
        }

    }
}