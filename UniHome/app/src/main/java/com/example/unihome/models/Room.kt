package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class Room(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("doorNumber") val doorNumber: String? = null,
    @SerializedName("floor") val floor: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("roomPhoto") val roomPhoto: String? = null,
    @SerializedName("RoomTypeID") val RoomTypeID: Int? = null,
    @SerializedName("RoomType") val roomType: RoomType? = null,
    @SerializedName("ResidenceID") val ResidenceID: Int? = null,
    @SerializedName("Residence") val residence: Residence? = null
)
