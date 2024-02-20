package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class PaymentType(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("description") val description: String? = null
)
