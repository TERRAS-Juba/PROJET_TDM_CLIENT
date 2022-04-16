package com.example.tp3

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.example.tp3.databinding.ActivityMainBinding
import com.example.tp3.databinding.FragmentDetailsParkingBinding


class DetailsParkingFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_details_parking, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val position= arguments?.getInt("position")
        val vm= ViewModelProvider(requireActivity()).get(ViewModelParking::class.java)
        val parking=vm.FetchParkingByIndex(position)
        view.findViewById<TextView>(R.id.nom).text=parking.nom
        view.findViewById<TextView>(R.id.position).text=parking.position
        view.findViewById<TextView>(R.id.capacite).text=parking.capacite.plus(" % ")
        view.findViewById<ImageView>(R.id.image).setImageResource(parking.image)
        view.findViewById<TextView>(R.id.statut).text=parking.statut
        if (parking.statut == "Fermé") {
            view.findViewById<TextView>(R.id.statut).setTextColor(Color.parseColor("#f00020"))
        } else {
            view.findViewById<TextView>(R.id.statut).setTextColor(Color.parseColor("#008000"))
        }
        view.findViewById<TextView>(R.id.distance).text = parking.distance.plus(" Km ")
        view.findViewById<TextView>(R.id.distance).setTextColor(Color.parseColor("#0080ff"))
        view.findViewById<TextView>(R.id.duree).text = parking.duree.plus(" min ")
        view.findViewById<TextView>(R.id.horaire).text=parking.horaire
        view.findViewById<TextView>(R.id.ouverture_fermeture).text=parking.heure_ouverture.plus(" a ").plus(parking.heure_fermeture)
        view.findViewById<TextView>(R.id.tarif).text=parking.tarif.toString().plus(" DZD ")
        view.findViewById<Button>(R.id.map).setOnClickListener{
            val latitude = parking.latitude
            val longitude = parking.longitude
            val intent = Uri.parse("geo:$latitude,$longitude").let {
                Intent(Intent.ACTION_VIEW,it)
            }
            startActivity(intent)

        }
        view.findViewById<Button>(R.id.share).setOnClickListener{
            val intent= Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,
                    parking.nom)
                type = "text/plain"
            }
            startActivity(intent)
        }
    }

}