package com.dev.eventbookingapp.ui.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.eventbookingapp.Screen
import com.dev.eventbookingapp.ui.ui.components.TopBar

@Composable
fun Dashboard(navController: NavController, nav: NavController) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopBar(text = "Dashboard", nav)
        arrayListOf(
            Screen.EditVenues,
            Screen.EditEvents
        ).forEach { item ->
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .background(Color.LightGray)
                    .clickable {
                        navController.navigate(item.route)
                    }
                    .padding(horizontal = 10.dp, vertical = 20.dp)
            ) {
                Text(
                    text = item.name,
                    modifier = Modifier.padding(20.dp),
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )
            }
        }
    }
}