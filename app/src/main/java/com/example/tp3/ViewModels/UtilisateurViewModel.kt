package com.example.tp3.ViewModels

import android.view.Menu
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.Retrofit.UtilisateurEndpoint
import kotlinx.coroutines.*
import retrofit2.http.FieldMap

class UtilisateurViewModel : ViewModel() {
    var utilisateurs = MutableLiveData<List<Utilisateur>>()
    var loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val registerStatus=MutableLiveData<Boolean>()
    var menu: Menu? =null
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            onError(throwable.localizedMessage.toString())
        }
    }
    fun connexionUtilisateurEmail(@FieldMap data: Map<String, String>) {
        loading.value = true
        if (utilisateurs.value == null || utilisateurs.value!!.isEmpty()) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = UtilisateurEndpoint.createInstance().connexionUtilisateurEmail(data)
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
    fun connexionUtilisateurEmailWithoutPassword(@FieldMap data: Map<String, String>) {
        loading.value = true
        if (utilisateurs.value == null || utilisateurs.value!!.isEmpty()) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = UtilisateurEndpoint.createInstance().connexionUtilisateurEmailWithoutPassword(data)
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
    fun connexionUtilisateurNumeroTelephone(@FieldMap data: Map<String, String>) {
        loading.value = true
        if (utilisateurs.value == null || utilisateurs.value!!.isEmpty()) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response =
                    UtilisateurEndpoint.createInstance().connexionUtilisateurNumeroTelephone(data)
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
    fun inscriptionUtilisateur(@FieldMap data: Map<String, String>) {
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = UtilisateurEndpoint.createInstance().inscriptionUtilisateur(data)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful ) {
                    loading.value = false
                    registerStatus.value=true
                } else {
                    registerStatus.value=false
                    onError(response.message())
                }
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.postValue( message)
        loading.value = false
    }
}