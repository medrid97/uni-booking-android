package com.dev.eventbookingapp.ui.ui.dashboard.venues

import android.annotation.SuppressLint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dev.eventbookingapp.models.Venue
import com.dev.eventbookingapp.service.Database
import com.google.android.gms.tasks.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class VenuesViewModel @Inject constructor() : ViewModel() {

    var showDialog by mutableStateOf(false)

    fun openCloseDialog() {
        this.showDialog = !showDialog
    }

    fun venueAlreadyExists(venue: Venue, venues: ArrayList<Venue?>): Boolean {
        val res = venues.find { item ->
            item!!.name.lowercase() == venue.name.trim().lowercase()
        }

        return res != null
    }

    fun createVenue(venue: Venue): Task<Void> {
        return Database.addVenue(venue)
    }
}