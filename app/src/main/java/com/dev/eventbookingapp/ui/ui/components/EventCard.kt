package com.dev.eventbookingapp.ui.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.eventbookingapp.App
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.models.Venue
import com.dev.eventbookingapp.ui.theme.BlueDark
import com.dev.eventbookingapp.ui.theme.MyGrey
import com.dev.eventbookingapp.ui.ui.home.HomeViewModel
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat", "MutableCollectionMutableState")
@Composable
fun EventCard(event: Event, appModel: AppModel, homeModel: HomeViewModel) {

    val context = LocalContext.current

    val count = remember { mutableStateOf(0) }

    fun getVenue(): Venue? {
        return appModel.venues.find {
            it!!.uid == event.venue
        }
    }

    fun formatTime(time: Map<String, Int>): String {

        val hour = time["hour"]!!.toInt()
        val minute = time["minute"]!!.toInt()

        val hourString = if (hour < 10) "0$hour" else hour.toString()
        val minuteString = if (minute < 10) "0$minute" else minute.toString()

        return "$hourString:$minuteString"
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MyGrey)
            .padding(16.dp)
            .clickable {
                count.value = count.value++
            }
    ) {
        Text(
            text = event.name,
            style = TextStyle(color = BlueDark, fontSize = 24.sp)
        )
        Text(
            text = "${getVenue()?.name ?: "Loading"} • ${
                SimpleDateFormat("dd.MM.yyyy").format(
                    Date(
                        event.date
                    )
                )
            } • ${formatTime(event.start)}",
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.Light
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = event.description)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "${event.participants.size}/${event.maxAttendees}")
        Spacer(modifier = Modifier.height(8.dp))

        if (event.user != App.user!!.uid) {
            if (event.participants.find { p -> p == App.user!!.uid } != null) {
                Button(
                    onClick = {
                        homeModel.isLoading = true
                        appModel.unJoinEvent(event, context) {
                            homeModel.isLoading = false
                        }
                    }
                ) {
                    Text(text = "Leave")
                }
            } else {
                Button(
                    onClick = {
                        homeModel.isLoading = true
                        appModel.joinEvent(event, context) {
                            homeModel.isLoading = false
                        }
                    }
                ) {
                    Text(text = "Join")
                }
            }
        }


    }
}