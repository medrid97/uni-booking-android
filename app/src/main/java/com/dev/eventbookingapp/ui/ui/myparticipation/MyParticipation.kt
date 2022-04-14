package com.dev.eventbookingapp.ui.ui.myparticipation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.ui.theme.BlueDark
import com.dev.eventbookingapp.ui.theme.MyGrey
import com.dev.eventbookingapp.ui.ui.components.LoadingDialog
import com.dev.eventbookingapp.ui.ui.components.TopBar
import com.dev.eventbookingapp.util.formatDate
import com.dev.eventbookingapp.util.formatTime
import com.dev.eventbookingapp.util.venueName

@Composable
fun MyParticipationView(appModel: AppModel, nav: NavController) {

    val context = LocalContext.current
    val loading = remember { mutableStateOf(false) }

    @Composable
    fun ParticipationCard(event: Event) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(MyGrey)
                .padding(16.dp)
        ) {
            Text(
                text = event.name,
                style = TextStyle(color = BlueDark, fontSize = 25.sp, fontWeight = FontWeight.Bold)
            )
            Text(
                text = "${
                    venueName(
                        event.venue,
                        appModel.venues
                    )
                } • ${formatDate(event.date)} • ${formatTime(event.start)} - ${formatTime(event.end)}",
                style = TextStyle(
                    color = Color.DarkGray,
                    fontSize = 14.sp,
                    fontStyle = FontStyle.Italic
                )
            )
            Text(
                text = event.description,
                style = TextStyle(fontSize = 16.sp)
            )
            Button(
                onClick = {
                    loading.value = true
                    appModel.unJoinEvent(event, context) { loading.value = false }
                },
            ) {
                Text(text = "Leave")
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(text = "My Participation", nav)

        if (loading.value) {
            LoadingDialog(text = "Processing your request ...")
        } else {
            if (appModel.myParticipation.size == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "You didn't join any event yet !",
                        style = TextStyle(
                            color = BlueDark,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Browse events by clicking the Home tab",
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }

            } else {
                LazyColumn {
                    items(appModel.myParticipation.size) {
                        ParticipationCard(event = appModel.myParticipation[it])
                    }
                }
            }

        }
    }
}