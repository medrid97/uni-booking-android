package com.dev.eventbookingapp.util

import android.annotation.SuppressLint
import com.dev.eventbookingapp.models.Venue
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun formatTime(time: Map<String, Int>): String {

    val hour = time["hour"]!!.toInt()
    val minute = time["minute"]!!.toInt()

    val hourString = if (hour < 10) "0$hour" else hour.toString()
    val minuteString = if (minute < 10) "0$minute" else minute.toString()

    return "$hourString:$minuteString"
}

@SuppressLint("SimpleDateFormat")
fun formatDate(date : Long) : String{
    return SimpleDateFormat("dd.MM.yyyy").format(Date(date))
}

fun venueName(uid: String, list: ArrayList<Venue?>) : String{
    val res = list.find { v -> v!!.uid == uid }

    return res?.name ?: "Venue not found"
}