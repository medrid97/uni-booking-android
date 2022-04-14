package com.dev.eventbookingapp

import android.app.Application
import com.dev.eventbookingapp.models.User
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application() {

    companion object {
        var user: User? = null
    }

}