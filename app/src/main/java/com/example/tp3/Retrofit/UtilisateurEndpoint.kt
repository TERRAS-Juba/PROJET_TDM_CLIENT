package com.example.tp3.Retrofit

import com.example.tp3.Entites.Parking
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface UtilisateurEndpoint {
    @FormUrlEncoded
    @POST("/utilisateur/connexion_utilisateur_email/")
    suspend fun connexionUtilisateurEmail(@Field("email")email:String,@Field("mot_de_passe")mot_de_passe:String): Response<List<Utilisateur>>
    companion object{
        @Volatile
        var utilisateurEndpoint:UtilisateurEndpoint?=null
        fun createInstance():UtilisateurEndpoint{
            if(utilisateurEndpoint==null){
                synchronized(this){
                    utilisateurEndpoint=
                        Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build().create(UtilisateurEndpoint::class.java)
                }
            }
            return utilisateurEndpoint!!
        }
    }
}