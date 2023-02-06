package com.example.proyectoappdepahouse.model

data class User(

    val nameUser: String = "",
    val email: String = "",
    val password: String = "",
    val favoriteEstates: MutableList<Estate> = mutableListOf()

) {
    fun likeEstate(estate: Estate) {
        estate.isLiked = true
        favoriteEstates.add(estate)
    }
}
