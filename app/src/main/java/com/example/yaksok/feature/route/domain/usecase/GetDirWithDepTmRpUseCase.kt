package com.example.yaksok.feature.route.domain.usecase

import com.example.yaksok.feature.route.domain.repository.DirectionsRepository

class GetDirWithDepTmRpUseCase
constructor(
    private val repository: DirectionsRepository
) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        departureTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ) = repository.getDirectionsWithDepartureTmRp(
        origin,
        destination,
        departureTime,
        transitMode,
        transitRoutingPreference
    )
}