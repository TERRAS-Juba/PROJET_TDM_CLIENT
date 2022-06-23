package com.example.tp3.ViewModels

import android.view.Menu
import androidx.lifecycle.MutableLiveData
import com.example.tp3.Entites.Reservation
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.Retrofit.ReservationEndpoint
import com.example.tp3.Retrofit.UtilisateurEndpoint
import kotlinx.coroutines.*
import retrofit2.http.Body
import retrofit2.http.FieldMap

class ReservationViewModel {
    var reservation = MutableLiveData<List<Reservation>>()
    var loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            onError(throwable.localizedMessage.toString())
        }
    }
    fun ajouterReservation(@Body data: List<Reservation>):Boolean {
        loading.value = true
        var succes:Boolean=false
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = ReservationEndpoint.createInstance().ajouterReservation(data)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        loading.value = false
                        succes=true
                    } else {
                        onError(response.message())
                    }
                }
            }
        return succes
    }
    private fun onError(message: String) {
        errorMessage.postValue( message)
        loading.value = false
    }
}