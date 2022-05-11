package com.example.tp3.Entites

import androidx.room.Entity
import java.util.*
import androidx.room.PrimaryKey
import java.sql.Timestamp

@Entity
data class Reservation (
    var date:Date,
    var heure_entree:Long,
    var heure_sortie:Long,
    var etat:String,
    var code_qr:String,
    var numero_place:Int
){
    @PrimaryKey(autoGenerate = true)
    var id_reservation:Int = 0
}