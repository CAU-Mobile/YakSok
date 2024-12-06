package com.example.yaksok.feature.savePlace.model

data class SavedPlace(
    val userId: String = "",
    val id: String = "",
    val displayName: String = "",
    val formattedAddress: String = "",
    val placeLat: Double = 0.0,
    val placeLng: Double = 0.0,
    val googleMapsUri: String = "",
    val websiteUri: String? = null,
    val currentOpeningHours: List<String> = emptyList(),
    val createdAd: Long = System.currentTimeMillis()
)