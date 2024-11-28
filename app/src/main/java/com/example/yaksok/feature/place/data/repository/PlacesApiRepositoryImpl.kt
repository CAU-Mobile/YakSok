package com.example.yaksok.feature.place.data.repository

import com.example.yaksok.feature.place.data.model.SearchPlacesRequest
import com.example.yaksok.feature.place.data.remote.PlacesApiService
import com.example.yaksok.feature.place.data.toEntity
import com.example.yaksok.feature.place.domain.model.PlaceListEntity
import com.example.yaksok.feature.place.domain.repository.PlacesApiRepository

class PlacesApiRepositoryImpl(
    private val placesApiService: PlacesApiService
) : PlacesApiRepository {

    override suspend fun searchPlacesList(
        query: String
    ): Result<PlaceListEntity> {
        return try {
            val request = SearchPlacesRequest(
                textQuery = query
            )
            val fieldMask =
                "places.name,places.id,places.displayName,places.types,places.formattedAddress,places.location,places.rating,places.googleMapsUri,places.websiteUri,places.currentOpeningHours"
            val response = placesApiService.requestSearchPlaces(
                API_KEY,
                fieldMask,
                request
            )
            Result.success(response.toEntity())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    companion object {
        private const val API_KEY = "API_KEY_REPLACE"
    }
}