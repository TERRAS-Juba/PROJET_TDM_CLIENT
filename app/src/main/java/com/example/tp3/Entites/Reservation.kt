package com.example.tp3.Entites

import androidx.room.Entity
import java.util.*
import androidx.room.PrimaryKey

@Entity
data class Reservation(
    @PrimaryKey
    var id_reservation:String,
    var date_reservation:Date,
    var heure_entree:Double,
    var heure_sortie:Double,
    var etat:Boolean,
    var numero_place: Int?,
    var id_parking:Int,
    val id_utilisateur:Int,
    var id_paiement: String,
    var synchronise:Boolean
)