package com.example.yaksok.feature.place.domain.usecase

import com.example.yaksok.feature.place.domain.model.PlaceListEntity
import com.example.yaksok.feature.place.domain.repository.PlacesApiRepository

class GetSearchListUseCase(
    private val placesApiRepository: PlacesApiRepository
) {
    suspend operator fun invoke(
        query: String
    ): Result<PlaceListEntity> {
        return placesApiRepository.searchPlacesList(
            query
        )
    }
}