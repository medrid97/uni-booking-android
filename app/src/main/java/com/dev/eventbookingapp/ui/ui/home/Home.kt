package com.dev.eventbookingapp.ui.ui.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.ui.ui.components.EventCard
import com.dev.eventbookingapp.ui.ui.components.LoadingDialog
import com.dev.eventbookingapp.ui.ui.components.TopBar
import java.util.*

@Composable
fun Home(
    viewModel: HomeViewModel = hiltViewModel(),
    appModel: AppModel,
    nav: NavController
) {

    if (viewModel.isLoading) {
        LoadingDialog(text = "Processing ...")
    }

    val filteredEvents =
        appModel.events
            .filter { ev -> ev.date > Calendar.getInstance().timeInMillis }
            .sortedBy { s -> s.date }

    Column {
        TopBar(text = "Home", nav)
        LazyColumn {

            items(filteredEvents.size) { i ->
                val event = filteredEvents[i]
                EventCard(
                    event = event,
                    appModel = appModel,
                    homeModel = viewModel
                )
            }
        }
    }


}