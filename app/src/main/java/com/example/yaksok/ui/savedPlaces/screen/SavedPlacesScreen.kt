package com.example.yaksok.ui.savedPlaces.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.yaksok.feature.savePlace.model.SavedPlace
import com.example.yaksok.feature.savePlace.SavePlaceViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun SavedPlacesScreen(
//    viewModel: SavePlaceViewModel,
    onPlaceClick: (SavedPlace) -> Unit
) {
    val savePlaceViewModel: SavePlaceViewModel = hiltViewModel()
    val userId = Firebase.auth.currentUser?.uid ?: ""
    LaunchedEffect(key1 = true) {
        savePlaceViewModel.listenForPlaces(userId = userId)
    }
    val savedPlaces by savePlaceViewModel.savedPlaces.collectAsState()


    LazyColumn() {
        if (savedPlaces.isEmpty()) {
            item{
                Text(text = "아직 저장된 장소가 없습니다.",
                    fontSize = 20.sp
                )
            }


        } else {
            items(savedPlaces) { place ->
                SavedPlaceItem(
                    place = place,
                    onClick = { onPlaceClick(place) }
                )
            }
        }
    }
}


@Composable
fun SavedPlaceItem(
    place: SavedPlace,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(122, 178, 211)
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = place.displayName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = place.formattedAddress,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}