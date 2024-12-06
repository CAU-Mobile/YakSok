package com.example.yaksok.ui.distance.viewModel

import android.location.Location
import androidx.lifecycle.ViewModel
import com.example.yaksok.query.Utilities
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DistanceViewModel : ViewModel() {
     var appointmentLocation: Location? = null

    private var _distanceMap = MutableStateFlow(HashMap<String, Double>(HashMap()))
    val distanceMap: StateFlow<Map<String, Double>> = _distanceMap

    private fun addDistance(userId: String, distance: Double) {
        val updatedMap = HashMap(_distanceMap.value)
        updatedMap[userId] = distance
        _distanceMap.value = updatedMap
    }

    fun updateDistance(
        userId: String,
        location: Location
    ) {
        appointmentLocation?.let {
            val result = Utilities.getDistance(
                appointmentLocation!!.latitude,
                appointmentLocation!!.longitude,
                location.latitude,
                location.longitude)
            addDistance(userId, result)
        }
    }
}
