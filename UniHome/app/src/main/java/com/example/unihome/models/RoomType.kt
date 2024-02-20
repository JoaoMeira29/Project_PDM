package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class RoomType(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("price") val price: Float? = null,
    @SerializedName("description") val description: String? = null
)
