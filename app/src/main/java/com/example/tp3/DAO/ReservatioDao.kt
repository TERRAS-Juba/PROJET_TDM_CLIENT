package com.example.tp3.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.tp3.Entites.Reservation

@Dao
interface ReservatioDao:BaseDao<Reservation> {
    @Query("select * from Reservation")
    fun getReservations(): List<Reservation>
    @Query("SELECT * from Reservation where code_qr=:code_qr")
    fun getReservationByCodeQR(code_qr:String):Reservation
    @Query("SELECT * from Reservation where heure_sortie >:heure_actuelle")
    fun getReservationEnCours(heure_actuelle: Long):List<Reservation>
}