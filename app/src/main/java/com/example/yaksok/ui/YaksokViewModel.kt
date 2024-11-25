package com.example.yaksok.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.Appointment
import com.example.yaksok.query.AppointmentQuery
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.FriendsQuery
import com.example.yaksok.query.User
import com.example.yaksok.query.UsersQuery
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

    private val _YaksokList = MutableStateFlow<List<Appointment>>(emptyList())
    val YaksokList: StateFlow<List<Appointment>> = _YaksokList.asStateFlow()

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

    //현재 유저의 유저코드까지 약속 데이터에 저장하는 코드
    //하...진짜 코드 드릅다 뇌빼고 때려넣음
    fun addYaksok(
        name: String,
        details: String,
        geoPoint: GeoPoint,
        time: Timestamp,
        friendList: StateFlow<List<User>>
    ) {
        viewModelScope.launch {
            AuthQuery.getCurrentUserId()?.let { currentUserId ->
                UsersQuery.getUserCodeWithId(currentUserId) { success, currentUserCode, errorMessage ->
                    if (success && currentUserCode != null) {
                        Log.d("CreateYaksok", "CurrentUserCode: $currentUserCode")
                        val members = mutableListOf(currentUserCode)
                        members.addAll(friendList.value.map { it.userCode })

                        AppointmentQuery.createAppointment(
                            name = name,
                            details = details,
                            geoPoint = geoPoint,
                            time = time,
                            memberIds = members
                        ) { isSuccess, appointmentId, errorMessage ->
                            _isYaksokSuccess.value = isSuccess
                            _isYaksokError.value = if (!isSuccess) errorMessage else null
                        }
                    }
                    else {
                        // 현재 유저의 userCode를 가져오지 못한 경우 처리
                        _isYaksokError.value = errorMessage ?: "Failed to retrieve current user code"
                    }
                }
            }
        }
    }

    fun loadYaksokList() {
        viewModelScope.launch {
            AuthQuery.getCurrentUserId()?.let { currentUserId ->
                UsersQuery.getUserCodeWithId(currentUserId) { success, currentUserCode, errorMessage ->
                    if (currentUserCode != null) {
                        AppointmentQuery.getAppointmentsWithUserId(currentUserCode) { success, appointments, errorMessage ->
                            if (success && appointments != null) {
                                val appointmentList = appointments.values.toList()
                                _YaksokList.value = appointmentList
                                Log.d("loadYaksok", "AppointmentId:${appointments.size}")
                            } else {
                                _YaksokList.value = emptyList()
                                _isYaksokError.value = errorMessage ?: "Failed to load appointments"
                            }
                        }
                    }
                }
            }
        }
    }

//    fun addYaksok(
//        name: String,
//        details: String,
//        geoPoint: GeoPoint,
//        time: Timestamp,
//        friendList: StateFlow<List<User>>
//    ){
//        viewModelScope.launch{
//            AuthQuery.getCurrentUserId()?.let {
//                val members = friendList.value.map{it.userCode}
//                AppointmentQuery.createAppointment(name, details, geoPoint, time, memberIds=members)
//                { isSuccess, appointmentId, errorMessage ->
//                    _isYaksokSuccess.value = isSuccess
//                    _isYaksokError.value = if (!isSuccess) errorMessage else null
//                }
//            }
//        }
//    }
}