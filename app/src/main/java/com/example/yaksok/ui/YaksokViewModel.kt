package com.example.yaksok.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.AppointmentQuery
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.User
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


    //얘는 날짜 선택 오류땜에 빡쳐서 옮겨둠
    private val _showDateTimePicker = MutableStateFlow(false)
    val showDateTimePicker: StateFlow<Boolean> = _showDateTimePicker

    // DateTimePickerDialog를 표시하는 메서드
    fun showDateTimePickerDialog() {
        _showDateTimePicker.value = true
    }

    // DateTimePickerDialog를 숨기는 메서드
    fun hideDateTimePickerDialog() {
        _showDateTimePicker.value = false
    }

    fun addYaksok(
        name: String,
        details: String,
        geoPoint: GeoPoint,
        time: Timestamp,
        friendList: StateFlow<List<User>>
    ){
        viewModelScope.launch{
            AuthQuery.getCurrentUserId()?.let {
                val members = friendList.value.map{it.userCode}
                AppointmentQuery.createAppointment(name, details, geoPoint, time, memberIds=members)
                { isSuccess, appointmentId, errorMessage ->
                    _isYaksokSuccess.value = isSuccess
                    _isYaksokError.value = if (!isSuccess) errorMessage else null
                }
            }
        }
    }
}