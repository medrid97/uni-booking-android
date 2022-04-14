package com.dev.eventbookingapp.ui.ui.login

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.AppModel
import com.dev.eventbookingapp.Screen

@Composable
fun InputField(
    value: String,
    onChanged: (String) -> Unit,
    password: Boolean = false,
    placeholder: String
) {

    TextField(
        value = value,
        onValueChange = { onChanged(it) },
        modifier = Modifier
            .background(
                color = Color.Transparent
            )
            .fillMaxWidth(),
        visualTransformation =
        if (password) PasswordVisualTransformation()
        else VisualTransformation.None,
        placeholder = { Text(text = placeholder) }
    )
}

@Composable
fun Login(
    navController: NavController,
    appModel: AppModel,
    viewModel: LoginViewModel = hiltViewModel()
) {

    Column(
        Modifier
            .fillMaxSize(1f),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (viewModel.isLoading) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = "Just a moment ...")
            }

        } else {
            Column(
                Modifier
                    .padding(
                        vertical = 20.dp,
                        horizontal = 10.dp
                    ),
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
                    text = "Login",
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

                if (viewModel.alert.isNotBlank()) {
                    Text(
                        text = viewModel.alert,
                        style = TextStyle(color = Color.Red),
                        textAlign = TextAlign.Center
                    )
                }

                Text(
                    text = "No account yet ? Sign up here !",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(Screen.SignupScreen.route)
                        })
                Button(
                    onClick = {
                        viewModel.login(navController, appModel)
                    }) {
                    Text(text = "Connect")
                }
            }
        }

    }
}