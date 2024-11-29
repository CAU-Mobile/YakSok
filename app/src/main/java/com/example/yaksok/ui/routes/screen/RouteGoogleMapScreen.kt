package com.example.yaksok.ui.routes.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.yaksok.ui.routes.viewModel.DirectionsViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.rememberMarkerState

//TODO googleMapScreen 앞에 Route 붙이기
@Composable
fun GoogleMapScreen(
    viewModel: DirectionsViewModel,
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier
) {
    viewModel.afterSelecting()

    val polylines by viewModel.polylines.collectAsState()
    val mapBounds by viewModel.mapBounds.collectAsState()
    val directionExplanations by viewModel.directionExplanations.observeAsState("")
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    val startLocation by viewModel.startLocation.observeAsState()
    val destLocation by viewModel.destLocationLatLng.observeAsState()

    val startAdd by viewModel.origin.observeAsState()
    val endAdd by viewModel.destination.observeAsState()

    val cameraPositionState = rememberCameraPositionState()

    LaunchedEffect(mapBounds) {
        mapBounds?.let { bounds ->
            cameraPositionState.move(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    BackHandler(onBack = onBackPressed)

    //여기부터 추가
    Box(modifier = modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            polylines.forEach { polylineOptions ->
                Polyline(
                    points = polylineOptions.points,
                    color = Color(polylineOptions.color),
                    width = 10f
                )
            }

            //출발지 마커
            startLocation?.let {
                Marker(
                    state = rememberMarkerState(position = it),
                    title = "출발지",
                    snippet = startAdd,
                    icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)
                )
            }

            //도착지 마커
            destLocation?.let {
                Marker(
                    state = rememberMarkerState(position = it),
                    title = "목적지",
                    snippet = endAdd
                )
            }
        }

        Button(
            onClick = { isBottomSheetVisible = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("자세히")
        }

        if (isBottomSheetVisible) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f))
                    .clickable { isBottomSheetVisible = false }
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(16.dp)
                    .clickable { }
            ) {
                Text(text = directionExplanations)
            }
        }
    }
}