package com.example.tp3.Retrofit

import com.example.tp3.Entites.Utilisateur
import com.example.tp3.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface UtilisateurEndpoint {
    @FormUrlEncoded
    @POST("/utilisateur/connexion_utilisateur_email/")
    suspend fun connexionUtilisateurEmail(@FieldMap data:Map<String,String>): Response<List<Utilisateur>>
    @FormUrlEncoded
    @POST("/utilisateur/connexion_utilisateur_numero_telephone/")
    suspend fun connexionUtilisateurNumeroTelephone(@FieldMap data:Map<String,String>): Response<List<Utilisateur>>
    @FormUrlEncoded
    @POST("/utilisateur/ajouter_utilisateur/")
    suspend fun inscriptionUtilisateur(@FieldMap data:Map<String,String>):Response<String>
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