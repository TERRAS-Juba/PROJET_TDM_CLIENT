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
        Binding = FragmentLoginBinding.inflate(inflater, container, false);
        return Binding.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        Binding.progressBarLogin.visibility = View.GONE
        utilisateurViewModel =
            ViewModelProvider(requireActivity()).get(UtilisateurViewModel::class.java)
        Binding.login.setOnClickListener {
            val emailUser = Binding.email.text.toString()
            val passwordUser = Binding.mdp.text.toString()
            if (emailUser != "" && passwordUser != "") {
                utilisateurViewModel.connexionUtilisateurEmail(emailUser, passwordUser)
            } else {
                Toast.makeText(
                    requireActivity(),
                    "L'email et mot de passe ne doivent pas etre vides!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        utilisateurViewModel.loading.observe(requireActivity(), Observer { loading ->
            if (!loading) {
                Binding.progressBarLogin.visibility = View.GONE
            } else {
                Binding.progressBarLogin.visibility = View.VISIBLE
            }
        })
        utilisateurViewModel.errorMessage.observe(requireActivity(), Observer { message ->
            Toast.makeText(
                requireActivity(),
                "Une erreur s'est produite",
                Toast.LENGTH_SHORT
            ).show()
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
        utilisateurViewModel.utilisateurs.observe(requireActivity(), Observer { utilisateurs ->
            if (utilisateurViewModel.loading.value == false) {
                if (utilisateurs!=null && utilisateurs.isEmpty()) {
                    if(context!=null){
                        var text = "La connexion a échouée"
                        val duration = Toast.LENGTH_LONG
                        var toast = Toast.makeText(context, text, duration)
                        toast.show()
                        text = "Email ou mot de passe incorrect"
                        toast = Toast.makeText(context, text, duration)
                        toast.show()
                    }else{
                        Log.d("Erreur: ","Null pointer Execption, Toast message")
                    }
                } else if (utilisateurs!=null && utilisateurs.isNotEmpty()) {
                    //view.findNavController().navigate(R.id.action_loginFragment_to_mesReservationFragment2)
                    activity?.findNavController(R.id.navHost)?.navigate(R.id.action_loginFragment_to_mesReservationFragment2)
                }
            }
        })
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }
}