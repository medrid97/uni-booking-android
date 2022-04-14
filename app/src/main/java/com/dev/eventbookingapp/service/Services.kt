package com.dev.eventbookingapp.service

import android.content.Context
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import com.dev.eventbookingapp.App
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.Screen
import com.dev.eventbookingapp.models.User

abstract class Services {

    companion object {

        fun login(
            email: String,
            password: String,
            navController: NavController,
            appModel: AppModel,
            onFailure: (e: Exception) -> Unit,
            onSuccess: () -> Unit
        ) {
            Database.userAuth(email, password)
                .addOnSuccessListener {

                    Database.getUserData(it.user!!.uid)
                        .addOnSuccessListener { user ->
                            App.user = User.fromMap(user.data!!)

                            if (App.user!!.isSuper) {
                                appModel.subscribeToRequests()
                            }

                            onSuccess()

                            appModel.subscribeToMyParticipation()
                            appModel.subscribeToMyRequests()

                            navController.navigate(Screen.HomeScreen.route)
                        }
                        .addOnFailureListener { error ->
                            onFailure(error)
                        }
                }
                .addOnFailureListener { error ->
                    onFailure(error)
                }
        }

        fun alertToast(text: String, context: Context) {
            Toast.makeText(context, text, Toast.LENGTH_LONG).show()
        }
    }

}