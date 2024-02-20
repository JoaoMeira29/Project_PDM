package com.example.unihome.models

import com.google.gson.annotations.SerializedName

data class Chat(
    @SerializedName("ID") val ID: Int? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("ChatTypeID") val ChatTypeID: Int? = null,
    @SerializedName("ChatType") val chatType: ChatType? = null
)