package com.example.unihome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unihome.models.Application
import com.example.unihome.models.Rent
import com.example.unihome.services.ApplicationService
import com.example.unihome.services.RentService
import com.example.unihome.utils.GlobalFunctions
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ApplicationActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private lateinit var rentObject: JSONObject
    private val applicationExitIV: ImageView by lazy { findViewById(R.id.application_exit_arrow_iv) }
    private val applicationIdTV: TextView by lazy { findViewById(R.id.application_id_tv) }
    private val usernameTV: TextView by lazy { findViewById(R.id.application_name_tv) }
    private val emailTV: TextView by lazy { findViewById(R.id.application_email_tv) }
    private val nifTV: TextView by lazy { findViewById(R.id.application_nif_tv) }
    private val phoneNumberTV: TextView by lazy { findViewById(R.id.application_phone_number_tv) }
    private val sosNumberTV: TextView by lazy { findViewById(R.id.application_sos_number_tv) }
    private val nationalityTV: TextView by lazy { findViewById(R.id.application_nationality_tv) }
    private val genderTV: TextView by lazy { findViewById(R.id.application_gender_tv) }
    private val residenceTV: TextView by lazy { findViewById(R.id.application_residence_tv) }
    private val roomTypeTV: TextView by lazy { findViewById(R.id.application_room_type_tv) }
    private val courseNameTV: TextView by lazy { findViewById(R.id.application_course_name_tv) }
    private val courseYearTV: TextView by lazy { findViewById(R.id.application_course_year_tv) }
    private val courseStartTV: TextView by lazy { findViewById(R.id.application_course_start_tv) }
    private val lastYearStatusCB: CheckBox by lazy { findViewById(R.id.application_last_year_status_cb) }
    private val scholarshipCB: CheckBox by lazy { findViewById(R.id.application_scholarship_cb) }
    private val observationsTV: TextView by lazy { findViewById(R.id.application_observations_tv) }
    private val acceptBT: Button by lazy { findViewById(R.id.application_accept_bt) }
    private val refuseBT: Button by lazy { findViewById(R.id.application_refuse_bt) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_application)

        val applicationID: Int = intent.getIntExtra("ID", 0)
        val position: Int = intent.getIntExtra("position", 0)
        if (applicationID != 0) {
            val applicationTitle = "Apply ID: $applicationID"
            applicationIdTV.text = applicationTitle

            getApplicationByIdData(applicationID)
        }

        acceptBT.setOnClickListener {
            acceptUserApplication(position)
        }
        refuseBT.setOnClickListener {
            refuseUserApplication(applicationID, position)
        }

        applicationExitIV.setOnClickListener{
            finish()
        }
    }

    private fun getApplicationByIdData(appId: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@ApplicationActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(ApplicationService::class.java)
        val call = service.getApplicationById(authToken, appId)

        call.enqueue(object : Callback<Application> {
            override fun onResponse(call: Call<Application>, response: Response<Application>) {
                if (response.code() == 200){
                    val application = response.body()
                    val fullName = application?.user?.name + " " + application?.user?.surname
                    rentObject = JSONObject()
                    rentObject.put("RoomTypeID", application?.RoomTypeID)
                    rentObject.put("UserID", application?.UserID)
                    rentObject.put("ResidenceID", application?.ResidenceID)

                    usernameTV.text = fullName
                    emailTV.text = application?.user?.email
                    nifTV.text = application?.user?.nif
                    phoneNumberTV.text = application?.user?.phoneNumber.toString()
                    sosNumberTV.text = if (application?.sosNumber != null) application.sosNumber.toString() else "-----"
                    nationalityTV.text = application?.user?.nationality
                    genderTV.text = application?.user?.gender
                    residenceTV.text = application?.residence?.name
                    roomTypeTV.text = application?.roomType?.name
                    courseNameTV.text = application?.courseName
                    courseYearTV.text = application?.courseYearAttended
                    courseStartTV.text = application?.courseYearStarted
                    lastYearStatusCB.isChecked = application?.lastYearStatus == true
                    scholarshipCB.isChecked = application?.socialBenefits == true
                    observationsTV.text = if (application?.observations != null) application.observations.toString() else "-----"

                    if (application?.status != "Waiting") {
                        acceptBT.isEnabled = false
                        acceptBT.alpha = 0.7f
                        refuseBT.isEnabled = false
                        refuseBT.alpha = 0.7f
                    }
                }
            }
            override fun onFailure(call: Call<Application>, t: Throwable) {
                print("error")
            }
        })
    }

    private fun acceptUserApplication(position: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@ApplicationActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(RentService::class.java)
        val call = service.addRent(authToken, rentObject)

        call.enqueue(object : Callback<Rent> {
            override fun onResponse(call: Call<Rent>, response: Response<Rent>) {
                if (response.code() == 200){
                    val returnIntent = Intent()
                    returnIntent.putExtra("status", "Accepted")
                    returnIntent.putExtra("position", position)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                else {
                    val errorBody = (response.errorBody())?.string()
                    val jsonObject = errorBody?.let { JSONObject(it) }
                    val error = jsonObject?.getString("error")
                    Toast.makeText(applicationContext, error, Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<Rent>, t: Throwable) {
                val returnIntent = Intent()
                returnIntent.putExtra("error", true)
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        })
    }

    private fun refuseUserApplication(appID: Int, position: Int) {
        val updateApplication = Application(
            status = "Refused"
        )

        val sp = GlobalFunctions.getSharedPreferencesContext(this@ApplicationActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(ApplicationService::class.java)
        val call = service.updateApplication(authToken, appID, updateApplication)

        call.enqueue(object : Callback<Application> {
            override fun onResponse(call: Call<Application>, response: Response<Application>) {
                if (response.code() == 200){
                    val updatedApplication = response.body()

                    val returnIntent = Intent()
                    returnIntent.putExtra("status", updatedApplication?.status)
                    returnIntent.putExtra("position", position)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Application>, t: Throwable) {
                val returnIntent = Intent()
                returnIntent.putExtra("error", true)
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        })
    }
}