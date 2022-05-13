package com.example.tp3.ViewModels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp3.Entites.Parking
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.Retrofit.ParkingEndpoint
import com.example.tp3.Retrofit.UtilisateurEndpoint
import kotlinx.coroutines.*
class UtilisateurViewModel : ViewModel() {
    var utilisateurs = MutableLiveData<List<Utilisateur>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }
    fun connexionUtilisateurEmail(email:String, mot_de_passe:String) {
        if (utilisateurs.value == null || utilisateurs.value!!.isEmpty()) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = UtilisateurEndpoint.createInstance().connexionUtilisateurEmail(email,mot_de_passe)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        loading.value = false
                        utilisateurs.postValue(response.body())
                    } else {
                        onError(response.message())
                    }
                }
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.value = message
        loading.value = false
    }
}