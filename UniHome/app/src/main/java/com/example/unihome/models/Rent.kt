package com.example.unihome.models

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Rent(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("entryDate") val entryDate: Timestamp? = null,
    @SerializedName("leaveDate") val leaveDate: Timestamp? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("doorAccessCode") val doorAccessCode: String? = null,
    @SerializedName("UserID") val UserID: Int? = null,
    @SerializedName("User") val User: User? = null,
    @SerializedName("RoomID") val RoomID: Int? = null,
    @SerializedName("Room") val room: Room? = null
)
