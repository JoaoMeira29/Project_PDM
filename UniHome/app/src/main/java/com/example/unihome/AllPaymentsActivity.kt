package com.example.unihome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.example.unihome.adapters.AllPaymentsArrayAdapter
import com.example.unihome.models.Payment
import com.example.unihome.services.PaymentService
import com.example.unihome.utils.GlobalFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AllPaymentsActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val allPaymentsLV: ListView by lazy { findViewById(R.id.allpayments_payments_lv) }
    private val paymentExitIV: ImageView by lazy { findViewById(R.id.allpayments_exit_arrow_iv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_payments)

        if(GlobalFunctions.isNetworkAvailable(applicationContext)){
            val userID: Int = intent.getIntExtra("userID", 0)
            if (userID != 0)
                getPaymentsData(userID)

            allPaymentsLV.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val paymentId = (adapterView.adapter as AllPaymentsArrayAdapter).getItem(position)?.ID
                val intent = Intent(this@AllPaymentsActivity, PaymentActivity::class.java)

                intent.putExtra("ID", paymentId)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
            }
        }
        else
            Toast.makeText(this, "No Internet!", Toast.LENGTH_LONG).show()

        paymentExitIV.setOnClickListener{
            finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            val errorExit = data?.getBooleanExtra("error", false)
            if (resultCode == Activity.RESULT_OK) {
                val status = data?.getStringExtra("status")
                val position: Int? = data?.getIntExtra("position", 0)

                val adapter = allPaymentsLV.adapter as? AllPaymentsArrayAdapter
                adapter?.updatePayment(position!!, status!!)

                allPaymentsLV.postDelayed({
                    adapter?.notifyDataSetChanged()
                }, 50)

                Toast.makeText(this, "Payment successfully made", Toast.LENGTH_SHORT).show()
            }
            else if (resultCode == Activity.RESULT_CANCELED && errorExit == true){
                Toast.makeText(this, "Something didn't work", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getPaymentsData(userID: Int) {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@AllPaymentsActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(PaymentService::class.java)
        val call = service.getPaymentsByUserId(authToken, userID)

        call.enqueue(object : Callback<List<Payment>> {
            override fun onResponse(call: Call<List<Payment>>, response: Response<List<Payment>>) {
                if (response.code() == 200){
                    val payments = response.body()
                    payments?.let {
                        val mutablePayments = it.toMutableList()
                        val adapter = AllPaymentsArrayAdapter(this@AllPaymentsActivity, R.layout.layout_all_payments_lv, mutablePayments)
                        allPaymentsLV.adapter = adapter
                    }
                }
            }
            override fun onFailure(call: Call<List<Payment>>, t: Throwable) {
                print("error")
            }
        })
    }
}