package com.dev.eventbookingapp.ui.ui.dashboard.events

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.ui.theme.BlueDark
import com.dev.eventbookingapp.ui.ui.components.RequestCard
import com.dev.eventbookingapp.ui.ui.components.TopBar

@Composable
fun EventsView(
    appModel: AppModel,
    viewModel: EventsRequestViewModel = hiltViewModel(),
    nav: NavController
) {

    if (viewModel.isLoading) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(20.dp))
            Text(text = "Committing changes...")
        }
    } else
        Column {
            TopBar(text = "Manage event requests", nav)

            if (appModel.requests.size == 0) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "All event requests have been processed !",
                        style = TextStyle(
                            color = BlueDark,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Good Job",
                        style = TextStyle(
                            color = Color.DarkGray,
                            fontStyle = FontStyle.Italic
                        )
                    )
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    items(appModel.requests.size) { i ->
                        RequestCard(
                            request = appModel.requests[i],
                            appModel = appModel,
                            onAccepted = { viewModel.acceptRequest(appModel.requests[i]) },
                            onRefused = { viewModel.refuseRequest(appModel.requests[i]) }
                        )
                    }
                }
            }

        }

}