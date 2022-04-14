package com.dev.eventbookingapp.ui.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.dev.eventbookingapp.App
import com.dev.eventbookingapp.Screen
import com.dev.eventbookingapp.ui.theme.BlueDark

@Composable
fun TopBar(text: String, nav: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BlueDark)
            .padding(10.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text,
            style = TextStyle(
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
        )
        Text(
            text = "Sign out",
            style = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
            ),
            modifier = Modifier.clickable {
                nav.navigate(Screen.LoginScreen.route)
            }
        )
    }

}