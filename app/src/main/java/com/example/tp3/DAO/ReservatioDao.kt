package com.example.tp3.DAO

import androidx.room.Dao
import androidx.room.Query
import com.example.tp3.Entites.Reservation

@Dao
interface ReservatioDao:BaseDao<Reservation> {
    @Query("select * from Reservation")
    fun getReservations(): List<Reservation>
    @Query("SELECT * from Reservation where id_reservation=:id_reservation")
    fun getReservationByIdReservation(id_reservation:String):Reservation
    @Query("SELECT * from Reservation where heure_sortie >:heure_actuelle")
    fun getReservationEnCours(heure_actuelle: Long):List<Reservation>
    @Query("SELECT * from Reservation where synchronise=0")
    fun getReservationsNonSynchronise():List<Reservation>
    @Query("SELECT * from Reservation where id_utilisateur=:id_utilisateur")
    fun getReservationsUtilisateur(id_utilisateur:Int):List<Reservation>
    @Query("UPDATE Reservation set synchronise=1 where synchronise=0")
    fun synchReservations()
}