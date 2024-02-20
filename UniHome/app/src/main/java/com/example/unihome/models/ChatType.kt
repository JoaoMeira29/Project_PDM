package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class ChatType(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("name") val name: String? = null
)