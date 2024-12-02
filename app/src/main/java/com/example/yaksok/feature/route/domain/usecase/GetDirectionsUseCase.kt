package com.example.yaksok.feature.route.domain.usecase

import com.example.yaksok.feature.route.domain.repository.DirectionsRepository

class GetDirectionsUseCase
constructor(
    private val repository: DirectionsRepository
) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        mode: String
    ) =
        repository.getDirections(
            origin,
            destination,
            mode
        )
}