package com.dev.eventbookingapp.ui.ui.signup

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.Screen
import com.dev.eventbookingapp.ui.ui.login.InputField


@Composable
fun Signup(
    navController: NavController,
    appModel: AppModel,
    viewModel: SignupViewModel = hiltViewModel()
) {

    Column(
        Modifier
            .fillMaxSize(1f),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (viewModel.isLoading) {
            CircularProgressIndicator()
        } else {
            Column(
                Modifier.padding(vertical = 20.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "UniBooking",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "Sign up",
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                InputField(
                    viewModel.email,
                    viewModel::updateEmail,
                    false,
                    "Email"
                )
                InputField(
                    viewModel.password,
                    viewModel::updatePassword,
                    true,
                    "Password"
                )
                if (viewModel.alert != "") {
                    Text(
                        text = viewModel.alert,
                        style = TextStyle(
                            color = Color.Red
                        )
                    )
                }
                Text(
                    text = "Already have an account ? Login here !",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.LoginScreen.route)
                        })
                Button(
                    onClick = {
                        viewModel.tryCreateAccount(navController, appModel)
                    }) {
                    Text(
                        text = "Create Account"
                    )
                }
            }
        }


    }
}