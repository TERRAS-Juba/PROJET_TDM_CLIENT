package com.example.tp3

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.example.tp3.databinding.FragmentLoginBinding
import com.example.tp3.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {
    lateinit var Binding: FragmentRegisterBinding
    lateinit var utilisateurViewModel: UtilisateurViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        Binding = FragmentRegisterBinding.inflate(inflater, container, false)
        return Binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        utilisateurViewModel =
            ViewModelProvider(requireActivity()).get(UtilisateurViewModel::class.java)
        Binding.register.setOnClickListener {
            var data : HashMap<String, String> = HashMap<String, String> ()
            data["nom"]="AMOKRANE"
            data["prenom"]="Ilhem"
            data["email"]="ii_amokrane@esi.dz"
            data["numero_telephone"] ="0541251311";
            data["mot_de_passe"] = "123456";
            utilisateurViewModel.inscriptionUtilisateur(data);
        }
    }
}