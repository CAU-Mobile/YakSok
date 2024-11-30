package com.example.yaksok.ui.places.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.yaksok.feature.place.domain.usecase.GetSearchListUseCase
import com.example.yaksok.feature.place.presentation.model.PlaceListModel
import com.example.yaksok.feature.place.presentation.model.PlaceModel
import com.example.yaksok.feature.place.presentation.toModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PlacesViewModel(
    private val getSearchListUseCase: GetSearchListUseCase
) : ViewModel() {
    private val _searchResults = MutableStateFlow<PlaceListModel?>(null)
    val searchResults: StateFlow<PlaceListModel?> = _searchResults

    private val _placeSelectionText = MutableStateFlow<List<String>>(emptyList())
    val placeSelectionText: StateFlow<List<String>> = _placeSelectionText

    private var _selectedPlace = MutableStateFlow<PlaceModel?>(null)
    val selectedPlace: StateFlow<PlaceModel?> get() = _selectedPlace

    private var _selectedPlaceForRoute: PlaceModel? = null

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _showPlaceDialog = MutableStateFlow(false)
    val showPlaceDialog: StateFlow<Boolean> get() = _showPlaceDialog

    private var _selectedOriginAddress: String = ""
    private var _selectedDestinationAddress: String = ""

    var isSelectingOrigin: Boolean = true
        private set

    fun searchPlaces(
        query: String
    ) {
        viewModelScope.launch {
            resetState()
            val result = getSearchListUseCase(
                query
            )
            result.onSuccess { placeListEntity ->
                _searchResults.value = placeListEntity.toModel()
                makePlaceSelectionText()
            }.onFailure { error ->
                _error.value = error.message
            }
        }
    }

    fun searchPlacesForRoute(
        query: String,
        isOrigin: Boolean
    ) {
        viewModelScope.launch {
            resetState()
            isSelectingOrigin = isOrigin
            val result = getSearchListUseCase(
                query
            )
            result.onSuccess { placeListEntity ->
                _searchResults.value = placeListEntity.toModel()
                makePlaceSelectionText()
            }.onFailure { error ->
                _error.value = error.message
            }
        }
    }

    fun makePlaceSelectionText() {
        _placeSelectionText.value = _searchResults.value?.places?.map { place ->
            "🔷${place.displayName.text}\n${place.formattedAddress}"
        } ?: emptyList()
        if (placeSelectionText.value.isNotEmpty()) {
            _showPlaceDialog.value = true
        } else {
            _showPlaceDialog.value = false
        }
    }

    fun closePlaceDialog() {
        _showPlaceDialog.value = false
    }

    fun selectPlace(
        index: Int
    ) {
        searchResults.value?.places?.getOrNull(index)?.let {
            _selectedPlace.value = it
        }
    }

    fun selectPlaceForRoute(
        index: Int
    ) {
        _selectedPlaceForRoute = _searchResults.value?.places?.getOrNull(index)
        _selectedPlaceForRoute?.let {
            if (isSelectingOrigin) {
                _selectedOriginAddress = it.formattedAddress
            } else {
                _selectedDestinationAddress = it.formattedAddress
            }
        }
    }

    fun getSelectedOriginAddress(): String = _selectedOriginAddress
    fun getSelectedDestinationAddress(): String = _selectedDestinationAddress

    fun resetState() {
        _showPlaceDialog.value = false
        _placeSelectionText.value = emptyList()
        _selectedPlace.value = null
        _searchResults.value = null
    }
}

class PlacesViewModelFactory(
    private val getSearchListUseCase: GetSearchListUseCase
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlacesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlacesViewModel(getSearchListUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}