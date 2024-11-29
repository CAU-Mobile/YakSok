package com.example.yaksok.feature.route.domain.usecase

import com.example.yaksok.feature.route.domain.repository.DirectionsRepository

class GetDirWithTmRpUseCase
constructor(
    private val repository: DirectionsRepository
) {
    suspend operator fun invoke(
        origin: String = "london bridge",
        destination: String = "granada",
        transitMode: String,
        transitRoutingPreference: String
    ) = repository.getDirectionsWithTmRp(
        origin,
        destination,
        transitMode,
        transitRoutingPreference
    )
}