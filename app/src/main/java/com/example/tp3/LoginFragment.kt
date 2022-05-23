package com.example.tp3

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.tp3.ViewModels.UtilisateurViewModel
import com.example.tp3.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {
    lateinit var utilisateurViewModel: UtilisateurViewModel
    lateinit var Binding: FragmentLoginBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        Binding = FragmentLoginBinding.inflate(inflater, container, false)
        return Binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Binding.progressBarLogin.visibility = View.GONE
        utilisateurViewModel =
            ViewModelProvider(requireActivity()).get(UtilisateurViewModel::class.java)
        utilisateurViewModel.errorMessage.value=null
        Binding.btnRegLogin.setOnClickListener {
            activity?.findNavController(R.id.navHost)?.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        Binding.login.setOnClickListener{
        val input = Binding.email.text.toString()
        val passwordUser = Binding.mdp.text.toString()
            var data : HashMap<String, String> = HashMap<String, String> ()
            if (input != "" && passwordUser != "") {
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()){
                    data["email"] = input;
                    data["mot_de_passe"] = passwordUser;
                    utilisateurViewModel.connexionUtilisateurEmail(data)
                }else if(android.util.Patterns.PHONE.matcher(input).matches()){
                    data["numero_telephone"] = input;
                    data["mot_de_passe"] = passwordUser;
                    utilisateurViewModel.connexionUtilisateurNumeroTelephone(data)
                }else{
                    Toast.makeText(
                        requireActivity(),
                        "Les données saisies sont invalides!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            } else {
                Toast.makeText(
                    requireActivity(),
                    "Veuillez remplir les champs email et mot de passe s'il vous plait!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        utilisateurViewModel.loading.observe(viewLifecycleOwner, Observer { loading ->
            if (!loading) {
                Binding.progressBarLogin.visibility = View.GONE
            } else {
                Binding.progressBarLogin.visibility = View.VISIBLE
            }
        })
        utilisateurViewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            if(message!=null){
                Toast.makeText(
                    requireActivity(),
                    "Une erreur s'est produite",
                    Toast.LENGTH_SHORT
                ).show()
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            }
        })
        utilisateurViewModel.utilisateurs.observe(viewLifecycleOwner, Observer { utilisateurs ->
            if (utilisateurViewModel.loading.value == false) {
                if (utilisateurs!=null && utilisateurs.isEmpty()) {
                        var text = "La connexion a échouée"
                        val duration = Toast.LENGTH_LONG
                        var toast = Toast.makeText(context, text, duration)
                        toast.show()
                        text = "Email ou mot de passe incorrect"
                        toast = Toast.makeText(context, text, duration)
                        toast.show()
                } else if (utilisateurs!=null && utilisateurs.isNotEmpty()) {
                    activity?.findNavController(R.id.navHost)?.navigate(R.id.action_loginFragment_to_mesReservationFragment2)
                }
            }
        })
    }
}