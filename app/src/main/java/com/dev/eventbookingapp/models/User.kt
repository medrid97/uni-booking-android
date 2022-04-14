package com.dev.eventbookingapp.models

data class User(val uid: String, var email: String, var isSuper: Boolean) {

    companion object {

        fun fromMap(map: Map<String, Any>): User? {

            if (
                map.containsKey("uid") &&
                map.containsKey("email") &&
                map.containsKey("isSuper")
            ) {

                val uid = map["uid"] as String
                val email = map["email"] as String
                val isSuper = map["isSuper"] as Boolean

                return User(uid, email, isSuper)
            }

            return null
        }

        fun toMap(uid: String, email: String, isSuper: Boolean): Map<String, Any> {
            return mapOf<String, Any>(
                "uid" to uid,
                "email" to email,
                "isSuper" to isSuper
            )
        }
    }

}