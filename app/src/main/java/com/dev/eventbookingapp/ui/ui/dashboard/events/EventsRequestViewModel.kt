package com.dev.eventbookingapp.ui.ui.dashboard.events

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.service.Database
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EventsRequestViewModel @Inject constructor() : ViewModel() {

    var isLoading by mutableStateOf(false)

    fun acceptRequest(request: Event) {

        isLoading = true

        Database.acceptEventRequest(request)
            .addOnSuccessListener {
                isLoading = false
            }.addOnFailureListener {
                isLoading = false
            }
    }

    fun refuseRequest(request: Event) {
        isLoading = true

        Database.refuseEventRequest(request)
            .addOnSuccessListener {
                isLoading = false
            }.addOnFailureListener {
                isLoading = false
            }
    }

}