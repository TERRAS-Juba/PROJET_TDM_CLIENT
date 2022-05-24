package com.example.tp3

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
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
        utilisateurViewModel.registerStatus.value = null
        utilisateurViewModel.errorMessage.value = null
        Binding.progressBarRegister.visibility = View.GONE
        Binding.register.setOnClickListener {
            var data: HashMap<String, String> = HashMap<String, String>()
            val email = Binding.email.text.toString()
            val mot_de_passe = Binding.motDePasse.text.toString()
            val confirmation_mot_de_passe = Binding.confirmationMotDePasse.text.toString()
            val numero_telephone = Binding.numeroTelephone.text.toString()
            val nom = Binding.nom.text.toString()
            val prenom = Binding.prenom.text.toString()
            if (email != "" && mot_de_passe != "" && numero_telephone != "" && nom != "" && prenom != "" && confirmation_mot_de_passe != "") {
                if (mot_de_passe == confirmation_mot_de_passe) {
                    if (android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                            .matches() && android.util.Patterns.PHONE.matcher(numero_telephone)
                            .matches()
                    ) {
                        data["email"] = email;
                        data["mot_de_passe"] = mot_de_passe;
                        data["numero_telephone"] = numero_telephone;
                        data["nom"] = nom;
                        data["prenom"] = prenom;
                        utilisateurViewModel.inscriptionUtilisateur(data)
                    } else {
                        Toast.makeText(
                            requireActivity(),
                            "Les données saisies sont invalides!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Le mot de passe et le confirmation ne correspondent pas!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    requireActivity(),
                    "Veuillez remplir tous les champs de saisie s'il vous plait!!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        utilisateurViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (!loading) {
                Binding.progressBarRegister.visibility = View.GONE
            } else {
                Binding.progressBarRegister.visibility = View.VISIBLE
            }
        })
        utilisateurViewModel.registerStatus.observe(viewLifecycleOwner, Observer { registerStatus ->
            if (registerStatus != null) {
                val builder = AlertDialog.Builder(requireActivity())
                if (registerStatus) {
                    builder.setTitle("Status d'inscription")
                    builder.setMessage("Inscription effectuée avec succés")
                    builder.setIcon(android.R.drawable.ic_dialog_info)
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                        Toast.makeText(
                            requireActivity(),
                            "Redirection vers l'ecran de connexion",
                            Toast.LENGTH_LONG
                        ).show()
                        utilisateurViewModel.registerStatus.value = null
                        activity?.findNavController(R.id.navHost)
                            ?.navigate(R.id.action_registerFragment_to_loginFragment)
                    }
                } else {
                    builder.setTitle("Status d'inscription")
                    builder.setMessage("Un probleme est survenu lors de l'inscription. Veuillez verifier les informations saisies et réessayer!")
                    builder.setIcon(android.R.drawable.ic_dialog_alert)
                    builder.setNeutralButton("Ok") { dialogInterface, which ->
                        Toast.makeText(
                            requireActivity(),
                            "Veuillez réessayer s'il vous plait!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()
            }
        })
        utilisateurViewModel.errorMessage.observe(requireActivity(), Observer { message ->
            if (message != null) {
                Toast.makeText(requireContext(), "Une erreur s'est produite", Toast.LENGTH_SHORT)
                    .show()
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}