package com.example.tp3.Retrofit

import com.example.tp3.Entites.Reservation
import com.example.tp3.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ReservationEndpoint {
    @POST("/reservation/ajouter_reservation/")
    suspend fun addReservation(@Body data:List<Reservation>):Response<Void>
    companion object{
        @Volatile
        var reservationEndpoint:ReservationEndpoint?=null
        fun createInstance():ReservationEndpoint{
            if(reservationEndpoint==null){
                synchronized(this){
                    reservationEndpoint=
                        Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build().create(ReservationEndpoint::class.java)
                }
            }
            return reservationEndpoint!!
        }
    }
}