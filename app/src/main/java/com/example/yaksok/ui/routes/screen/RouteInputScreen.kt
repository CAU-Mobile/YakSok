package com.example.yaksok.ui.routes.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import com.example.yaksok.ui.places.dialog.PlaceSearchResultsDialog
import com.example.yaksok.ui.places.viewModel.PlacesViewModel
import java.util.Calendar
import java.util.TimeZone

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteInputScreen(
    origin: String,
    destination: String,
    placeViewModel: PlacesViewModel,
    onOriginChange: (String) -> Unit,
    onDestinationChange: (String) -> Unit,
    onTimeChange: (Int, Pair<Int, Int>) -> Unit,
    onSearchClicked: (String, String, String, Int, Pair<Int, Int>) -> Unit
) {
    var currentOrigin by remember { mutableStateOf(origin) }
    var currentDestination by remember { mutableStateOf(destination) }
    var selectedMode by remember { mutableStateOf("transit") }
    var timeSelection by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }
    var selectedTime by remember { mutableStateOf<Pair<Int, Int>?>(null) }

    val focusManager = LocalFocusManager.current

    val showPlaceDialog by placeViewModel.showPlaceDialog.collectAsState()
    val placeSelectionText by placeViewModel.placeSelectionText.collectAsState()

    LaunchedEffect(Unit) {
        placeViewModel.closePlaceDialog()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                focusManager.clearFocus()
            },
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = currentOrigin,
            onValueChange = {
                currentOrigin = it
                onOriginChange(it)
            },
            label = { Text("출발지") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && currentOrigin.isNotBlank()) {
                        placeViewModel.searchPlacesForRoute(
                            currentOrigin,
                            isOrigin = true
                        )
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(223, 242, 235),
                focusedIndicatorColor = Color(112, 178, 211),
                unfocusedIndicatorColor = Color(185, 229, 232)
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = currentDestination,
            onValueChange = {
                currentDestination = it
                onDestinationChange(it)
            },
            label = { Text("목적지") },
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    if (!focusState.isFocused && currentDestination.isNotBlank()) {
                        placeViewModel.searchPlacesForRoute(
                            currentDestination,
                            isOrigin = false
                        )
                    }
                },
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color(223, 242, 235),
                focusedIndicatorColor = Color(112, 178, 211),
                unfocusedIndicatorColor = Color(185, 229, 232)
            )
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            TimeSelectionButton(
                currentSelection = timeSelection,
                onSelectionChange = {
                    timeSelection = it
                    if (it != 0) showTimePicker = true

                    if (selectedTime != null) {
                        onTimeChange(
                            timeSelection,
                            Pair(selectedTime!!.first, selectedTime!!.second)
                        )
                    } else {
                        onTimeChange(
                            timeSelection,
                            Pair(0, 0)
                        )
                    }
                },
                buttonColor = Color(122, 178, 211)
            )
            if (timeSelection != 0 && selectedTime != null) {
                Text("    ${selectedTime!!.first}:${selectedTime!!.second}")
            }
            if (timeSelection != 0 && showTimePicker) {
                TimePickerDialog(
                    showDialog = showTimePicker,
                    onDismiss = {
                        showTimePicker = false
                    },
                    onTimeSelected = { time ->
                        selectedTime = time
                        showTimePicker = false
                    },
                    buttonColor = Color(122, 178, 211)
                )
            }
        }

//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        TransportationModeSelector(
//            selectedMode = selectedMode,
//            onModeSelected = { selectedMode = it })

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                onSearchClicked(
                    placeViewModel.getSelectedOriginAddress().ifEmpty { currentOrigin },
                    placeViewModel.getSelectedDestinationAddress().ifEmpty { currentDestination },
                    selectedMode,
                    timeSelection,
                    selectedTime ?: Pair(0, 0)
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color(122, 178, 211)),
            enabled = currentOrigin.isNotEmpty() && currentDestination.isNotEmpty()
        ) {
            Text("검색하기")
        }
    }
    if (showPlaceDialog) {
        PlaceSearchResultsDialog(
            results = placeSelectionText,
            onDismiss = { placeViewModel.closePlaceDialog() },
            onSelectPlace = { index ->
                placeViewModel.selectPlaceForRoute(index)
                placeViewModel.closePlaceDialog()
            }
        )
    }

}

@Composable
fun TimeSelectionButton(
    currentSelection: Int,
    onSelectionChange: (Int) -> Unit,
    buttonColor: Color
) {
    val options = listOf("시간 지정 없음", "출발시간 지정", "도착시간 지정")
    Button(
        onClick = { onSelectionChange((currentSelection + 1) % options.size) },
        colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
    ) {
        Text(text = options[currentSelection])
    }
}

//@Composable
//fun TransportationModeSelector(
//    selectedMode: String,
//    onModeSelected: (String) -> Unit
//) {
//    val modes = listOf("transit", "walking", "driving")
//    var expanded by remember { mutableStateOf(false) }
//
//    Column {
//        Button(
//            onClick = {
//                expanded = true
//            }
//        ) {
//            Text(text = selectedMode)
//        }
//        DropdownMenu(
//            expanded = expanded,
//            onDismissRequest = { expanded = false }
//        ) {
//            modes.forEach { mode ->
//                DropdownMenuItem(
//                    text = { Text(text = mode) },
//                    onClick = {
//                        onModeSelected(mode)
//                        expanded = false
//                    }
//                )
//            }
//        }
//    }
//}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onTimeSelected: (Pair<Int, Int>) -> Unit,
    buttonColor: Color
) {
    val koreaTimeZone = TimeZone.getTimeZone("Asia/Seoul")
    val currentTime = Calendar.getInstance(koreaTimeZone)

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    if (showDialog) {
        Column {
            TimePicker(
                state = timePickerState,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("취소")
                }

                Button(
                    onClick = {
                        onTimeSelected(
                            Pair(
                                timePickerState.hour,
                                timePickerState.minute
                            )
                        )
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = buttonColor)
                ) {
                    Text("확인")
                }
            }
        }
    }
}