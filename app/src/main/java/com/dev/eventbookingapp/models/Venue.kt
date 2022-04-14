package com.dev.eventbookingapp.models

import android.util.Log

data class Venue(
    val uid: String,
    val name: String,
    val capacity: Int,
    val surface: Float,
    val activity: String
) {
    companion object {
        fun fromMap(uid: String, map: Map<String, Any>): Venue? {

            val cap = if (map["capacity"]!!::class.java == String::class.java){
                (map["capacity"] as String).toInt()
            } else {
                (map["capacity"] as Long).toInt()
            }

            Log.d("fire", "$map")

            return if (
                map.containsKey("name") &&
                map.containsKey("capacity") &&
                map.containsKey("activity") &&
                map.containsKey("surface")
            )

                Venue(
                    uid,
                    map["name"] as String,
                    cap,
                    // (map["capacity"] as Long).toInt(),
                    (map["surface"] as Number).toFloat(),
                    map["activity"] as String
                )
            else null
        }

        fun toMap(venue: Venue): Map<String, Any> {
            return mapOf(
                "name" to venue.name,
                "capacity" to venue.capacity,
                "activity" to venue.activity,
                "surface" to venue.surface
            )
        }
    }
}
