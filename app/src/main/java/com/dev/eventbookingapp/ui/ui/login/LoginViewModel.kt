package com.dev.eventbookingapp.ui.ui.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.service.Services
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor() : ViewModel() {

    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var alert by mutableStateOf("")

    fun updateEmail(value: String) {
        email = value
    }

    fun updatePassword(value: String) {
        password = value
    }

    fun login(navController: NavController, appModel: AppModel) {

        alert = ""
        var errors = ""

        if (email.trim().length < 6) {
            errors += "Email is too short\n"
        }

        if (password.trim().length < 6) {
            errors += "Password is too short\n"
        }

        if (errors.isNotBlank()) {
            alert = errors.substring(0, errors.length - 1)
            return
        }

        isLoading = true

        Services.login(
            email.trim(),
            password.trim(),
            navController,
            appModel,
            { error ->
                alert = error.message!!
                isLoading = false
            },
            {
                isLoading = false
            }
        )
    }

}