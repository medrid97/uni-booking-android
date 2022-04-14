package com.dev.eventbookingapp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.dev.eventbookingapp.ui.ui.components.BottomNavBar
import com.dev.eventbookingapp.ui.ui.dashboard.Dashboard
import com.dev.eventbookingapp.ui.ui.dashboard.events.EventsView
import com.dev.eventbookingapp.ui.ui.dashboard.venues.VenuesView
import com.dev.eventbookingapp.ui.ui.home.Home
import com.dev.eventbookingapp.ui.ui.login.Login
import com.dev.eventbookingapp.ui.ui.myparticipation.MyParticipationView
import com.dev.eventbookingapp.ui.ui.myrequests.MyRequestsView
import com.dev.eventbookingapp.ui.ui.request.Request
import com.dev.eventbookingapp.ui.ui.signup.Signup

@Preview
@Composable
fun Navigation(viewModel: AppModel = hiltViewModel()) {
    val navController = rememberNavController()

    Scaffold(
        bottomBar = { BottomNavBar(navController) },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.padding(bottom = it.calculateBottomPadding())) {
            NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
                composable(route = Screen.LoginScreen.route) {
                    Login(navController, viewModel)
                }
                composable(route = Screen.SignupScreen.route) {
                    Signup(navController, viewModel)
                }
                composable(route = Screen.HomeScreen.route) {
                    Home(appModel = viewModel, nav = navController)
                }
                composable(route = Screen.RequestScreen.route) {
                    Request(viewModel.venues, nav = navController)
                }
                composable(route = Screen.DashboardScreen.route) {
                    Dashboard(navController, nav = navController)
                }
                composable(route = Screen.EditVenues.route) {
                    VenuesView(viewModel.venues, nav = navController)
                }
                composable(route = Screen.EditEvents.route) {
                    EventsView(viewModel, nav = navController)
                }
                composable(route = Screen.MyRequests.route) {
                    MyRequestsView(viewModel, nav = navController)
                }
                composable(route = Screen.MyParticipation.route) {
                    MyParticipationView(viewModel, nav = navController)
                }
            }
        }

    }

}
