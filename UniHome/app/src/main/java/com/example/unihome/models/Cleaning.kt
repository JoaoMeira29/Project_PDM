package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class Cleaning(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("date") val date: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("RoomID") val RoomID: Int? = null,
    @SerializedName("Room") val room: Room? = null,
    @SerializedName("UserID") val UserID: Int? = null,
    @SerializedName("User") val user: User? = null
)
