package com.example.unihome.services

import com.example.unihome.models.User
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Streaming

interface UserService {
    @GET("listUsers")
    fun getAllUsers(@Header("Authorization") token: String?): Call<List<User>>
    @GET("listUser/{ID}")
    fun getUserById(@Header("Authorization") token: String?, @Path("ID") id: Int): Call<User>
    @GET("getUserProfileImage/{userId}")
    @Streaming // This annotation is used for larger responses (like files)
    fun getUserProfileImage(@Header("Authorization") token: String?, @Path("userId") userId: Int): Call<ResponseBody>

    @PUT("updateUser/{ID}")
    fun updateUser(@Header("Authorization") token: String?, @Path("ID") id: Int, @Body body: User): Call<User>
}