package com.example.yaksok

import com.example.yaksok.feature.place.data.remote.PlaceNetworkClient
import com.example.yaksok.feature.place.data.repository.PlacesApiRepositoryImpl
import com.example.yaksok.feature.place.domain.repository.PlacesApiRepository
import com.example.yaksok.feature.place.domain.usecase.GetSearchListUseCase
import com.example.yaksok.feature.route.data.remote.RouteNetworkClient
import com.example.yaksok.feature.route.data.repository.DirectionsRepositoryImpl
import com.example.yaksok.feature.route.domain.repository.DirectionsRepository
import com.example.yaksok.feature.route.domain.usecase.GetDirWithArrTmRpUseCase
import com.example.yaksok.feature.route.domain.usecase.GetDirWithDepTmRpUseCase
import com.example.yaksok.feature.route.domain.usecase.GetDirWithTmRpUseCase
import com.example.yaksok.feature.route.domain.usecase.GetDirectionsUseCase
import com.example.yaksok.ui.places.viewModel.PlacesViewModelFactory
import com.example.yaksok.ui.routes.viewModel.DirectionsViewModelFactory

class ServiceLocator {
    private val placesApiService = PlaceNetworkClient.placesApiService

    val placesApiRepository: PlacesApiRepository by lazy {
        PlacesApiRepositoryImpl(placesApiService)
    }

    val getSearchListUseCase: GetSearchListUseCase by lazy {
        GetSearchListUseCase(placesApiRepository)
    }

    val placesContainer: PlacesContainer by lazy {
        PlacesContainer(
            getSearchListUseCase
        )
    }

    private val directionsApiService = RouteNetworkClient.directionsApiService

    val directionsRepository: DirectionsRepository by lazy {
        DirectionsRepositoryImpl(directionsApiService)
    }

    val getDirectionsUseCase: GetDirectionsUseCase by lazy {
        GetDirectionsUseCase(directionsRepository)
    }
    val getDirWithTmRpUseCase: GetDirWithTmRpUseCase by lazy {
        GetDirWithTmRpUseCase(directionsRepository)
    }
    val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase by lazy {
        GetDirWithDepTmRpUseCase(directionsRepository)
    }
    val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase by lazy {
        GetDirWithArrTmRpUseCase(directionsRepository)
    }

    val directionsContainer: DirectionsContainer by lazy {
        DirectionsContainer(
            getDirectionsUseCase,
            getDirWithDepTmRpUseCase,
            getDirWithTmRpUseCase,
            getDirWithArrTmRpUseCase
        )
    }
}

class PlacesContainer(
    private val getSearchListUseCase: GetSearchListUseCase
) {
    val placesViewModelFactory = PlacesViewModelFactory(
        getSearchListUseCase
    )
}

class DirectionsContainer(
    private val getDirectionsUseCase: GetDirectionsUseCase,
    private val getDirWithDepTmRpUseCase: GetDirWithDepTmRpUseCase,
    private val getDirWithTmRpUseCase: GetDirWithTmRpUseCase,
    private val getDirWithArrTmRpUseCase: GetDirWithArrTmRpUseCase
) {
    val directionsViewModelFactory = DirectionsViewModelFactory(
        getDirectionsUseCase,
        getDirWithDepTmRpUseCase,
        getDirWithTmRpUseCase,
        getDirWithArrTmRpUseCase
    )
}