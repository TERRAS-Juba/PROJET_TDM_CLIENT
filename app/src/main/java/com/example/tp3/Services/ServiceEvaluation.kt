package com.example.tp3.Services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ListenableWorker
import androidx.work.WorkerParameters
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Evaluation
import com.example.tp3.Entites.Reservation
import com.example.tp3.Retrofit.EvaluationEndpoint
import com.example.tp3.Retrofit.ReservationEndpoint
import kotlinx.coroutines.*

class ServiceEvaluation (val context: Context, val params: WorkerParameters) :
    CoroutineWorker(context, params) {
        override suspend fun doWork(): Result{
            val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
                CoroutineScope(Dispatchers.Main).launch {
                    Log.d("error", throwable.localizedMessage.toString());
                }
            }
            val bd: AppBD? = AppBD.buildDatabase(context)
            val evaluations: List<Evaluation> =
                bd?.getEvaluationDao()?.getEvaluationsNonSynchronise()!!
            var succes:Boolean=false
            var request= CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
                val response = EvaluationEndpoint.createInstance().addEvaluation(evaluations)
                withContext(Dispatchers.Main) {
                    succes=if (response.isSuccessful) {
                        Log.d("hello world ", "Succes")
                        bd.getEvaluationDao().synchEvaluation()
                        true
                    }else{
                        false
                    }
                }
            }
            request.join()
            return if(succes){
                Result.success()
            }else{
                Result.retry()
            }
        }
}