package com.example.unihome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.unihome.adapters.AllApplicationsArrayAdapter
import com.example.unihome.models.Rent
import com.example.unihome.models.User
import com.example.unihome.services.RentService
import com.example.unihome.services.UserService
import com.example.unihome.utils.GlobalFunctions
import com.squareup.picasso.Picasso
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.io.File

class ProfileActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val profileExitIV: ImageView by lazy { findViewById(R.id.profile_exit_arrow_iv) }
    private val userProfilePhotoIV: ImageView by lazy { findViewById(R.id.profile_icon_iv) }
    private val userNameTV: TextView by lazy { findViewById(R.id.profile_name_tv) }
    private val phoneNumberTV: TextView by lazy { findViewById(R.id.profile_phone_tv) }
    private val emailTV: TextView by lazy { findViewById(R.id.profile_email_tv) }
    private val residenceTV: TextView by lazy { findViewById(R.id.profile_residence_tv) }
    private val nationalityTV: TextView by lazy {findViewById(R.id.profile_nationality_tv)}
    private val genderTV: TextView by lazy { findViewById(R.id.profile_gender_tv) }
    private val nifTV: TextView by lazy { findViewById(R.id.profile_nif_tv) }
    private val cogWheel: ImageView by lazy { findViewById(R.id.profile_cog_wheel_iv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val userID: Int = intent.getIntExtra("userID", 0)

        if (userID != 0 ) {
            getUserData(userID)
            getUserProfilePhoto(userID)
            getUserRentData(userID)
        }

        cogWheel.setOnClickListener {
            val intent = Intent(this@ProfileActivity, EditProfileActivity::class.java)
            intent.putExtra("userID", userID)
            startActivityForResult(intent, 1)
        }

        profileExitIV.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val errorExit = data?.getBooleanExtra("error", false)
            if (resultCode == Activity.RESULT_OK) {
                val name: String? = data?.getStringExtra("name")
                val gender: String? = data?.getStringExtra("gender")
                val nationality: String? = data?.getStringExtra("nationality")
                val nif: String? = data?.getStringExtra("nif")
                val phoneNumber: Int? = data?.getIntExtra("phoneNumber", 0)

                userNameTV.text = name
                phoneNumberTV.text = if (phoneNumber != 0) phoneNumber.toString() else "---------"
                genderTV.text = gender ?: "---------"
                nationalityTV.text = nationality ?: "---------"
                nifTV.text = nif ?: "---------"

                Toast.makeText(applicationContext, "User successfully updated!", Toast.LENGTH_LONG).show()
            }
            else if (resultCode == Activity.RESULT_CANCELED && errorExit == true){
                Toast.makeText(this, "Something didn't work", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getUserData(userID: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@ProfileActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(UserService::class.java)
        val call = service.getUserById(authToken, userID)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    val user = response.body()
                    userNameTV.text = user?.name
                    phoneNumberTV.text = if (user?.phoneNumber != null) user.phoneNumber.toString() else "---------"
                    emailTV.text = user?.email
                    genderTV.text = if (user?.gender != null) user.gender else "---------"
                    nationalityTV.text = if (user?.nationality != null) user.nationality else "---------"
                    nifTV.text = if (user?.nif != null) user.nif else "---------"
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                print("error")
            }
        })
    }

    private fun getUserProfilePhoto(userID: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@ProfileActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(UserService::class.java)
        val call = service.getUserProfileImage(authToken, userID)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.code() == 200) {
                    val contentType = response.headers()["Content-Type"]

                    if (contentType?.startsWith("image/") == true) {
                        val inputStream = response.body()?.byteStream()
                        val file = File.createTempFile("temp_image", null, applicationContext.cacheDir)
                        file.outputStream().use { inputStream?.copyTo(it) }

                        runOnUiThread {
                            Picasso.get()
                                .load(file)
                                .into(userProfilePhotoIV)
                        }
                    } else {
                        Log.e("ImageDownload", "Error: ${response.code()} - ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                print("error")
            }
        })
    }

    private fun getUserRentData(userID: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@ProfileActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(RentService::class.java)
        val call = service.getRentByUserID(authToken, userID)

        call.enqueue(object : Callback<Rent> {
            override fun onResponse(call: Call<Rent>, response: Response<Rent>) {
                if (response.code() == 200) {
                    val rentData = response.body()

                    residenceTV.text =
                        if (rentData?.room?.residence?.name != null)
                            rentData.room.residence.name
                        else "---------"
                }
            }

            override fun onFailure(call: Call<Rent>, t: Throwable) {
                residenceTV.text = "---------"
            }
        })
    }
}