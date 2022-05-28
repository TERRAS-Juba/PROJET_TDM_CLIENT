package com.example.tp3

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp3.Entites.Parking
import com.example.tp3.Retrofit.ParkingEndpoint
import kotlinx.coroutines.*
import android.util.Log
class ParkingViewModel : ViewModel() {
    var parkings = MutableLiveData<List<Parking>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    var postion=MutableLiveData<HashMap<String,Double>>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            onError(throwable.localizedMessage.toString())
        }
    }
    fun getParkings() {
        if (parkings.value == null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = ParkingEndpoint.createInstance().getParkings()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        loading.value = false
                        parkings.postValue(response.body())
                        Log.d("La reponse", response.body().toString())

                    } else {
                        onError(response.message())
                    }
                }
            }
        }
    }
    private fun onError(message: String) {
        errorMessage.postValue( message)
        loading.value = false
    }
}