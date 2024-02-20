package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class User(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("surname") val surname: String? = null,
    @SerializedName("email") val email: String? = null,
    @SerializedName("password") val password: String? = null,
    @SerializedName("confirmPassword") val confirmPassword: String? = null,
    @SerializedName("gender") val gender: String? = null,
    @SerializedName("nationality") val nationality: String? = null,
    @SerializedName("nif") val nif: String? = null,
    @SerializedName("phoneNumber") val phoneNumber: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("role") val role: String? = null,
    @SerializedName("phoneToken") val phoneToken: String? = null,
    @SerializedName("userPhoto") val userPhoto: String? = null
)
