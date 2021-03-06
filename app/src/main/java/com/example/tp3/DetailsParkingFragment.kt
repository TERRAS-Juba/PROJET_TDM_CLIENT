package com.example.tp3

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
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
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Evaluation
import com.example.tp3.Services.ServiceEvaluation
import com.example.tp3.databinding.FragmentDetailsParkingBinding
import retrofit2.Response.error


class DetailsParkingFragment : Fragment() {
    lateinit var Binding:FragmentDetailsParkingBinding
    lateinit var parkingViewModel:ParkingViewModel
    lateinit var pref:SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Binding =FragmentDetailsParkingBinding.inflate(inflater,container,false);
        return Binding.getRoot();
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        pref = requireActivity().getSharedPreferences("db_privee", Context.MODE_PRIVATE)
        val position= arguments?.getInt("position")
        val connected:Boolean=pref.getBoolean("connected", false)
        Binding.buttonPaiement.setOnClickListener{
            val bundle = bundleOf(
                "position" to position,
            )
            view.findNavController()
                .navigate(R.id.action_detailsParkingFragment_to_ajouterReservationFragment,bundle)
        }
        if(!connected){
            Binding.buttonPaiement.visibility=View.INVISIBLE
            Binding.buttonNoter.visibility=View.INVISIBLE
        }else{
            Binding.buttonPaiement.visibility=View.VISIBLE
            Binding.buttonNoter.visibility=View.VISIBLE
        }
        val remplissageParking= arguments?.getString("remplissageParking")
        val dureeParking= arguments?.getString("dureeParking")
        val distanceParking=arguments?.getString("distanceParking")
        parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
        if(position!=null){
            val parking= parkingViewModel.parkings.value?.get(position)
            if (parking != null) {
                Binding.nomParkingDetails.text=parking.nom
                Binding.communeParkingDetails.text=parking.commune
                if (!parking.etat) {
                    Binding.etatParkingDetails.text="Ferme"
                    Binding.etatParkingDetails.setTextColor(Color.parseColor("#f00020"))
                } else {
                    Binding.etatParkingDetails.text="Ouvert"
                    Binding.etatParkingDetails.setTextColor(Color.parseColor("#008000"))
                }
                Binding.remplissageParkingDetails.text=remplissageParking
                Binding.distanceParkingDetails.text=distanceParking
                Binding.distanceParkingDetails.setTextColor(Color.parseColor("#4287f5"))
                Binding.dureeParkingDetails.text=dureeParking
                Binding.horairesParkingDetails.text=parking.horaires
                Binding.tarifParkingDetails.text=parking.tarif.toString().plus("DZD")
                Binding.tarifParkingDetails.setTextColor(Color.parseColor("#008000"))
                Glide.with(requireContext()).load(parking.photo).apply(RequestOptions().error(R.drawable.ic_baseline_error_outline_24)).into(Binding.imageParkingDetails)
                Binding.mapParkingDetails.setOnClickListener{
                    val latitude = parking.latitude
                    val longitude = parking.longitude
                    val intent = Uri.parse("geo:$latitude,$longitude").let {
                        Intent(Intent.ACTION_VIEW,it)
                    }
                    startActivity(intent)
                }
                Binding.shareParkingDetails.setOnClickListener{
                    val intent= Intent().apply {
                        var chaine:String=""
                        chaine="Nom : "+parking.nom
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT,
                            chaine)
                        type = "text/plain"
                    }
                    startActivity(intent)
                }

            }
        }
        Binding.buttonNoter.setOnClickListener {
            val parking= parkingViewModel.parkings.value!![position!!]
            val bundle = bundleOf(
                "position" to position,
                "image_parking" to parking.photo,
                "nom_parking" to parking.nom,
                "id_utilisateur" to  pref.getString("id_utilisateur", "")!!.toInt(),
            )
            view.findNavController()
                .navigate(R.id.action_detailsParkingFragment_to_commentaireFragment,bundle)
        }
    }

}