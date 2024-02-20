package com.example.unihome.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.unihome.R
import com.example.unihome.models.Application

class AllApplicationsArrayAdapter (context: Context, resource: Int, objects: MutableList<Application>) :
    ArrayAdapter<Application>(context, resource, objects) {
    private var mContext: Context
    private var mValues: MutableList<Application>
    private var mResource: Int

    init {
        mContext = context
        mValues = objects
        mResource = resource
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view: View

        if (convertView != null) {
            view = convertView
        }
        else {
            view = LayoutInflater.from(context).inflate(mResource, parent, false)
            view.tag = AllApplicationsViewHolder(view)
        }

        val vh: AllApplicationsViewHolder = view.tag as AllApplicationsViewHolder
        val currentApplication = getItem(position)

        val dateSubmitted = "Date: " + currentApplication?.dateSubmitted.toString().substring(0,11)
        val userEmail = "Email: " + currentApplication?.user?.email
        val status = currentApplication?.status

        vh.dateSubmittedTV?.text = dateSubmitted
        vh.emailTV?.text = userEmail
        vh.statusTV?.text = status

        return view
    }

    fun updateApplication(position: Int, status: String) {
        val updatedApplication = Application(
            ID = mValues[position].ID,
            dateSubmitted = mValues[position].dateSubmitted,
            status = status,
            sosNumber = mValues[position].sosNumber,
            courseName = mValues[position].courseName,
            courseYearAttended = mValues[position].courseYearAttended,
            courseYearStarted = mValues[position].courseYearStarted,
            lastYearStatus = mValues[position].lastYearStatus,
            socialBenefits = mValues[position].socialBenefits,
            observations = mValues[position].observations,
            UserID = mValues[position].UserID,
            user = mValues[position].user,
            RoomTypeID = mValues[position].RoomTypeID,
            roomType = mValues[position].roomType,
            ResidenceID = mValues[position].ResidenceID,
            residence = mValues[position].residence,
        )

        mValues[position] = updatedApplication
    }
}

private class AllApplicationsViewHolder(view: View?) {
    val dateSubmittedTV = view?.findViewById<TextView>(R.id.layoutallapplications_date_tv)
    val emailTV = view?.findViewById<TextView>(R.id.layoutallapplications_email_tv)
    val statusTV = view?.findViewById<TextView>(R.id.layoutallapplications_status_tv)
}