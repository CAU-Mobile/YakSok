package com.example.yaksok.feature.savePlace.model

import com.example.yaksok.feature.place.presentation.model.OpeningHoursModel
import java.util.UUID

data class SavedPlace(
    val userId: String = "",
    val id: String = "",
    val displayName: String = "",
    val formattedAddress: String = "",
    val placeLat: Double = 0.0,
    val placeLng: Double = 0.0,
//    val types: List<String>,
//    val rating: Float?,
    val googleMapsUri: String = "",
    val websiteUri: String? = null,
    val currentOpeningHours: List<String> = emptyList(),
    val createdAd: Long = System.currentTimeMillis()
)