package com.example.unihome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.unihome.entities.RoomTypeEntity
import com.example.unihome.models.Residence
import com.example.unihome.services.ResidenceService
import com.example.unihome.utils.GlobalFunctions
import com.example.unihome.utils.InternalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class CreateResidenceActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val internalDB: InternalDatabase by lazy {
        GlobalFunctions.getInternalDatabase(this@CreateResidenceActivity) }
    private val createResidenceExitIV: ImageView by lazy { findViewById(R.id.createresidence_exit_arrow_iv) }
    private val residenceNameET: EditText by lazy { findViewById(R.id.createresidence_name_et) }
    private val residenceLocationET: EditText by lazy { findViewById(R.id.createresidence_location_et) }
    private val residencePhoneNumberET: EditText by lazy { findViewById(R.id.createresidence_phone_number_et) }
    private val rt1TV: TextView by lazy { findViewById(R.id.createresidence_rt_1_tv) }
    private val rt2TV: TextView by lazy { findViewById(R.id.createresidence_rt_2_tv) }
    private val rt3TV: TextView by lazy { findViewById(R.id.createresidence_rt_3_tv) }
    private val rt4TV: TextView by lazy { findViewById(R.id.createresidence_rt_4_tv) }
    private val rt1MinusBT: Button by lazy { findViewById(R.id.createresidence_rt_1_minus_bt) }
    private val rt2MinusBT: Button by lazy { findViewById(R.id.createresidence_rt_2_minus_bt) }
    private val rt3MinusBT: Button by lazy { findViewById(R.id.createresidence_rt_3_minus_bt) }
    private val rt4MinusBT: Button by lazy { findViewById(R.id.createresidence_rt_4_minus_bt) }
    private val rt1NumberTV: TextView by lazy { findViewById(R.id.createresidence_rt_1_number_tv) }
    private val rt2NumberTV: TextView by lazy { findViewById(R.id.createresidence_rt_2_number_tv) }
    private val rt3NumberTV: TextView by lazy { findViewById(R.id.createresidence_rt_3_number_tv) }
    private val rt4NumberTV: TextView by lazy { findViewById(R.id.createresidence_rt_4_number_tv) }
    private val rt1PlusBT: Button by lazy { findViewById(R.id.createresidence_rt_1_plus_bt) }
    private val rt2PlusBT: Button by lazy { findViewById(R.id.createresidence_rt_2_plus_bt) }
    private val rt3PlusBT: Button by lazy { findViewById(R.id.createresidence_rt_3_plus_bt) }
    private val rt4PlusBT: Button by lazy { findViewById(R.id.createresidence_rt_4_plus_bt) }
    private val createResidenceBT: Button by lazy { findViewById(R.id.createresidence_create_residence_bt) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_residence)

        getInternalRoomTypesData()

        rt1MinusBT.setOnClickListener { rtAlterValue(rt1NumberTV, 0) }
        rt1PlusBT.setOnClickListener { rtAlterValue(rt1NumberTV, 1) }
        rt2MinusBT.setOnClickListener { rtAlterValue(rt2NumberTV, 0) }
        rt2PlusBT.setOnClickListener { rtAlterValue(rt2NumberTV, 1) }
        rt3MinusBT.setOnClickListener { rtAlterValue(rt3NumberTV, 0) }
        rt3PlusBT.setOnClickListener { rtAlterValue(rt3NumberTV, 1) }
        rt4MinusBT.setOnClickListener { rtAlterValue(rt4NumberTV, 0) }
        rt4PlusBT.setOnClickListener { rtAlterValue(rt4NumberTV, 1) }

        createResidenceBT.setOnClickListener {
            if (GlobalFunctions.isNetworkAvailable(applicationContext)) {
                createResidence()
            }
            else
                Toast.makeText(this, "No Internet!", Toast.LENGTH_LONG).show()
        }

        createResidenceExitIV.setOnClickListener {
            finish()
        }
    }

    private fun createResidence() {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@CreateResidenceActivity)
        val authToken = sp.getString("authToken", null)

        val residencePair = Pair(
            first = Residence(
                null,
                residenceNameET.text.toString(),
                residenceLocationET.text.toString(),
                residencePhoneNumberET.text.toString().toInt()
            ),
            second = listOf(
                JSONObject().apply {
                    put("ID", rt1TV.tag.toString())
                    put("numberOfRooms", rt1NumberTV.text.toString())
                },
                JSONObject().apply {
                    put("ID", rt2TV.tag.toString())
                    put("numberOfRooms", rt2NumberTV.text.toString())
                },
                JSONObject().apply {
                    put("ID", rt3TV.tag.toString())
                    put("numberOfRooms", rt3NumberTV.text.toString())
                },
                JSONObject().apply {
                    put("ID", rt4TV.tag.toString())
                    put("numberOfRooms", rt4NumberTV.text.toString())
                }
            )
        )

        val service = retrofit.create(ResidenceService::class.java)
        val call = service.addResidence(authToken, residencePair)

        call.enqueue(object : Callback<Residence> {
            override fun onResponse(call: Call<Residence>, response: Response<Residence>) {
                if (response.code() == 200){
                    val newResidence = response.body()

                    val returnIntent = Intent()
                    returnIntent.putExtra("ID", newResidence?.ID)
                    returnIntent.putExtra("name", newResidence?.name)
                    returnIntent.putExtra("location", newResidence?.location)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
            override fun onFailure(call: Call<Residence>, t: Throwable) {
                val returnIntent = Intent()
                returnIntent.putExtra("error", true)
                setResult(Activity.RESULT_CANCELED, returnIntent)
                finish()
            }
        })
    }

    private fun rtAlterValue(rtNumberTV: TextView , state: Int) {
        if (state == 1) {
            val newValue = (rtNumberTV.text.toString().toInt() + 1).toString()
            rtNumberTV.text = newValue
        }
        else {
            if (rtNumberTV.text.toString().toInt() > 0) {
                val newValue = (rtNumberTV.text.toString().toInt() - 1).toString()
                rtNumberTV.text = newValue
            }
        }
    }

    private fun getInternalRoomTypesData() {
        GlobalScope.launch(Dispatchers.IO) {
            val data = internalDB.roomTypeDao().getAll()
            fillData(data)
        }
    }

    private fun fillData(roomTypeData: List<RoomTypeEntity>){
        runOnUiThread {
            for (rt in roomTypeData){
                when (rt.name) {
                    "Simple single room" -> {
                        rt1TV.text = rt.name
                        rt1TV.tag = rt.ID
                    }
                    "Simple duplex room" -> {
                        rt2TV.text = rt.name
                        rt2TV.tag = rt.ID
                    }
                    "Premium single room" -> {
                        rt3TV.text = rt.name
                        rt3TV.tag = rt.ID
                    }
                    "Premium duplex room" -> {
                        rt4TV.text = rt.name
                        rt4TV.tag = rt.ID
                    }
                }
            }
        }
    }
}