package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class Residence(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("location") val location: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: Int? = null
)