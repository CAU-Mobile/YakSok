package com.example.yaksok.ui.distance.viewModel

import android.location.Location
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DistanceViewModel : ViewModel() {
    private val _currentDistance = MutableStateFlow<Double?>(null)
    val currentDistance: StateFlow<Double?> = _currentDistance

    private var _othersDistance = 0.0
    val othersDistance: Double get() = _othersDistance

    fun calculateDistance(
        currentLocation: Location,
        destinationLat: Double,
        destinationLng: Double
    ) {
        val results = FloatArray(1)
        Location.distanceBetween(
            currentLocation.latitude,
            currentLocation.longitude,
            destinationLat,
            destinationLng,
            results
        )
        _currentDistance.value = results[0].toDouble()
    }

    fun calculateOthersDistance(
        currentLocation: Location,
        destinationLat: Double,
        destinationLng: Double
    ) {
        val results = FloatArray(1)
        Location.distanceBetween(
            currentLocation.latitude,
            currentLocation.longitude,
            destinationLat,
            destinationLng,
            results
        )
        _othersDistance = results[0].toDouble()
    }
}
