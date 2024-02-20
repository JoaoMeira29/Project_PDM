package com.example.unihome

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import com.example.unihome.entities.RoomTypeEntity
import com.example.unihome.utils.GlobalFunctions
import com.example.unihome.utils.InternalDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ChooseRoomTypeActivity : AppCompatActivity() {
    private val internalDB: InternalDatabase by lazy {
        GlobalFunctions.getInternalDatabase(this@ChooseRoomTypeActivity) }
    private val chooseRoomExitIV: ImageView by lazy { findViewById(R.id.room_exit_arrow_iv) }
    private val optionSimpleSingleRoomBT: Button by lazy { findViewById(R.id.chooseroom_type_ss_bt) }
    private val optionSimpleDuplexRoomBT: Button by lazy { findViewById(R.id.chooseroom_type_sd_bt) }
    private val optionPremiumSingleRoomBT: Button by lazy { findViewById(R.id.chooseroom_type_ps_bt) }
    private val optionPremiumDuplexRoomBT: Button by lazy { findViewById(R.id.chooseroom_type_pd_bt) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_room_type)

        getInternalRoomTypesData()

        optionSimpleSingleRoomBT.setOnClickListener {
            val intent = Intent(this@ChooseRoomTypeActivity, RoomActivity::class.java)
            intent.putExtra("roomTypeID", optionSimpleSingleRoomBT.tag.toString().toInt())
            startActivity(intent)
        }
        optionSimpleDuplexRoomBT.setOnClickListener {
            val intent = Intent(this@ChooseRoomTypeActivity, RoomActivity::class.java)
            intent.putExtra("roomTypeID", optionSimpleDuplexRoomBT.tag.toString().toInt())
            startActivity(intent)
        }
        optionPremiumSingleRoomBT.setOnClickListener {
            val intent = Intent(this@ChooseRoomTypeActivity, RoomActivity::class.java)
            intent.putExtra("roomTypeID", optionPremiumSingleRoomBT.tag.toString().toInt())
            startActivity(intent)
        }
        optionPremiumDuplexRoomBT.setOnClickListener {
            val intent = Intent(this@ChooseRoomTypeActivity, RoomActivity::class.java)
            intent.putExtra("roomTypeID", optionPremiumDuplexRoomBT.tag.toString().toInt())
            startActivity(intent)
        }
        chooseRoomExitIV.setOnClickListener {
            finish()
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
                        optionSimpleSingleRoomBT.text = rt.name
                        optionSimpleSingleRoomBT.tag = rt.ID
                    }
                    "Simple duplex room" -> {
                        optionSimpleDuplexRoomBT.text = rt.name
                        optionSimpleDuplexRoomBT.tag = rt.ID
                    }
                    "Premium single room" -> {
                        optionPremiumSingleRoomBT.text = rt.name
                        optionPremiumSingleRoomBT.tag = rt.ID
                    }
                    "Premium duplex room" -> {
                        optionPremiumDuplexRoomBT.text = rt.name
                        optionPremiumDuplexRoomBT.tag = rt.ID
                    }
                }
            }
        }
    }
}