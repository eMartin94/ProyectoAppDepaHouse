package com.example.proyectoappdepahouse.model

import com.google.android.gms.maps.model.LatLng

data class Estate(

    var idestate : String? = null,
    var name: String? = null,
    var city: String? = null,
    var district: String? = null,
//    var location: String? = null,
//    var location: LatLng? = null,
    var location: Map<String, Double>? = null,
    var type: String? = null,
    var price: Double? = null,
    var photo: String? = null,
    var isLiked: Boolean = false
) : java.io.Serializable

