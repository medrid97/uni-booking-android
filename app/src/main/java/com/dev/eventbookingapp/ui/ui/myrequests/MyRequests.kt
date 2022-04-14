package com.dev.eventbookingapp.ui.ui.myrequests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import com.dev.eventbookingapp.ui.ui.components.TopBar

@Composable
fun PendingRequestCard(event: Event) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(MyGrey)
            .padding(16.dp)
    ) {
        Text(
            text = event.name,
            style = TextStyle(color = BlueDark, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        )
        Text(
            text = event.description,
            style = TextStyle(fontSize = 14.sp)
        )
        Text(
            text = "Pending ...",
            style = TextStyle(color = Color.DarkGray, fontStyle = FontStyle.Italic)
        )
    }
}

@Composable
fun MyRequestsView(appModel: AppModel, nav: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(text = "My Requests", nav)

        if (appModel.myRequests.size == 0) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "You didn't make any request for an event !",
                    style = TextStyle(
                        color = BlueDark,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Make a request in the Request tab.",
                    style = TextStyle(
                        color = Color.DarkGray,
                        fontStyle = FontStyle.Italic
                    )
                )
            }
        } else {
            LazyColumn {
                items(appModel.myRequests.size) {
                    val e = appModel.myRequests[it]

                    PendingRequestCard(event = e)
                }
            }
        }


    }

}