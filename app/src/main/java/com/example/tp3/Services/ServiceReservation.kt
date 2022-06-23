package com.example.tp3.Services

import android.content.Context
import android.util.Log
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.ViewModelProvider
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.tp3.BDD.AppBD
import com.example.tp3.Entites.Reservation
import com.example.tp3.MainActivity
import com.example.tp3.ParkingViewModel
import com.example.tp3.Retrofit.ReservationEndpoint
import kotlinx.coroutines.*

class ServiceReservation(val context: Context, val params: WorkerParameters) :
    CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
            CoroutineScope(Dispatchers.Main).launch {
                Log.d("error", throwable.localizedMessage.toString());
            }
        }
        val bd: AppBD? = AppBD.buildDatabase(context)
        var succes: Boolean = false
        val reservations: List<Reservation> =
            bd?.getReservationDao()?.getReservationsNonSynchronise()!!

        CoroutineScope(Dispatchers.IO + exceptionHandler).launch {
            val response = ReservationEndpoint.createInstance().ajouterReservation(reservations)
            withContext(Dispatchers.Main) {
                succes = if (response.isSuccessful) {
                    Log.d("hello world ", "Succes")
                    bd.getReservationDao().synchReservations()
                    true
                } else {
                    false
                }
            }
        }
        if (succes) {
            return Result.success()
        } else {
            return Result.failure()
        }
    }
}