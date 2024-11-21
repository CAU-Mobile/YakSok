package com.example.yaksok.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.AppointmentQuery
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.FriendsQuery
import com.google.firebase.Timestamp
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class YaksokViewModel : ViewModel(){

    private val _isYaksokSuccess = MutableStateFlow(false)
    val isYaksokSuccess: StateFlow<Boolean> = _isYaksokSuccess.asStateFlow()

    private val _isYaksokError = MutableStateFlow<String?>("약속생성 실패.") //아이디가 존재하지 않거나 등
    val isYaksokError : StateFlow<String?> = _isYaksokError.asStateFlow()

    fun addYaksok(name: String,
                  geoPoint: GeoPoint,
                  time: Timestamp){
        viewModelScope.launch{
            AuthQuery.getCurrentUserId()?.let {
//                AppointmentQuery.createAppointment(name, GeoPoint, Timestamp, memberIds = )
//                { isSuccess, errorMessage ->
//                    _isYaksokSuccess.value = isSuccess
//                    _isYaksokError.value = if (!isSuccess) errorMessage else null
//                }
            }
        }
    }
}