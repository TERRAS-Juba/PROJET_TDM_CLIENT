package com.example.tp3.Entites

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Evaluation(
    var note: Int,
    var commentaire:String,
    var id_utilisateur:Int,
    var id_parking:Int,
    var synchronise:Boolean
){
    @PrimaryKey(autoGenerate = true)
    var id_evaluation:Int = 0
}