package com.example.unihome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.example.unihome.adapters.AllCleaningsArrayAdapter
import com.example.unihome.models.Cleaning
import com.example.unihome.models.Rent
import com.example.unihome.services.CleaningService
import com.example.unihome.services.RentService
import com.example.unihome.utils.GlobalFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AllCleaningsByUserActivity : AppCompatActivity() {
    private var userRoomID: Int? = 0
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val allCleaningsExitBT: ImageView by lazy { findViewById(R.id.allcleanings_exit_arrow_iv) }
    private val scheduleCleaningBT: Button by lazy { findViewById(R.id.allcleanings_create_bt) }
    private val allCleaningsLV: ListView by lazy { findViewById(R.id.allcleanings_cleanings_lv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_cleanings_by_user)

        val userID: Int = intent.getIntExtra("userID", 0)
        if (userID != 0) {
            getCleanings(userID)
            getRentDataByUserID(userID)
        }

        scheduleCleaningBT.setOnClickListener {
            val intent = Intent(this@AllCleaningsByUserActivity, ScheduleCleaningActivity::class.java)
            intent.putExtra("userID", userID)
            intent.putExtra("roomID", userRoomID)
            startActivityForResult(intent, 1)
        }

        allCleaningsExitBT.setOnClickListener {
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val errorExit = data?.getBooleanExtra("error", false)
            if (resultCode == Activity.RESULT_OK) {
                val date = data?.getStringExtra("date")
                val status = data?.getStringExtra("status")

                val newCleaning = Cleaning(
                    date = date,
                    status = status
                )

                val adapter = allCleaningsLV.adapter as? AllCleaningsArrayAdapter
                adapter?.addCleaning(newCleaning)
                allCleaningsLV.postDelayed({
                    adapter?.notifyDataSetChanged()
                    allCleaningsLV.smoothScrollToPosition(adapter?.count ?: 0)
                }, 50)

                Toast.makeText(this, "Cleaning successfully scheduled", Toast.LENGTH_SHORT).show()
            }
            else if (resultCode == Activity.RESULT_CANCELED && errorExit == true){
                Toast.makeText(this, "Something didn't work", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getCleanings(userID: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@AllCleaningsByUserActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(CleaningService::class.java)
        val call = service.getAllCleaningsByUserId(authToken, userID)

        call.enqueue(object : Callback<List<Cleaning>> {
            override fun onResponse(call: Call<List<Cleaning>>, response: Response<List<Cleaning>>) {
                if (response.code() == 200){
                    val cleanings = response.body()
                    cleanings?.let {
                        val mutableCleanings = it.toMutableList()
                        val adapter = AllCleaningsArrayAdapter(this@AllCleaningsByUserActivity, R.layout.layout_all_cleanings_lv, mutableCleanings)
                        allCleaningsLV.adapter = adapter
                    }
                }
            }
            override fun onFailure(call: Call<List<Cleaning>>, t: Throwable) {
                print("error")
            }
        })
    }

    private fun getRentDataByUserID(userID: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@AllCleaningsByUserActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(RentService::class.java)
        val call = service.getRentByUserID(authToken, userID)

        call.enqueue(object : Callback<Rent> {
            override fun onResponse(call: Call<Rent>, response: Response<Rent>) {
                if (response.code() == 200){
                    val rent = response.body()

                    userRoomID = rent?.RoomID
                }
            }
            override fun onFailure(call: Call<Rent>, t: Throwable) {
                print("error")
            }
        })
    }
}