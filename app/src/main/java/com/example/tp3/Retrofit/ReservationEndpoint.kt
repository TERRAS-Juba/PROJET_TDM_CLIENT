package com.example.tp3.Retrofit

import com.example.tp3.Entites.Place
import com.example.tp3.Entites.Reservation
import com.example.tp3.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ReservationEndpoint {
    @POST("/reservation/ajouter_reservation/")
    suspend fun addReservation(@Body data:List<Reservation>):Response<Void>
    @GET("/place/place_libre/{id}")
    suspend fun getPlaceLibre(@Path("id") id: Int?):Response<List<Place>>
    @FormUrlEncoded
    @POST("/reservation/nombre_reservations/")
    suspend fun getNombrePlacesOccupees(@FieldMap data: Map<String, String>):Response<List<Reservation>>
    @FormUrlEncoded
    @POST("/paiement/ajouter_paiement/")
    suspend fun addPaiement(@FieldMap data: Map<String, String>):Response<Void>
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