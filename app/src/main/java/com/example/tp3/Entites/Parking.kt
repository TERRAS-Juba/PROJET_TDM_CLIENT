package com.example.tp3.Entites

data class Parking(
    var id_parking: Int,
    var photo: String,
    var nom: String,
    var commune: String,
    var etat: Boolean,
    var capacite: Int,
    var nb_places_libres: Int,
    var latitude: Double,
    var longitude: Double,
    var horaires: String,
    var tarif: Double
)
