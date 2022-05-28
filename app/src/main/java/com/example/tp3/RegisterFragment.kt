package com.example.tp3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
            if (isValid()){
                var data: HashMap<String, String> = HashMap<String, String>()
                val email = Binding.email.text.toString()
                val mot_de_passe = Binding.mdp.text.toString()
                val confirmation_mot_de_passe = Binding.confirmationmdp.text.toString()
                val numero_telephone = Binding.numeroTelephone.text.toString()
                val nom = Binding.nom.text.toString()
                val prenom = Binding.prenom.text.toString()


                        data["email"] = email;
                        data["mot_de_passe"] = mot_de_passe;
                        data["numero_telephone"] = numero_telephone;
                        data["nom"] = nom;
                        data["prenom"] = prenom;
                        utilisateurViewModel.inscriptionUtilisateur(data)

            }  }
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
    inner class textFieldValidation(private val view: View) : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            when (view.id) {
                R.id.email -> {
                    validateEmail()
                }
                R.id.mdp -> {
                    validatePassword()
                }
                R.id.confirmationmdp -> {
                    validateConfirmPassword()
                }
                R.id.numeroTelephone -> {
                    validatePhone()
                }
                R.id.nom -> {
                    validateNom()
                }
                R.id.prenom -> {
                    validatePrenom()
                }
            }
        }
        override fun afterTextChanged(p0: Editable?) {
        }
    }
    fun isValid(): Boolean = validateNom() && validatePrenom() && validatePhone() && validateEmail() && validatePassword() && validateConfirmPassword()
    fun validateEmail(): Boolean {
        if (Binding.email.text.toString().trim().isEmpty()) {
            Binding.emailTextInputLayout.error = "Champs requis!"
            Binding.email.requestFocus()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Binding.email.text.toString())
                .matches()
        ) {
            Binding.emailTextInputLayout.error = "Email invalide!"
            Binding.email.requestFocus()
            return false
        } else {
            Binding.emailTextInputLayout.isErrorEnabled = false
        }
        return true
    }
fun validatePhone():Boolean{
    if (Binding.numeroTelephone.text.toString().isEmpty()){
        Binding.numTextInputLayout.error = "Champs requis!"
        Binding.numeroTelephone.requestFocus()
        return false
    }else if ( !android.util.Patterns.PHONE.matcher(Binding.numeroTelephone.text.toString())
            .matches()){
        Binding.numTextInputLayout.error = "Numéro de téléphone invalide!"
        Binding.numeroTelephone.requestFocus()
        return false
    } else {
        Binding.numTextInputLayout.isErrorEnabled = false
    }
    return true
}
    fun validatePassword(): Boolean {
        if (Binding.mdp.text.toString().isEmpty()) {
            Binding.passwordTextInputLayout.error = "Champs requis!"
            Binding.mdp.requestFocus()
            return false
        } else if (Binding.mdp.text.toString().length <= 6) {
            Binding.passwordTextInputLayout.error =
                "Mot de passe doit contenir au moins 6 caracteres!"
            Binding.mdp.requestFocus()
            return false
        } else {
            Binding.passwordTextInputLayout.isErrorEnabled = false
        }
        return true
    }
    fun validateNom(): Boolean {
        if (Binding.nom.text.toString().isEmpty()) {
            Binding.nomTextInputLayout.error = "Champs requis!"
            Binding.nom.requestFocus()
            return false
        } else if (Binding.nom.text.toString().length <3) {
            Binding.nomTextInputLayout.error =
                "nom doit contenir au moins 3 caracteres!"
            Binding.nom.requestFocus()
            return false
        } else {
            Binding.nomTextInputLayout.isErrorEnabled = false
        }
        return true
    }
    fun validatePrenom(): Boolean {
        if (Binding.prenom.text.toString().isEmpty()) {
            Binding.prenomTextInputLayout.error = "Champs requis!"
            Binding.prenom.requestFocus()
            return false
        } else if (Binding.prenom.text.toString().length <3) {
            Binding.prenomTextInputLayout.error =
                "prenom doit contenir au moins 3 caracteres!"
            Binding.prenom.requestFocus()
            return false
        } else {
            Binding.prenomTextInputLayout.isErrorEnabled = false
        }
        return true
    }
    fun validateConfirmPassword(): Boolean {

        when {
            Binding.confirmationmdp.text.toString().trim().isEmpty() -> {
                Binding.confirmpasswordTextInputLayout.error = "Champ requis!"
                Binding.confirmationmdp.requestFocus()
                return false
            }
            Binding.confirmationmdp.text.toString() != Binding.mdp.text.toString() -> {
                Binding.confirmpasswordTextInputLayout.error = "Le mot de passe et le confirmation ne correspondent pas!"
                Binding.confirmationmdp.requestFocus()
                return false
            }
            else -> {
                Binding.confirmpasswordTextInputLayout.isErrorEnabled = false
            }
        }
        return true
    }

    fun setupListers() {
        Binding.email.addTextChangedListener(textFieldValidation(Binding.email))
        Binding.mdp.addTextChangedListener(textFieldValidation(Binding.mdp))
        Binding.confirmationmdp.addTextChangedListener(textFieldValidation(Binding.confirmationmdp))
        Binding.numeroTelephone.addTextChangedListener(textFieldValidation(Binding.numeroTelephone))
        Binding.nom.addTextChangedListener(textFieldValidation(Binding.nom))
        Binding.prenom.addTextChangedListener(textFieldValidation(Binding.prenom))
    }
}