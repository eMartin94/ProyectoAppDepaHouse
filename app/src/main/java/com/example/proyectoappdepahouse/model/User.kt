package com.example.proyectoappdepahouse.model

data class User(

    val nameUser: String = "",
    val email: String = "",
    val password: String = "",
    val favoriteEstates: MutableList<Estate> = mutableListOf(),
    val infoSendEstates: MutableList<Estate> = mutableListOf()

) {
    fun likeEstate(estate: Estate) {
        estate.isLiked = true
        favoriteEstates.add(estate)
    }

    fun infoEstate(estate: Estate) {
        estate.infoSend = true
        infoSendEstates.add(estate)
    }

}