package com.dev.eventbookingapp

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val icon: ImageVector, val name: String) {
    object LoginScreen : Screen("login", Icons.Filled.Home, "Login")
    object SignupScreen : Screen("signup", Icons.Filled.Home, "Sign Up")
    object HomeScreen : Screen("home", Icons.Filled.Home, "Home")
    object DashboardScreen : Screen("dashboard", Icons.Filled.List, "Dashboard")
    object RequestScreen : Screen("request", Icons.Filled.Add, "Make a Request")
    object EditVenues : Screen("edit_venues",Icons.Filled.Home,"Manage Venues")
    object EditEvents : Screen("edit_events",Icons.Filled.Check,"Manage Event Requests")
    object MyRequests: Screen("my_requests",Icons.Filled.Send,"My Requests")
    object MyParticipation: Screen("my_participation",Icons.Filled.Done,"My Participation")
}
