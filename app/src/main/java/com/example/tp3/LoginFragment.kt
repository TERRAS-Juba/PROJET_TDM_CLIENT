package com.example.tp3

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_login.*

class LoginFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<Button>(R.id.login).setOnClickListener {
            var emailUser=email.text.toString()
            var passwordUser=mdp.text.toString()
            if(emailUser=="test@gmail.com" && passwordUser=="test"){
                var pref = this.activity?.getSharedPreferences("db_privee",Context.MODE_PRIVATE)?.edit()
                pref?.putBoolean("connected",true)
                pref?.putString("email",emailUser)
                pref?.putString("password",passwordUser)
                pref?.apply ()

                /*this.activity?.findNavController(R.id.navHost)
                    ?.navigate(R.id.mesReservationFragment)*/
                view.findNavController().navigate(R.id.action_loginFragment_to_mesReservationFragment2)
                }else{
                val text = "Compte inexistant"
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
            }
        }
    }
}