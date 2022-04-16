package com.example.tp3

import androidx.lifecycle.ViewModel
import com.example.parking_tp3.Parking

class ViewModelParking(): ViewModel() {
    lateinit var parkings:List<Parking>
    fun SetParkings(parkings: List<Parking>) {
        this.parkings=parkings
    }
    fun FetchParkings():List<Parking>{
        return this.parkings
    }
    fun FetchParkingByIndex(index: Int?):Parking{
        return this.parkings[index!!]
    }
}
