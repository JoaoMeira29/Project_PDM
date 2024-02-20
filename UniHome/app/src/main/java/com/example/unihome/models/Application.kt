package com.example.unihome.models

import com.google.gson.annotations.SerializedName
import java.sql.Timestamp

data class Application(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("dateSubmitted") val dateSubmitted: Timestamp? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("sosNumber") val sosNumber: Int? = null,
    @SerializedName("courseName") val courseName: String? = null,
    @SerializedName("courseYearAttended") val courseYearAttended: String? = null,
    @SerializedName("courseYearStarted") val courseYearStarted: String? = null,
    @SerializedName("lastYearStatus") val lastYearStatus: Boolean? = null,
    @SerializedName("socialBenefits") val socialBenefits: Boolean? = null,
    @SerializedName("observations") val observations: String? = null,
    @SerializedName("UserID") val UserID: Int? = null,
    @SerializedName("User") val user: User? = null,
    @SerializedName("RoomTypeID") val RoomTypeID: Int? = null,
    @SerializedName("RoomType") val roomType: RoomType? = null,
    @SerializedName("ResidenceID") val ResidenceID: Int? = null,
    @SerializedName("Residence") val residence: Residence? = null
)