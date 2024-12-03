package com.example.yaksok.feature.savePlace.model

import com.example.yaksok.feature.place.presentation.model.OpeningHoursModel
import java.util.UUID

data class SavedPlace(
    val id: String = UUID.randomUUID().toString(),
    val displayName: String,
    val formattedAddress: String,
    val placeLat: Double,
    val placeLng: Double,
//    val types: List<String>,
//    val rating: Float?,
    val googleMapsUri: String,
    val websiteUri: String?,
    val currentOpeningHours: List<String>
)