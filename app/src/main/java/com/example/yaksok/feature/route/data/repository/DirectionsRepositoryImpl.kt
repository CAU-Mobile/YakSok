package com.example.yaksok.feature.route.data.repository

import com.example.yaksok.feature.route.data.remote.DirectionsApiService
import com.example.yaksok.feature.route.domain.model.DirectionsEntity
import com.example.yaksok.feature.route.domain.repository.DirectionsRepository

class DirectionsRepositoryImpl(
    private val apiService: DirectionsApiService
) : DirectionsRepository {

    override suspend fun getDirections(
        origin: String,
        destination: String,
        mode: String
    ): DirectionsEntity {
        val result = apiService.getDirections(origin, destination, mode).toEntity()
        return result
    }

    override suspend fun getDirectionsWithDepartureTmRp(
        origin: String,
        destination: String,
        departureTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ): DirectionsEntity {
        val result = apiService.getDirectionsDep(
            origin,
            destination,
            departureTime,
            transitMode,
            transitRoutingPreference
        ).toEntity()
        return result
    }

    override suspend fun getDirectionsWithTmRp(
        origin: String,
        destination: String,
        transitMode: String,
        transitRoutingPreference: String
    ): DirectionsEntity {
        val result =
            apiService.getDirectionsTmRp(origin, destination, transitMode, transitRoutingPreference)
                .toEntity()
        return result
    }

    override suspend fun getDirectionsWithArrivalTmRp(
        origin: String,
        destination: String,
        arrivalTime: Int,
        transitMode: String,
        transitRoutingPreference: String
    ): DirectionsEntity {
        val result = apiService.getDirectionsArr(
            origin,
            destination,
            arrivalTime,
            transitMode,
            transitRoutingPreference
        ).toEntity()
        return result
    }

}