package com.example.yaksok.feature.savePlace

import androidx.lifecycle.ViewModel
import com.example.yaksok.feature.place.presentation.model.PlaceModel
import com.example.yaksok.feature.savePlace.model.SavedPlace
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SavePlaceViewModel : ViewModel() {
    private val _savedPlaces = MutableStateFlow<List<SavedPlace>>(emptyList())
    val savedPlaces = _savedPlaces.asStateFlow()

    fun savePlace(
        displayName: String,
        formattedAddress: String,
        placeLat: Double,
        placeLng: Double,
        googleMapUri: String,
        webSiteUri: String?,
        currentOpeningHours: List<String>
    ) {
        val savedPlace = SavedPlace(
            displayName = displayName,
            formattedAddress = formattedAddress,
            placeLat = placeLat,
            placeLng = placeLng,
            googleMapsUri = googleMapUri,
            websiteUri = webSiteUri,
            currentOpeningHours = currentOpeningHours
        )
        _savedPlaces.value += savedPlace
    }


}