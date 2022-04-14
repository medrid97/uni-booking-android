package com.dev.eventbookingapp.ui.ui.request

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dev.eventbookingapp.App
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.models.Venue
import com.dev.eventbookingapp.service.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class RequestViewModel @Inject constructor() : ViewModel() {

    var showVenuesList by mutableStateOf(false)

    var loading by mutableStateOf(false)

    fun toggleVenuesList() {
        showVenuesList = !showVenuesList
    }

    fun submitRequest(context: Context, venues: ArrayList<Venue?>) {

        fun failToast(text: String) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }

        if (name.trim().length < 4) {
            failToast("Name is too short ! (Less than 4)")
            return
        }

        if (activity.trim().length < 4) {
            failToast("Activity is too short ! (Less than 4)")
            return
        }

        if (description.trim().length < 20) {
            failToast("Description is too short ! (Less than 20)")
            return
        }

        val attendees = maxAttendee.toFloat().toInt()
        if (attendees < 10) {
            failToast("Max attendees is too Low ! (Less than 10)")
            return
        }

        if (date < Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 24 * 7.5) {
            failToast("Event date should be 7 days or more later.")
            return
        }

        if (start["hour"]!! > end["hour"]!!) {
            failToast("Start and End times are invalid.")
            return
        }

        if (start["hour"]!! == end["hour"]!! && start["minute"]!! >= end["minute"]!!) {
            failToast("Start and End times are invalid.")
            return
        }

        val request = Event(
            UUID.randomUUID().toString(),
            App.user!!.uid,
            name,
            activity,
            description,
            date,
            start,
            end,
            maxAttendee.toFloat().toInt(),
            venues[venue]!!.uid,
            "PENDING"
        )

        loading = true

        Database
            .submitEventRequest(request)
            .addOnSuccessListener {
                clean()
                loading = false
            }.addOnFailureListener {
                clean()
                loading = false
            }

    }

    private fun clean() {
        name = ""
        date = 0L
        start = mapOf(
            "hour" to now.get(Calendar.HOUR_OF_DAY),
            "minute" to now.get(Calendar.MINUTE)
        )
        end = mapOf(
            "hour" to now.get(Calendar.HOUR_OF_DAY),
            "minute" to now.get(Calendar.MINUTE)
        )
        venue = 0
        maxAttendee = "100"
        activity = ""
        description = ""
    }

    private val now: Calendar = Calendar.getInstance()

    var name by mutableStateOf("")
    var date by mutableStateOf(0L)
    var start: Map<String, Int> by mutableStateOf(
        mapOf(
            "hour" to now.get(Calendar.HOUR_OF_DAY),
            "minute" to now.get(Calendar.MINUTE)
        )
    )
    var end: Map<String, Int> by mutableStateOf(
        mapOf(
            "hour" to now.get(Calendar.HOUR_OF_DAY),
            "minute" to now.get(Calendar.MINUTE)
        )
    )
    var venue by mutableStateOf(0)
    var maxAttendee by mutableStateOf("100")
    var activity by mutableStateOf("")
    var description by mutableStateOf("")

}