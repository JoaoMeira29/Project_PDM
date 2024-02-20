package com.example.unihome.services

import com.example.unihome.models.Rent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface RentService {
    @GET("listRentByUserId/{UserID}")
    fun getRentByUserID(@Header("Authorization") token: String?, @Path("UserID") id: Int): Call<Rent>

    @POST("createRent")
    fun addRent(@Header("Authorization") token: String?, @Body body: JSONObject): Call<Rent>
}