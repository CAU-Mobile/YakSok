package com.example.yaksok.feature.place.data.model

data class PlacesListResponse(
    val places: List<PlaceResponse>
)

data class PlaceResponse(
    val name: String,
    val id: String,
    val displayName: LocalizedText,
    val types: List<String>,
    val formattedAddress: String,
    val location: LatLng,
    val rating: Float?,
    val googleMapsUri: String,
    val websiteUri: String?,
    val currentOpeningHours: OpeningHours?
)

data class LocalizedText(
    val text: String,
    val languageCode: String
)

data class OpeningHours(
    val openNow: Boolean,
    val weekdayDescriptions: List<String>
)