package com.example.tp3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Evaluation
import com.example.tp3.Entites.Reservation
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.example.tp3.databinding.FragmentMesReservationBinding
import kotlinx.android.synthetic.main.fragment_mes_reservation.*
import java.util.*

class MesReservationFragment : Fragment() {
    lateinit var Binding: FragmentMesReservationBinding
    lateinit var utilisateurViewModel: UtilisateurViewModel
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
        var utilisateur = utilisateurViewModel.utilisateurs.value?.get(0)
        val pr = requireActivity().getSharedPreferences("db_privee", Context.MODE_PRIVATE)?.edit()
        pr?.putBoolean("connected", true)
        pr?.putString("email", utilisateur?.email)
        pr?.putString("password", utilisateur?.mot_de_passe)
        pr?.apply()
        val bd: AppBD? = AppBD.buildDatabase(requireContext())
        //bd?.getReservationDao()?.insert(Reservation(date_reservation = Date(), heure_entree = System.currentTimeMillis().toDouble(), heure_sortie = System.currentTimeMillis().toDouble(), etat = true, numero_place = 1,id_parking=6,id_utilisateur=3, id_paiement = 12, synchronise = false))
        //bd?.getReservationDao()?.insert(Reservation(date_reservation = Date(), heure_entree = System.currentTimeMillis().toDouble(), heure_sortie = System.currentTimeMillis().toDouble(), etat = false, numero_place = 2,id_parking=6,id_utilisateur=3, id_paiement = 12, synchronise = false))
        //bd?.getEvaluationDao()?.insert(Evaluation(note = 1, commentaire = "Parking dans un etat catastrophique", id_utilisateur = 3, id_parking = 6,synchronise = false))
        //bd?.getEvaluationDao()?.insert(Evaluation(note = 5, commentaire = "Parking tres moderne", id_utilisateur = 3, id_parking = 1,synchronise = false))
        val reservations: List<Reservation> = bd?.getReservationDao()?.getReservations()!!
        var toast: Toast
        var text: String = "La liste de toutes les reservations \n"
        for (item in reservations) {
            text += item.id_reservation.toString() + " | "
        }
        val duration: Int = Toast.LENGTH_LONG
        toast = Toast.makeText(context, text, duration)
        toast.show()
        text = "La liste des reservation en cours \n"
        for (item in reservations) {
            if ((item.heure_sortie > System.currentTimeMillis() && item.date_reservation == Date()) || (item.date_reservation > Date())) {
                text += item.id_reservation.toString() + " | "
            }
        }
        toast = Toast.makeText(context, text, duration)
        toast.show()
    }
}