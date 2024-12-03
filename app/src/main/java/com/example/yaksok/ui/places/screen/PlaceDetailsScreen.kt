package com.example.yaksok.ui.places.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.yaksok.feature.savePlace.model.SavedPlace
import com.example.yaksok.ui.places.viewModel.PlacesViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

//@Composable
//fun PlaceDetailsScreen(viewModel: PlacesViewModel) {
//    val selectedPlace by viewModel.selectedPlace.collectAsState()
//    val uriHandler = LocalUriHandler.current
//
//    Column(
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        selectedPlace?.let { place ->
//            GoogleMap(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp),
//                cameraPositionState = rememberCameraPositionState {
//                    position = CameraPosition.fromLatLngZoom(
//                        LatLng(place.location.latitude, place.location.longitude),
//                        15f
//                    )
//                }) {
//                Marker(
//                    state = MarkerState(
//                        position = LatLng(place.location.latitude, place.location.longitude)
//                    ),
//                    title = place.displayName.text
//                )
//            }
//        }
//
//        selectedPlace?.let { place ->
//            Column(modifier = Modifier.padding(16.dp)) {
//                Text(place.displayName.text, style = MaterialTheme.typography.displaySmall)
//                Text("\n")
//                Text("주소: ${place.formattedAddress}\n")
//                if (place.rating != null) {
//                    Text("평점 : ${place.rating}\n")
//                }
//                ClickableText(
//                    text = buildAnnotatedString {
//                        append("구글 맵 : ")
//                        pushStringAnnotation("URL", place.googleMapsUri)
//                        withStyle(
//                            style = SpanStyle(
//                                color = Color.Blue,
//                                textDecoration = TextDecoration.Underline
//                            )
//                        ) {
//                            append(place.googleMapsUri)
//                        }
//                    },
//                    onClick = {
//                        place.googleMapsUri.let { url ->
//                            uriHandler.openUri(url)
//                        }
//                    }
//                )
//                place.websiteUri?.let { websiteUri ->
//                    ClickableText(
//                        text = buildAnnotatedString {
//                            append("웹사이트: ")
//                            pushStringAnnotation("URL", websiteUri)
//                            withStyle(
//                                style = SpanStyle(
//                                    color = Color.Blue,
//                                    textDecoration = TextDecoration.Underline
//                                )
//                            ) {
//                                append(websiteUri)
//                            }
//                        },
//                        onClick = {
//                            uriHandler.openUri(websiteUri)
//                        }
//                    )
//                }
//                if (!place.currentOpeningHours?.weekdayDescriptions.isNullOrEmpty()) {
//                    Text("\n영업시간")
//                    place.currentOpeningHours?.weekdayDescriptions?.forEach {
//                        Text(it)
//                    }
//                }
//            }
//        } ?: Text("선택된 장소가 없습니다.")
//    }
//}

@Composable
fun PlaceDetailsScreen(
    place: SavedPlace
) {
//    val selectedPlace by viewModel.selectedPlace.collectAsState()
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        place.let { place ->
            GoogleMap(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(
                        LatLng(place.placeLat, place.placeLng),
                        15f
                    )
                }) {
                Marker(
                    state = MarkerState(
                        position = LatLng(place.placeLat, place.placeLng)
                    ),
                    title = place.displayName
                )
            }
        }

        place.let { place ->
            Column(modifier = Modifier.padding(16.dp)) {
                Text(place.displayName, style = MaterialTheme.typography.displaySmall)
                Text("\n")
                Text("주소: ${place.formattedAddress}\n")
//                if (place.rating != null) {
//                    Text("평점 : ${place.rating}\n")
//                }
                ClickableText(
                    text = buildAnnotatedString {
                        append("구글 맵 : ")
                        pushStringAnnotation("URL", place.googleMapsUri)
                        withStyle(
                            style = SpanStyle(
                                color = Color.Blue,
                                textDecoration = TextDecoration.Underline
                            )
                        ) {
                            append(place.googleMapsUri)
                        }
                    },
                    onClick = {
                        place.googleMapsUri.let { url ->
                            uriHandler.openUri(url)
                        }
                    }
                )
                place.websiteUri?.let { websiteUri ->
                    ClickableText(
                        text = buildAnnotatedString {
                            append("웹사이트: ")
                            pushStringAnnotation("URL", websiteUri)
                            withStyle(
                                style = SpanStyle(
                                    color = Color.Blue,
                                    textDecoration = TextDecoration.Underline
                                )
                            ) {
                                append(websiteUri)
                            }
                        },
                        onClick = {
                            uriHandler.openUri(websiteUri)
                        }
                    )
                }
                if (place.currentOpeningHours.isNotEmpty()) {
                    Text("\n영업시간")
                    place.currentOpeningHours.forEach {
                        Text(it)
                    }
                }
            }
        } ?: Text("선택된 장소가 없습니다.")
    }
}