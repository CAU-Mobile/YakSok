package com.example.yaksok.feature.place.presentation.model

data class PlaceListModel(
    val places: List<PlaceModel>?
)

data class PlaceModel(
    val id: String,
    val name: String,
    val displayName: LocalizedTextModel,
    val formattedAddress: String,
    val location: LatLngModel,
    val types: List<String>,
    val rating: Float?,
    val googleMapsUri: String,
    val websiteUri: String?,
    val currentOpeningHours: OpeningHoursModel?
)

data class LocalizedTextModel(
    val text: String,
    val languageCode: String
)

data class LatLngModel(
    val latitude: Double,
    val longitude: Double
)

data class OpeningHoursModel(
    val openNow: Boolean,
    val weekdayDescriptions: List<String>
)