package com.example.tp3

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp3.Entites.Parking
import com.example.tp3.Retrofit.ParkingEndpoint
import kotlinx.coroutines.*

class ParkingViewModel : ViewModel() {
    var parkings = MutableLiveData<List<Parking>>()
    val loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        onError(throwable.localizedMessage)
    }

    fun getParkings() {
        if (parkings.value == null) {
            CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = ParkingEndpoint.createInstance().getParkings()
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful && response.body() != null) {
                        loading.value = false
                        parkings.postValue(response.body())

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