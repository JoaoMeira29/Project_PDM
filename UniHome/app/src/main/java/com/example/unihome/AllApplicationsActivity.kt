package com.example.unihome

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.ListView
import android.widget.Toast
import com.example.unihome.adapters.AllApplicationsArrayAdapter
import com.example.unihome.models.Application
import com.example.unihome.services.ApplicationService
import com.example.unihome.utils.GlobalFunctions
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class AllApplicationsActivity : AppCompatActivity() {
    private val retrofit: Retrofit by lazy {
        GlobalFunctions.getRetrofitBuildInstance() }
    private val allApplicationsLV: ListView by lazy { findViewById(R.id.allapplications_applications_lv) }
    private val applicationsExitIV: ImageView by lazy { findViewById(R.id.allapplications_exit_arrow_iv) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_applications)

        if(GlobalFunctions.isNetworkAvailable(applicationContext)){
            getApplicationsData()

            allApplicationsLV.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, position, id ->
                val applicationId = (adapterView.adapter as AllApplicationsArrayAdapter).getItem(position)?.ID
                val intent = Intent(this@AllApplicationsActivity, ApplicationActivity::class.java)

                intent.putExtra("ID", applicationId)
                intent.putExtra("position", position)
                startActivityForResult(intent, 1)
            }
        }
        else
            Toast.makeText(this, "No Internet!", Toast.LENGTH_LONG).show()

        applicationsExitIV.setOnClickListener{
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

                val adapter = allApplicationsLV.adapter as? AllApplicationsArrayAdapter
                adapter?.updateApplication(position!!, status!!)

                allApplicationsLV.postDelayed({
                    adapter?.notifyDataSetChanged()
                }, 50)

                Toast.makeText(applicationContext, "Application status successfully updated!", Toast.LENGTH_LONG).show()
            }
            else if (resultCode == Activity.RESULT_CANCELED && errorExit == true){
                Toast.makeText(this, "Something didn't work", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun getApplicationsData() {
        val sp = GlobalFunctions.getSharedPreferencesContext(this@AllApplicationsActivity)
        val authToken = sp.getString("authToken", null)

        val service = retrofit.create(ApplicationService::class.java)
        val call = service.getAllApplications(authToken)

        call.enqueue(object : Callback<List<Application>> {
            override fun onResponse(call: Call<List<Application>>, response: Response<List<Application>>) {
                if (response.code() == 200){
                    val applications = response.body()
                    applications?.let {
                        val mutableApplications = it.toMutableList()
                        val adapter = AllApplicationsArrayAdapter(this@AllApplicationsActivity, R.layout.layout_all_applications_lv, mutableApplications)
                        allApplicationsLV.adapter = adapter
                    }
                }
            }
            override fun onFailure(call: Call<List<Application>>, t: Throwable) {
                print("error")
            }
        })
    }
}