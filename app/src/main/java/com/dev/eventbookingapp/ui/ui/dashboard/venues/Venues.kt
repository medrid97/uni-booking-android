package com.dev.eventbookingapp.ui.ui.dashboard.venues

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle.Companion.Italic
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.models.Venue
import com.dev.eventbookingapp.ui.theme.BlueDark
import com.dev.eventbookingapp.ui.theme.MyGrey
import com.dev.eventbookingapp.ui.ui.components.TopBar

@Composable
fun VenuesView(
    venues: ArrayList<Venue?>,
    viewModel: VenuesViewModel = hiltViewModel(),
    nav: NavController
) {

    val context = LocalContext.current

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                backgroundColor = BlueDark,
                onClick = {
                    viewModel.openCloseDialog()
                },
            ) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "Add venue",
                    tint = Color.White
                )
            }
        }) {


        @Composable
        fun VenueItem(venue: Venue?) {

            if (venue !== null) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth(1f)
                        .background(MyGrey)
                        .border(
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(0.dp, Color.Transparent)
                        )
                        .padding(all = 10.dp)

                ) {
                    Text(
                        text = venue.name,
                        style = TextStyle(
                            fontSize = 25.sp,
                            color = BlueDark,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Capacity : ${venue.capacity} persons",
                        style = TextStyle(fontStyle = Italic)
                    )
                    Text(
                        text = "Surface : ${venue.surface} m²",
                        style = TextStyle(fontStyle = Italic)
                    )
                    Text(
                        text = "Preferred activity: ${venue.activity}",
                        style = TextStyle(fontStyle = Italic)
                    )
                }
            }

        }

        Column {
            TopBar(text = "Manage venues", nav)
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(venues.size) { i ->
                    VenueItem(venues[i])
                }
            }
        }
        if (viewModel.showDialog) {

            var name by remember { mutableStateOf("") }
            var capacity by remember { mutableStateOf(0) }
            var activity by remember { mutableStateOf("") }
            var surface by remember { mutableStateOf(10f) }

            var loading by remember { mutableStateOf(false) }

            @Composable
            fun InputField(
                value: String,
                onChange: (newVal: String) -> Unit,
                type: KeyboardType,
                placeholder: String
            ) {
                TextField(
                    value = value,
                    onValueChange = { onChange(it) },
                    keyboardOptions = KeyboardOptions(keyboardType = type),
                    placeholder = { Text(text = placeholder) },
                    modifier = Modifier.padding(bottom = 10.dp)
                )
            }

            Dialog(
                onDismissRequest = {
                    viewModel.openCloseDialog()
                },
                content = {
                    Column(
                        modifier = Modifier
                            .background(Color.White)
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (loading) {
                            CircularProgressIndicator()
                            Text(text = "Creating Venue...")
                        } else {
                            Text(
                                text = "Add new Venue",
                                modifier = Modifier.padding(10.dp)
                            )
                            InputField(
                                value = name,
                                onChange = { name = it },
                                KeyboardType.Text,
                                placeholder = "venue's name"
                            )
                            InputField(
                                value = capacity.toString(),
                                onChange = {
                                    capacity = if (it.trim().isEmpty()) 0 else it.toFloat().toInt()
                                },
                                KeyboardType.Number,
                                placeholder = "capacity"
                            )
                            InputField(
                                value = activity,
                                onChange = { activity = it },
                                KeyboardType.Text,
                                placeholder = "preferred activity"
                            )
                            InputField(
                                value = surface.toString(),
                                onChange = { surface = if (it.isNotBlank()) it.toFloat() else 0f },
                                KeyboardType.Number,
                                placeholder = "venue's surface"
                            )
                            Row {
                                Button(
                                    onClick = {
                                        if (name.length < 3) {
                                            Toast.makeText(
                                                context,
                                                "Name is too short (Less than 3) !",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return@Button
                                        }
                                        if (capacity < 10) {
                                            Toast.makeText(
                                                context,
                                                "Capacity is too low (Less than 10) !",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return@Button
                                        }
                                        if (activity.length < 3) {
                                            Toast.makeText(
                                                context,
                                                "Activity name is too short (Less than 3 !",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return@Button
                                        }
                                        if (surface < 10) {
                                            Toast.makeText(
                                                context,
                                                "Surface is too low (Less than 10) !",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            return@Button
                                        }


                                        val venue = Venue(
                                            "empty",
                                            name,
                                            capacity,
                                            surface,
                                            activity
                                        )

                                        if (!viewModel.venueAlreadyExists(venue, venues)) {
                                            loading = true

                                            viewModel
                                                .createVenue(venue)
                                                .addOnSuccessListener {
                                                    loading = false
                                                    viewModel.openCloseDialog()
                                                }.addOnFailureListener {
                                                    loading = false
                                                    Toast.makeText(
                                                        context,
                                                        "Unable to create Venue...",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                        } else {
                                            Toast.makeText(
                                                context,
                                                "Venue already exist !",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }

                                    },
                                    modifier = Modifier.padding(end = 10.dp)
                                ) {
                                    Text(text = "Create")
                                }
                                Button(
                                    onClick = {
                                        viewModel.openCloseDialog()
                                    }) {
                                    Text(text = "Cancel")
                                }
                            }

                        }

                    }
                })
        }
    }
}