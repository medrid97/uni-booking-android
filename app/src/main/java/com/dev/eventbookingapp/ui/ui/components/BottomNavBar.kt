package com.dev.eventbookingapp.ui.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.dev.eventbookingapp.App
import com.dev.eventbookingapp.Screen
import com.dev.eventbookingapp.ui.theme.BlueLight

@Composable
fun BottomNavBar(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    if (App.user !== null && !arrayListOf(
            Screen.SignupScreen.route, Screen.LoginScreen.route
        )
            .contains(currentRoute)
    ) {

        val items = arrayListOf(
            Screen.HomeScreen,
            Screen.RequestScreen,
            Screen.MyParticipation,
            Screen.MyRequests
        )

        if (App.user!!.isSuper) {
            items.add(Screen.DashboardScreen)
        }

        BottomNavigation {
            items.forEach { item ->
                BottomNavigationItem(
                    selected = currentRoute === item.route,
                    onClick = { navController.navigate(item.route) },
                    icon = { Icon(imageVector = item.icon, contentDescription = item.name) },
                    selectedContentColor = BlueLight,
                    unselectedContentColor = Color.White,
                    alwaysShowLabel = true,
                    label = { Text(text = item.name, style = TextStyle(fontSize = 7.sp)) }
                )
            }
        }

    }

}