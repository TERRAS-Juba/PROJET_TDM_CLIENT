package com.example.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.ViewModelProvider
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Evaluation
import com.example.tp3.Services.ServiceEvaluation
import com.example.tp3.ViewModels.EvaluationViewModel
import com.example.tp3.ViewModels.ReservationViewModel
import com.example.tp3.databinding.FragmentCommentaireBinding
import com.example.tp3.databinding.FragmentLoginBinding

class CommentaireFragment : Fragment() {
    lateinit var Binding:FragmentCommentaireBinding
    lateinit var evaluationViewModel: EvaluationViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Binding = FragmentCommentaireBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parkingViewModel = ViewModelProvider(requireActivity()).get(ParkingViewModel::class.java)
       evaluationViewModel = ViewModelProvider(requireActivity()).get(EvaluationViewModel::class.java)
        val imageParking= arguments?.getString("image_parking")
        val nomParking= arguments?.getString("nom_parking")
        Glide.with(requireActivity()).load(imageParking)
            .apply(RequestOptions().error(R.drawable.ic_baseline_error_outline_24))
            .diskCacheStrategy(
                DiskCacheStrategy.ALL
            ).into(Binding.imageParkingCommentaire)
        Binding.nomeParkingCommentaire.text=nomParking
        Binding.buttonNoterParking.setOnClickListener {
            val stars=Binding.noteParking.rating
            var commentaire:String=Binding.commentaireParking.text.toString()
            if(commentaire.isEmpty()){
                commentaire="pas de commentaire"
            }
            val position=arguments?.getInt("position")
            val id_utilisateur=arguments?.getInt("id_utilisateur")
            val bd: AppBD? = AppBD.buildDatabase(requireContext())
            var evaluation: Evaluation
            val parking= parkingViewModel.parkings.value!![position!!]
            evaluation= Evaluation(
                note = stars.toInt(),
                commentaire = commentaire,
                id_utilisateur = id_utilisateur!!,
                id_parking = parking.id_parking,
                synchronise = false
            )
            bd?.getEvaluationDao()?.insert(evaluation)
            // Service de synchronisation
            val myConstraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED) //checks whether device should have Network Connectionthe work request
                .build()
            val yourWorkRequest = OneTimeWorkRequestBuilder<ServiceEvaluation>()
                .setConstraints(myConstraints)
                .build()
            WorkManager.getInstance(requireActivity()).enqueue(yourWorkRequest)
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("Status de commentaire")
            builder.setMessage("Votre commentaire a été enregistré")
            builder.setIcon(android.R.drawable.ic_dialog_info)
            builder.setNeutralButton("Ok") { dialogInterface, which ->
            }
            val alertDialog: AlertDialog = builder.create()
            alertDialog.setCancelable(false)
            alertDialog.show()
            Binding.commentaireParking.text.clear()
            Binding.noteParking.rating=0f
        }
    }
}