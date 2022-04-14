package com.dev.eventbookingapp.models

import android.util.Log
import java.lang.Exception
import kotlin.reflect.typeOf

data class Event(
    var uid: String,
    var user: String,
    var name: String,
    var activity: String,
    var description: String,
    var date: Long,
    var start: Map<String, Int>,
    var end: Map<String, Int>,
    var maxAttendees: Int,
    var venue: String,
    var state: String,
    var participants: ArrayList<String> = ArrayList()
) {

    fun toMap(): Map<String, Any> {
        return mapOf(
            "user" to user,
            "name" to name,
            "activity" to activity,
            "description" to description,
            "date" to date,
            "start" to start,
            "end" to end,
            "maxAttendees" to maxAttendees,
            "venue" to venue,
            "state" to state
        )
    }

    companion object {
        fun fromMap(uid: String, data: Map<String, Any>): Event? {
            if (
                data.containsKey("user") &&
                data.containsKey("name") &&
                data.containsKey("activity") &&
                data.containsKey("description") &&
                data.containsKey("date") &&
                data.containsKey("start") &&
                data.containsKey("end") &&
                data.containsKey("maxAttendees") &&
                data.containsKey("venue") &&
                data.containsKey("state")
            ) {

                val maxAttendees =
                    if (data["maxAttendees"]!!::class.java == String::class.java)
                        (data["maxAttendees"] as String).toInt()
                    else
                        (data["maxAttendees"] as Number).toInt()

                return Event(
                    uid,
                    data["user"] as String,
                    data["name"] as String,
                    data["activity"] as String,
                    data["description"] as String,
                    data["date"] as Long,
                    data["start"] as Map<String, Int>,
                    data["end"] as Map<String, Int>,
                    maxAttendees,
                    data["venue"] as String,
                    data["state"] as String
                )
            }
            return null
        }
    }

}