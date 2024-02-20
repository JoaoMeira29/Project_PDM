package com.example.unihome

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.ImageView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import com.example.unihome.models.Cleaning
import com.example.unihome.services.CleaningService
import com.example.unihome.utils.GlobalFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.TimeZone


class ScheduleCleaningActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val minHour = 9
    private val maxHour = 17
    private val currentDateTime by lazy { Calendar.getInstance() }
    private val scheduleCleaningExitIV: ImageView by lazy { findViewById(R.id.schedulecleaning_exit_arrow_iv) }
    private val datePickerDP: DatePicker by lazy { findViewById(R.id.schedulecleaning_date_dp) }
    private val timePickerTP: TimePicker by lazy { findViewById(R.id.schedulecleaning_time_tp) }
    private val submitBT: Button by lazy { findViewById(R.id.schedulecleaning_submit_bt) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_cleaning)

        val userID: Int = intent.getIntExtra("userID", 0)
        val userRoomID: Int = intent.getIntExtra("roomID", 0)
        val today = Calendar.getInstance()
        today.time = Date()

        // Calculate days until next Monday
        val daysUntilNextMonday = (Calendar.SATURDAY - today.get(Calendar.DAY_OF_WEEK) + 2) % 7
        val startNextWeek = Calendar.getInstance()
        startNextWeek.time = today.time
        startNextWeek.add(Calendar.DAY_OF_YEAR, daysUntilNextMonday)

        // Set the minimum date as the start of next week (Monday)
        datePickerDP.minDate = startNextWeek.timeInMillis
        timePickerTP.setIs24HourView(true)

        // Validate and set initial time for TimePicker
        val currentDateTime = Calendar.getInstance()
        val currentHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
        val currentMinute = currentDateTime.get(Calendar.MINUTE)

        // Check if the current time is within the allowed range (9 AM to 5 PM)
        if (currentHour < minHour || currentHour >= maxHour) {
            // If current time is outside allowed range, set TimePicker to the minimum hour (9 AM)
            timePickerTP.hour = minHour
            timePickerTP.minute = 0 // You can set it to any minute value you prefer
        } else {
            // If current time is within allowed range, set TimePicker to the current time
            timePickerTP.hour = currentHour
            timePickerTP.minute = currentMinute
        }

        timePickerTP.setOnTimeChangedListener { _, hourOfDay, minuteOfDay ->
            if (hourOfDay < minHour || (hourOfDay >= maxHour && minuteOfDay > 0)) {
                // If the selected hour is outside the allowed range, reset it to the minimum hour
                timePickerTP.hour = minHour
            }
        }

        submitBT.setOnClickListener {
            if (userID != 0 && userRoomID != 0) {
                createCleaningRequest(userID, userRoomID)
            }
        }

        scheduleCleaningExitIV.setOnClickListener {
            finish()
        }
    }

    private fun createCleaningRequest(userID: Int, roomID: Int) {
        val calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"))
        calendar.set(Calendar.YEAR, datePickerDP.year)
        calendar.set(Calendar.MONTH, datePickerDP.month)
        calendar.set(Calendar.DAY_OF_MONTH, datePickerDP.dayOfMonth)
        calendar.set(Calendar.HOUR_OF_DAY, timePickerTP.hour)
        calendar.set(Calendar.MINUTE, timePickerTP.minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        // Format the calendar instance to the desired timestamp format
        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        dateFormat.timeZone = TimeZone.getTimeZone("UTC")
        val formattedTimestamp: String = dateFormat.format(calendar.time)

        val sp = GlobalFunctions.getSharedPreferencesContext(this@ScheduleCleaningActivity)
        val authToken = sp.getString("authToken", null)

        val cleaningData = Cleaning(
            date = formattedTimestamp,
            RoomID = roomID,
            UserID = userID,
        )

        val service = retrofit.create(CleaningService::class.java)
        val call = service.addCleaning(authToken, cleaningData)

        call.enqueue(object : Callback<Cleaning> {
            override fun onResponse(call: Call<Cleaning>, response: Response<Cleaning>) {
                if (response.code() == 200){
                    val newCleaning = response.body()

                    val returnIntent = Intent()
                    returnIntent.putExtra("date", newCleaning?.date)
                    returnIntent.putExtra("status", newCleaning?.status)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                else if (response.code() == 403){
                    val returnIntent = Intent()
                    returnIntent.putExtra("error", true)
                    setResult(Activity.RESULT_CANCELED, returnIntent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Cleaning>, t: Throwable) {
                val returnIntent = Intent()
                returnIntent.putExtra("error", true)
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        })
    }
}