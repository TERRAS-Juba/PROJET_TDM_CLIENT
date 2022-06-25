package com.example.tp3.ViewModels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tp3.Entites.Evaluation
import com.example.tp3.Entites.Reservation
import com.example.tp3.Retrofit.EvaluationEndpoint
import com.example.tp3.Retrofit.ReservationEndpoint
import kotlinx.coroutines.*
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Path

class EvaluationViewModel : ViewModel(){
    var evaluations = MutableLiveData<List<Evaluation>>()
    var loading = MutableLiveData<Boolean>()
    val errorMessage = MutableLiveData<String>()
    val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        CoroutineScope(Dispatchers.Main).launch {
            onError(throwable.localizedMessage.toString())
        }
    }
    fun getEvaluationsParking(@Path("id") id: Int?) {
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = EvaluationEndpoint.createInstance().getEvaluationParking(id)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                    evaluations.postValue(response.body())
                } else {
                    onError(response.message())
                }
            }
        }
    }
    suspend fun addEvaluation(@Body data: List<Evaluation>){
        loading.value = true
        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = EvaluationEndpoint.createInstance().addEvaluation(data)
            withContext(Dispatchers.Main) {
                if (response.isSuccessful) {
                    loading.value = false
                } else {
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