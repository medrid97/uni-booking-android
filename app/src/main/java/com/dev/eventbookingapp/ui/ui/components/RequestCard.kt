package com.dev.eventbookingapp.ui.ui.components

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.models.Event
import com.dev.eventbookingapp.ui.theme.BlueDark
import com.dev.eventbookingapp.ui.theme.MyGrey
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
@Composable
fun RequestCard(
    request: Event,
    appModel: AppModel,
    onAccepted: () -> Unit,
    onRefused: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(5.dp)
            .background(MyGrey)
            .padding(10.dp)
    ) {

        val venue = appModel.venues.find { venue ->
            venue!!.uid == request.venue
        }

        Text(
            text = request.name,
            style = TextStyle(
                color = BlueDark,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        Text(
            text = "Activity : ${request.activity}",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
        Text(
            text = "Description : ${request.description}",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
        Text(
            text = "Venue: ${venue?.name ?: "Venue not found"}",
            style = TextStyle(
                color = if (venue !== null) Color.Black else Color.Red,
                fontStyle = FontStyle.Italic
            )
        )
        Text(
            text = "Date : ${SimpleDateFormat("dd.MM.yyyy").format(Date(request.date))}",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
        Text(
            text = "Starts ${request.start["hour"]}:${request.start["minute"]}",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )
        Text(
            text = "Ends ${request.end["hour"]}:${request.end["minute"]}",
            style = TextStyle(fontStyle = FontStyle.Italic)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(onClick = { onAccepted() }) {
                Text(text = "Accept")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { onRefused() }) {
                Text(text = "Refuse")
            }
        }
    }
}