package com.example.parking_tp3

import java.io.Serializable

data class Parking(
    var nom:String,
    var position:String,
    var statut:String,
    var capacite:String,
    var distance:String,
    var duree:String,
    var image:Int,
    var longitude: Double,
    var latitude:Double,
    var tarif:Double,
    var horaire:String,
    var heure_ouverture:String,
    var heure_fermeture:String,
):Serializable
