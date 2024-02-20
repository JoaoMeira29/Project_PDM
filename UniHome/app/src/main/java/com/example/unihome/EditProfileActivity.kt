package com.example.unihome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.unihome.models.User
import com.example.unihome.services.UserService
import com.example.unihome.utils.GlobalFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class EditProfileActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val editProfileExitIV: ImageView by lazy { findViewById(R.id.edit_profile_exit_arrow_iv)}
    private val firstNameET: TextView by lazy { findViewById(R.id.editprofile_first_name_et) }
    private val surnameET: TextView by lazy { findViewById(R.id.editprofile_surname_et) }
    private val passwordET: TextView by lazy { findViewById(R.id.editprofile_password_et) }
    private val phoneNumberET: TextView by lazy { findViewById(R.id.editprofile_phone_et) }
    private val nifET: TextView by lazy { findViewById(R.id.editprofile_nif_et) }
    private val submitBT: Button by lazy { findViewById(R.id.editprofile_submit_bt) }
    private val genderSP: Spinner by lazy {
        val spinner = findViewById<Spinner>(R.id.editprofile_nationality_sp)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("-Gender-", "Male", "Female")
        )
        spinner
    }
    private val nationalitySP: Spinner by lazy {
        val spinner = findViewById<Spinner>(R.id.editprofile_gender_sp)
        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("-Nationality-", "Portuguese", "English")
        )
        spinner
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        genderSP.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.lightBrown)
                )
                (parent.getChildAt(0) as TextView).textSize = 18f
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        nationalitySP.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
                (parent!!.getChildAt(0) as TextView).setTextColor(
                    ContextCompat.getColor(applicationContext, R.color.lightBrown)
                )
                (parent.getChildAt(0) as TextView).textSize = 18f
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        val userID: Int = intent.getIntExtra("userID", 0)

        submitBT.setOnClickListener {
            updateUserData(userID)
        }

        editProfileExitIV.setOnClickListener {
            finish()
        }
    }

    private fun updateUserData(userID: Int) {
        //TODO ADD USER PROFILE PHOTO
        val updatedUser = User(
            name = firstNameET.text.takeIf { it.isNotBlank() && it.isNotEmpty() }?.toString(),
            surname = surnameET.text.takeIf { it.isNotBlank() && it.isNotEmpty() }?.toString(),
            password = passwordET.text.takeIf { it.isNotBlank() && it.isNotEmpty() }?.toString(),
            gender = if (genderSP.selectedItemPosition != 0) genderSP.selectedItem.toString() else null,
            nationality = if (nationalitySP.selectedItemPosition != 0) genderSP.selectedItem.toString() else null,
            nif = nifET.text.takeIf { it.isNotBlank() && it.isNotEmpty() }?.toString(),
            phoneNumber = phoneNumberET.text.toString().takeIf { it.isNotBlank() && it.isNotEmpty() }?.toInt(),
        )

        val sp = GlobalFunctions.getSharedPreferencesContext(this@EditProfileActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(UserService::class.java)
        val call = service.updateUser(authToken, userID, updatedUser)

        call.enqueue(object : Callback<User> {
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (response.code() == 200) {
                    val user = response.body()

                    val returnIntent = Intent()
                    returnIntent.putExtra("name", user?.name)
                    returnIntent.putExtra("gender", user?.gender)
                    returnIntent.putExtra("nationality", user?.nationality)
                    returnIntent.putExtra("nif", user?.nif)
                    returnIntent.putExtra("phoneNumber", user?.phoneNumber)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }

            override fun onFailure(call: Call<User>, t: Throwable) {
                val returnIntent = Intent()
                returnIntent.putExtra("error", true)
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        })
    }
}