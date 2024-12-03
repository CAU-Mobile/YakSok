package com.example.yaksok.feature.savePlace

import androidx.lifecycle.ViewModel
import com.example.yaksok.feature.place.presentation.model.PlaceModel
import com.example.yaksok.feature.savePlace.model.SavedPlace
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.UUID
import javax.inject.Inject

class SavePlaceViewModel @Inject constructor() : ViewModel() {
    private val _savedPlaces = MutableStateFlow<List<SavedPlace>>(emptyList())
    val savedPlaces = _savedPlaces.asStateFlow()

    private val firebaseDatabase = Firebase.database

    fun savePlace(
        displayName: String,
        formattedAddress: String,
        placeLat: Double,
        placeLng: Double,
        googleMapUri: String,
        webSiteUri: String?,
        currentOpeningHours: List<String>
    ) {
        val userId = Firebase.auth.currentUser?.uid ?: ""
        val savedPlace = SavedPlace(
            userId = userId,
            id = firebaseDatabase.reference.child("savedPlaces").push().key ?: UUID.randomUUID()
                .toString(),
            displayName = displayName,
            formattedAddress = formattedAddress,
            placeLat = placeLat,
            placeLng = placeLng,
            googleMapsUri = googleMapUri,
            websiteUri = webSiteUri,
            currentOpeningHours = currentOpeningHours
        )
        _savedPlaces.value += savedPlace
        firebaseDatabase.reference.child("savedPlaces").child(userId).push().setValue(savedPlace)
    }

    fun listenForPlaces(userId: String){
        firebaseDatabase.reference.child("savedPlaces").child(userId).orderByChild("createdAt")
            .addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    val list = mutableListOf<SavedPlace>()
                    snapshot.children.forEach { eachSavedPlace ->
                        val savedPlace = eachSavedPlace.getValue(SavedPlace::class.java)
                        savedPlace?.let {
                            list.add(it)
                        }
                    }
                    _savedPlaces.value = list
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
    }

}