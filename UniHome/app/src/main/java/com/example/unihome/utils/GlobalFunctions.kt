package com.example.unihome.utils

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Build
import com.example.unihome.R
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.Base64

object GlobalFunctions {
    fun getInternalDatabase(context: Context): InternalDatabase {
        return InternalDatabase.invoke(context)
    }

    fun getSharedPreferencesContext(context: Context): SharedPreferences {
        return context.getSharedPreferences(context.resources.getString(R.string.app_name), Context.MODE_PRIVATE)
    }

    fun getRetrofitBuildInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(GlobalVariables.UNIHOME_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectivityManager.activeNetworkInfo

        // Check if there is a network connection and it is available
        return (activeNetworkInfo != null) && activeNetworkInfo.isConnected
    }

    fun decodeJWTToken(jwt: String): String {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O)
            return "Requires SDK 26"

        val parts = jwt.split(".")
        return try {
            val charset = charset("UTF-8")
            val header = String(Base64.getUrlDecoder().decode(parts[0].toByteArray(charset)), charset)
            val payload = String(Base64.getUrlDecoder().decode(parts[1].toByteArray(charset)), charset)
            "$header"
            "$payload"
        } catch (e: Exception) {
            "Error parsing JWT: $e"
        }
    }
}