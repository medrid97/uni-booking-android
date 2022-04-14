package com.dev.eventbookingapp.ui.ui.request

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.dev.eventbookingapp.models.Venue
import com.dev.eventbookingapp.ui.theme.BlueDark
import com.dev.eventbookingapp.ui.ui.components.LoadingDialog
import com.dev.eventbookingapp.ui.ui.components.TopBar
import com.dev.eventbookingapp.ui.ui.login.InputField
import java.util.*

@Composable
fun DatePicker(context: Context, onPicked: (year: Int, month: Int, day: Int) -> Unit) {

    val year: Int
    val month: Int
    val day: Int

    val calendar = Calendar.getInstance()
    year = calendar.get(Calendar.YEAR)
    month = calendar.get(Calendar.MONTH)
    day = calendar.get(Calendar.DAY_OF_MONTH)
    calendar.time = Date()

    val date = remember { mutableStateOf("Select a date") }
    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, pYear: Int, pMonth: Int, pDay: Int ->
            date.value = "$pDay/${pMonth + 1}/$pYear"
            onPicked(pYear, pMonth, pDay)
        }, year, month, day
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {

        Text(text = "Selected Date: ${date.value}")
        Spacer(modifier = Modifier.size(4.dp))
        Button(onClick = {
            datePickerDialog.show()
        }) {
            Text(text = "Pick a day")
        }
    }

}

@Composable
fun TimePicker(context: Context, text: String, onPicked: (hour: Int, minute: Int) -> Unit) {

    val time = remember { mutableStateOf("Select time") }
    val hour = remember { mutableStateOf(Calendar.getInstance()[Calendar.HOUR_OF_DAY]) }
    val minute = remember { mutableStateOf(Calendar.getInstance()[Calendar.MINUTE]) }

    val timePicker = TimePickerDialog(
        context, { _, pHour: Int, pMinute: Int ->
            time.value = "$pHour:$pMinute"
            hour.value = pHour
            minute.value = pMinute
            onPicked(pHour, pMinute)
        },
        hour.value,
        minute.value,
        true
    )

    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = text)
        Text(text = time.value)
        Spacer(modifier = Modifier.size(4.dp))
        Button(onClick = {
            timePicker.show()
        }) {
            Text(text = "Pick a time")
        }
    }
}

@Composable
fun InputNumber(value: String, onChanged: (newVal: String) -> Unit, placeholder: String) {
    TextField(
        value = value,
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { onChanged(it) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        placeholder = { Text(text = placeholder) }
    )
}

@Composable
fun SelectVenue(venues: ArrayList<Venue?>, viewModel: RequestViewModel) {
    Column {
        Text(text = "Select a venue")
        LazyColumn(
            modifier = Modifier
                .padding(16.dp)
                .background(Color.White)
                .padding(16.dp)

        ) {
            items(venues.size) { i ->
                Column(modifier = Modifier
                    .clickable {
                        viewModel.venue = i
                        viewModel.toggleVenuesList()
                    }
                    .padding(8.dp)
                    .fillMaxWidth()) {
                    Text(
                        text = venues[i]!!.name,
                        style = TextStyle(color = BlueDark, fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Capacity: ${venues[i]!!.capacity} persons",
                        style = TextStyle(fontStyle = FontStyle.Italic)
                    )
                    Text(
                        text = "Surface: ${venues[i]!!.surface} m²",
                        style = TextStyle(fontStyle = FontStyle.Italic)
                    )
                }

            }
        }
    }

}

@Composable
fun Request(venues: ArrayList<Venue?>, viewModel: RequestViewModel = hiltViewModel(), nav: NavController) {

    val context = LocalContext.current

    TopBar(text = "Make a request",nav)

    if (viewModel.loading) {
        LoadingDialog(text = "Submitting your request")
    } else {
        if (viewModel.showVenuesList) {
            Dialog(onDismissRequest = {
                viewModel.toggleVenuesList()
            }) {
                SelectVenue(venues = venues, viewModel = viewModel)
            }
        }

        Column(
            modifier = Modifier
                .verticalScroll(ScrollState(0), true)
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.size(50.dp))
            InputField(
                value = viewModel.name,
                onChanged = { viewModel.name = it },
                placeholder = "Name"
            )
            Spacer(modifier = Modifier.size(8.dp))
            InputField(
                value = viewModel.activity,
                onChanged = { viewModel.activity = it },
                placeholder = "Activity"
            )
            Spacer(modifier = Modifier.size(8.dp))
            InputField(
                value = viewModel.description,
                onChanged = { viewModel.description = it },
                placeholder = "Description"
            )
            Spacer(modifier = Modifier.size(8.dp))
            InputNumber(
                value = viewModel.maxAttendee,
                onChanged = {
                    viewModel.maxAttendee = it
                },
                placeholder = "Max attendees"
            )
            Spacer(modifier = Modifier.size(8.dp))
            DatePicker(
                context = context,
                onPicked = { year: Int, month: Int, day: Int ->
                    viewModel.date = GregorianCalendar(year, month, day).timeInMillis
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
            TimePicker(
                context = context,
                text = "Start time",
                onPicked = { hour: Int, minute: Int ->
                    viewModel.start = mapOf("hour" to hour, "minute" to minute)
                })
            Spacer(modifier = Modifier.size(8.dp))
            TimePicker(
                context = context,
                text = "End time",
                onPicked = { hour: Int, minute: Int ->
                    viewModel.end = mapOf("hour" to hour, "minute" to minute)
                })
            Spacer(modifier = Modifier.size(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Venue: ${if (venues.size > 0) venues[viewModel.venue]!!.name else "Loading..."}")
                Button(
                    onClick = { viewModel.toggleVenuesList() }) {
                    Text(
                        text = "Select Venue"
                    )
                }
            }
            Spacer(modifier = Modifier.size(8.dp))
            Button(onClick = {
                viewModel.submitRequest(context, venues)
            }) {
                Text(text = "Submit request")
            }
        }
    }


}