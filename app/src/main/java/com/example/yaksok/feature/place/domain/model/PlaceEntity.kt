package com.example.yaksok.feature.place.domain.model

data class PlaceListEntity(
    val places: List<PlaceEntity>?
)

data class PlaceEntity(
    val id: String,
    val name: String,
    val displayName: LocalizedTextEntity,
    val formattedAddress: String,
    val location: LatLngEntity,
    val types: List<String>,
    val rating: Float?,
    val googleMapsUri: String,
    val websiteUri: String?,
    val currentOpeningHours: OpeningHoursEntity?
)

data class LocalizedTextEntity(
    val text: String,
    val languageCode: String
)

data class LatLngEntity(
    val latitude: Double,
    val longitude: Double
)

data class OpeningHoursEntity(
    val openNow: Boolean,
    val weekdayDescriptions: List<String>
)