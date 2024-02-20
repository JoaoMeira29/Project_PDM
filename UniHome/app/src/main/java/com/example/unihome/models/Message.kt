package com.example.unihome.models

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Message(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("text") val text: String? = null,
    @SerializedName("dateSended") val dateSended: Timestamp? = null,
    @SerializedName("ChatID") val ChatID: Int? = null,
    @SerializedName("Chat") val chat: Chat? = null,
    @SerializedName("UserID") val UserID: Int? = null,
    @SerializedName("User") val user: User? = null
)