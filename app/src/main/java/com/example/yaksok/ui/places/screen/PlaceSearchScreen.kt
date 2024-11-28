package com.example.yaksok.ui.places.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.yaksok.ui.places.viewModel.PlacesViewModel

@Composable
fun PlaceSearchScreen(
    viewModel: PlacesViewModel,
    onNavigateToDetails: () -> Unit
) {
    var searchQuery by remember {
        mutableStateOf("")
    }
    var showDialog by remember { mutableStateOf(false) }
    val placeSelectionText by viewModel.placeSelectionText.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            label = { Text("장소 검색") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                viewModel.searchPlaces(searchQuery)
                showDialog = true
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("검색")
        }
    }

    LaunchedEffect(placeSelectionText) {
        if (placeSelectionText.isNotEmpty()) {
            showDialog = true
        }
    }

    if (showDialog) {
        PlaceSearchResultsDialog(
            results = placeSelectionText,
            onDismiss = { showDialog = false },
            onSelectPlace = { index ->
                viewModel.selectPlace(index)
                showDialog = false
                onNavigateToDetails()
            }
        )
    }
}