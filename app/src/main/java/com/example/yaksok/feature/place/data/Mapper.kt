package com.example.yaksok.feature.place.data

import com.example.yaksok.feature.place.data.model.LatLng
import com.example.yaksok.feature.place.data.model.LocalizedText
import com.example.yaksok.feature.place.data.model.OpeningHours
import com.example.yaksok.feature.place.data.model.PlaceResponse
import com.example.yaksok.feature.place.data.model.PlacesListResponse
import com.example.yaksok.feature.place.domain.model.LatLngEntity
import com.example.yaksok.feature.place.domain.model.LocalizedTextEntity
import com.example.yaksok.feature.place.domain.model.OpeningHoursEntity
import com.example.yaksok.feature.place.domain.model.PlaceEntity
import com.example.yaksok.feature.place.domain.model.PlaceListEntity

fun PlacesListResponse.toEntity(): PlaceListEntity {
    return PlaceListEntity(
        places = this.places.map { it.toEntity() }
    )
}

fun PlaceResponse.toEntity(): PlaceEntity {
    return PlaceEntity(
        id = this.id,
        name = this.name,
        displayName = this.displayName.toEntity(),
        formattedAddress = this.formattedAddress,
        location = this.location.toEntity(),
        types = this.types,
        rating = this.rating,
        googleMapsUri = this.googleMapsUri,
        websiteUri = this.websiteUri,
        currentOpeningHours = this.currentOpeningHours?.toEntity()
    )
}

fun LocalizedText.toEntity(): LocalizedTextEntity {
    return LocalizedTextEntity(
        text = this.text,
        languageCode = this.languageCode
    )
}

fun LatLng.toEntity(): LatLngEntity {
    return LatLngEntity(
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun OpeningHours.toEntity(): OpeningHoursEntity {
    return OpeningHoursEntity(
        openNow = this.openNow,
        weekdayDescriptions = this.weekdayDescriptions

    )
}