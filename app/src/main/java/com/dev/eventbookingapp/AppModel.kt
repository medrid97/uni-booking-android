package com.dev.eventbookingapp

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.models.Venue
import com.dev.eventbookingapp.service.Database
import com.dev.eventbookingapp.service.Services
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@SuppressLint("MutableCollectionMutableState")
@HiltViewModel
class AppModel @Inject constructor() : ViewModel() {

    var venues by mutableStateOf(ArrayList<Venue?>())
    var requests by mutableStateOf(ArrayList<Event>())
    var events by mutableStateOf(ArrayList<Event>())
    var reqSubscription by mutableStateOf(false)
    var myRequests by mutableStateOf(ArrayList<Event>())
    var myParticipation by mutableStateOf(ArrayList<Event>())

    init {
        Database.subscribeToVenues(
            onAdded = { doc ->
                venues.add(Venue.fromMap(doc.id, doc.data))
            },
            onModified = { doc ->
                val mapped = venues.map { item ->
                    if (item!!.uid == doc.id) {
                        Venue.fromMap(doc.id, doc.data)
                    } else
                        item
                }

                venues = ArrayList(mapped)
            },
            onRemoved = {
                val filtered = venues.filter { item ->
                    item!!.uid != it.id
                }

                venues = ArrayList(filtered)
            })

        Database.subscribeToEvents(
            onAdded = { doc ->
                val ev = Event.fromMap(doc.id, doc.data)!!

                events.add(ev)

                Database.subscribeToParticipant(
                    ev,
                    onAdded = { pDoc ->

                        events = ArrayList(
                            events.map { item ->
                                if (item.uid == ev.uid) {
                                    item.participants.add(pDoc.id)
                                    item
                                } else item
                            }
                        )

                    },
                    onRemoved = { pDoc ->

                        events = ArrayList(
                            events.map { item ->
                                if (item.uid == ev.uid) {
                                    item.participants.remove(pDoc.id)
                                    item
                                } else item
                            }
                        )
                    },
                    onModified = {}
                )

            },
            onModified = { doc ->
                val mapped = events.map { item ->
                    if (item.uid == doc.id) Event.fromMap(doc.id, doc.data)!!
                    else item
                }

                events = ArrayList(mapped)
            },
            onRemoved = { doc ->
                val filtered = events.filter { item ->
                    item.uid != doc.id
                }

                events = ArrayList(filtered)
            }
        )

    }

    fun subscribeToRequests() {
        if (!reqSubscription) {
            reqSubscription = true

            Database.subscribeToRequests(
                { doc ->
                    requests.add(Event.fromMap(doc.id, doc.data)!!)
                }, { doc ->
                    val mapped = requests.map { item ->
                        if (item.uid == doc.id) Event.fromMap(doc.id, doc.data)!!
                        else item
                    }

                    requests = ArrayList(mapped)
                }, { doc ->
                    val filtered = requests.filter { item ->
                        item.uid != doc.id
                    }

                    requests = ArrayList(filtered)
                }
            )
        }
    }

    fun subscribeToMyRequests() {
        myRequests = ArrayList()

        if (App.user != null) {
            Database.subscribeToMyRequests(
                App.user!!,
                { doc ->
                    if (doc.exists()) {
                        myRequests.add(doc.data?.let { Event.fromMap(doc.id, it) }!!)
                    }

                    Log.d("fire","$myRequests")
                },
                {
                    myRequests = ArrayList(myRequests.filter { doc -> doc.uid != it.id })
                }
            )
        }

    }

    fun subscribeToMyParticipation() {
        myParticipation = ArrayList()

        if (App.user != null) {
            Database.subscribeToMyParticipation(
                App.user!!,
                { doc ->
                    if (doc.exists()) {
                        myParticipation.add(doc.data?.let { Event.fromMap(doc.id, it) }!!)
                    }
                },
                {
                    myParticipation = ArrayList(myParticipation.filter { doc -> doc.uid != it.id })
                }
            )
        }

    }

    fun joinEvent(event: Event, context: Context, onCompleted: () -> Unit) {
        if (event.user == App.user!!.uid) {
            Services.alertToast("You are the creator of the event !", context)
            return
        }

        if (event.participants.find { p -> p == App.user!!.uid } != null) {
            Services.alertToast("Already Joined !", context)
            return
        }

        if (event.participants.size == event.maxAttendees) {
            Services.alertToast("There are no place left !", context)
            return
        }

        Database.checkUserParticipation(
            App.user!!,
            event,
            {
                Services.alertToast("Already Joined !", context)
                onCompleted()
            },
            {
                Database
                    .participateInEvent(App.user!!, event)
                    .addOnSuccessListener {
                        Services.alertToast("Successfully joined the event !", context)
                    }
                    .addOnSuccessListener {
                        onCompleted()
                    }
            }
        )

    }

    fun unJoinEvent(event: Event, context: Context, onCompleted: () -> Unit) {
        Database
            .removeParticipationFromEvent(App.user!!, event)
            .addOnSuccessListener {
                Services.alertToast("Removed participation from the event !", context)
            }
            .addOnSuccessListener {
                onCompleted()
            }
    }

}