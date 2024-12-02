package com.example.yaksok.feature.place.domain.repository

import com.example.yaksok.feature.place.domain.model.PlaceListEntity

interface PlacesApiRepository {
    suspend fun searchPlacesList(
        query: String
    ): Result<PlaceListEntity>
}