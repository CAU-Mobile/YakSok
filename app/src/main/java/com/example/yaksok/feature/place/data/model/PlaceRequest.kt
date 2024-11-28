package com.example.yaksok.feature.place.data.model

data class SearchPlacesRequest(
    val textQuery: String,
    val languageCode: String = "ko",
    val locationBias: LocationBias = LocationBias(
        Rectangle(
            LatLng(37.2410, 131.8653),
            LatLng(33.1220, 126.2742)
        )
    ),
    val pageSize: Int = 5
)

data class LocationBias(
    val rectangle: Rectangle
)

data class Rectangle(
    val high: LatLng,
    val low: LatLng
)

data class LatLng(
    val latitude: Double,
    val longitude: Double
)