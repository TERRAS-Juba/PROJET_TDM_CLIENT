package com.example.tp3.Entites

import androidx.room.Entity
import java.util.*
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Reservation (
    var date_reservation:Date,
    var heure_entree:Double,
    var heure_sortie:Double,
    var etat:Boolean,
    var numero_place:Int,
    var id_parking:Int,
    val id_utilisateur:Int,
    var id_paiement:Int,
    var synchronise:Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id_reservation:Int = 0
}