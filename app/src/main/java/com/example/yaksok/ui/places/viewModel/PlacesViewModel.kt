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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun searchPlaces(
        query: String
    ) {
        viewModelScope.launch {
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
            "${place.displayName.text}\n${place.formattedAddress}"
        } ?: emptyList()
    }

    fun selectPlace(
        index: Int
    ) {
        searchResults.value?.places?.getOrNull(index)?.let {
            _selectedPlace.value = it
        }
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