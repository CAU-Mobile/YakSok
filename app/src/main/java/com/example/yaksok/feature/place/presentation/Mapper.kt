package com.example.yaksok.feature.place.presentation

import com.example.yaksok.feature.place.domain.model.LatLngEntity
import com.example.yaksok.feature.place.domain.model.LocalizedTextEntity
import com.example.yaksok.feature.place.domain.model.OpeningHoursEntity
import com.example.yaksok.feature.place.domain.model.PlaceEntity
import com.example.yaksok.feature.place.domain.model.PlaceListEntity
import com.example.yaksok.feature.place.presentation.model.LatLngModel
import com.example.yaksok.feature.place.presentation.model.LocalizedTextModel
import com.example.yaksok.feature.place.presentation.model.OpeningHoursModel
import com.example.yaksok.feature.place.presentation.model.PlaceListModel
import com.example.yaksok.feature.place.presentation.model.PlaceModel

fun PlaceListEntity.toModel(): PlaceListModel {
    return PlaceListModel(
        places = this.places?.map { it.toModel() }
    )
}

fun PlaceEntity.toModel(): PlaceModel {
    return PlaceModel(
        id = this.id,
        name = this.name,
        displayName = this.displayName.toModel(),
        formattedAddress = this.formattedAddress,
        location = this.location.toModel(),
        types = this.types,
        rating = this.rating,
        googleMapsUri = this.googleMapsUri,
        websiteUri = this.websiteUri,
        currentOpeningHours = this.currentOpeningHours?.toModel()
    )
}

fun LocalizedTextEntity.toModel(): LocalizedTextModel {
    return LocalizedTextModel(
        text = this.text,
        languageCode = this.languageCode
    )
}

fun LatLngEntity.toModel(): LatLngModel {
    return LatLngModel(
        latitude = this.latitude,
        longitude = this.longitude
    )
}

fun OpeningHoursEntity.toModel(): OpeningHoursModel {
    return OpeningHoursModel(
        openNow = this.openNow,
        weekdayDescriptions = this.weekdayDescriptions
    )
}