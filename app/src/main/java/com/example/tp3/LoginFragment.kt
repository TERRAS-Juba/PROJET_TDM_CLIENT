package com.example.tp3

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.view.menu.MenuView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
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
        utilisateurViewModel.errorMessage.value = null
        setupListers()
        Binding.btnRegLogin.setOnClickListener {
            activity?.findNavController(R.id.navHost)
                ?.navigate(R.id.action_loginFragment_to_registerFragment)
        }
        Binding.login.setOnClickListener {
            if (isValid()) {
                val input = Binding.email.text.toString()
                val passwordUser = Binding.mdp.text.toString()
                var data: HashMap<String, String> = HashMap<String, String>()
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(input).matches()) {
                    data["email"] = input;
                    data["mot_de_passe"] = passwordUser;
                    utilisateurViewModel.connexionUtilisateurEmail(data)
                } else if (android.util.Patterns.PHONE.matcher(input).matches()) {
                    data["numero_telephone"] = input;
                    data["mot_de_passe"] = passwordUser;
                    utilisateurViewModel.connexionUtilisateurNumeroTelephone(data)
                } else {
                    Toast.makeText(
                        requireActivity(),
                        "Les données saisies sont invalides!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
            if (message != null) {
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
                if (utilisateurs != null && utilisateurs.isEmpty()) {
                    var text = "La connexion a échouée"
                    val duration = Toast.LENGTH_LONG
                    var toast = Toast.makeText(context, text, duration)
                    toast.show()
                    text = "Email ou mot de passe incorrect"
                    toast = Toast.makeText(context, text, duration)
                    toast.show()
                } else if (utilisateurs != null && utilisateurs.isNotEmpty()) {
                    val itemMenu: MenuItem = utilisateurViewModel.menu!!.findItem(R.id.btnLogout)
                    itemMenu.isVisible = true
                    activity?.findNavController(R.id.navHost)
                        ?.navigate(R.id.action_loginFragment_to_mesReservationFragment2)
                }
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
                    validateInput()
                }
                R.id.mdp -> {
                    validatePassword()
                }
            }
        }

        override fun afterTextChanged(p0: Editable?) {
        }
    }

    fun isValid(): Boolean = validateInput() && validatePassword()

    fun validateInput(): Boolean {
        if (Binding.email.text.toString().trim().isEmpty()) {
            Binding.emailTextInputLayout.error = "Champs requis!"
            Binding.email.requestFocus()
            return false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(Binding.email.text.toString())
                .matches() && !android.util.Patterns.PHONE.matcher(Binding.email.text.toString())
                .matches()
        ) {
            Binding.emailTextInputLayout.error = "Email/Numero de telephone invalide!"
            Binding.email.requestFocus()
            return false
        } else {
            Binding.emailTextInputLayout.isErrorEnabled = false
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

    fun setupListers() {
        Binding.email.addTextChangedListener(textFieldValidation(Binding.email))
        Binding.mdp.addTextChangedListener(textFieldValidation(Binding.mdp))
    }
}