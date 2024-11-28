package com.example.yaksok.feature.place.data.remote

import com.example.yaksok.feature.place.data.model.PlacesListResponse
import com.example.yaksok.feature.place.data.model.SearchPlacesRequest
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface PlacesApiService {
    @POST("/v1/places:searchText")
    suspend fun requestSearchPlaces(
        @Header("X-Goog-Api-Key") apiKey: String,
        @Header("X-Goog-FieldMask") fieldMask: String,
        @Body request: SearchPlacesRequest
    ): PlacesListResponse
}