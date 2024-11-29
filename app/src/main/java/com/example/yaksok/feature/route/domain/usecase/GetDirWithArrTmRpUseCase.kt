package com.example.yaksok.feature.route.domain.usecase

import com.example.yaksok.feature.route.domain.repository.DirectionsRepository

class GetDirWithArrTmRpUseCase
constructor(
    private val repository: DirectionsRepository
) {
    suspend operator fun invoke(
        origin: String,
        destination: String,
        arrivalTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ) = repository.getDirectionsWithArrivalTmRp(
        origin,
        destination,
        arrivalTime,
        transitMode,
        transitRoutingPreference
    )
}