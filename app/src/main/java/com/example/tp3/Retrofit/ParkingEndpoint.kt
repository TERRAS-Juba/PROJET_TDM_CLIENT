package com.example.tp3.Retrofit

import com.example.tp3.Entites.Parking
import com.example.tp3.url
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ParkingEndpoint {
    @GET("/parking/liste_parkings/")
    suspend fun getParkings():Response<List<Parking>>
    companion object{
        @Volatile
        var parkingEndpoint:ParkingEndpoint?=null
        fun createInstance():ParkingEndpoint{
            if(parkingEndpoint==null){
                synchronized(this){
                    parkingEndpoint=Retrofit.Builder().baseUrl(url).addConverterFactory(GsonConverterFactory.create()).build().create(ParkingEndpoint::class.java)
                }
            }
            return parkingEndpoint!!
        }
    }
}