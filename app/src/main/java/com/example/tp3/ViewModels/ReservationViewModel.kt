package com.example.tp3.ViewModels

import android.view.Menu
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp3.Entites.Place
import com.example.tp3.Entites.Reservation
import com.example.tp3.Entites.Utilisateur
import com.example.tp3.Retrofit.ReservationEndpoint
import com.example.tp3.Retrofit.UtilisateurEndpoint
import kotlinx.coroutines.*
import retrofit2.http.Body
import retrofit2.http.FieldMap
import retrofit2.http.Path

class ReservationViewModel : ViewModel(){
    var reservation = MutableLiveData<List<Reservation>>()
    var place=MutableLiveData<List<Place>>()
    var nbPlaces=MutableLiveData<List<Reservation>>()
    var loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val paye=MutableLiveData<Boolean>()
    val effectue=MutableLiveData<Boolean>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            onError(throwable.localizedMessage.toString())
        }
    }
    fun ajouterReservation(@Body data: List<Reservation>) {
        loading.value = true
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = ReservationEndpoint.createInstance().addReservation(data)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        loading.value = false
                        effectue.postValue(true)
                    } else {
                        onError(response.message())
                        effectue.postValue(false)
                    }
                }
            }
    }
    fun getPlaceLibre(@Path("id") id: Int?){
        loading.value=true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ReservationEndpoint.createInstance().getPlaceLibre(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                    place.postValue(response.body())
                } else {
                    onError(response.message())
                }
            }
        }
    }
    fun getNombrePlacesOccupees(@FieldMap data: Map<String, String>){
        loading.value=true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ReservationEndpoint.createInstance().getNombrePlacesOccupees(data)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                    nbPlaces.postValue(response.body())
                } else {
                    onError(response.message())
                }
            }
        }
    }
    fun addPaiement(@FieldMap data: Map<String, String>){
        loading.value=true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ReservationEndpoint.createInstance().addPaiement(data)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                    paye.postValue(true)
                } else {
                    onError(response.message())
                    paye.postValue(false)
                }
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.postValue( message)
        loading.value = false
    }
}