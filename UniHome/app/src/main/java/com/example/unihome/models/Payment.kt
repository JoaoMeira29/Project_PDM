package com.example.unihome.models

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Payment(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("description") val description: String? = null,
    @SerializedName("value") val value: Double? = null,
    @SerializedName("issueDate") val issueDate: Timestamp? = null,
    @SerializedName("paymentDate") val paymentDate: Timestamp? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("PaymentTypeID") val PaymentTypeID: Int? = null,
    @SerializedName("PaymentType") val paymentType: PaymentType? = null,
    @SerializedName("UserID") val UserID: Int? = null,
    @SerializedName("User") val user: User? = null,
    @SerializedName("RentID") val RentID: Int? = null,
    @SerializedName("Rent") val rent: Rent? = null,
    @SerializedName("CleaningID") val CleaningID: Int? = null,
    @SerializedName("Cleaning") val cleaning: Cleaning? = null
)
