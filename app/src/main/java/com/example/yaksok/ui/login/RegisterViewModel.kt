package com.example.yaksok.ui.login;

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.UsersQuery
import com.example.yaksok.query.AuthQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    private val _registerStatus = MutableStateFlow<Boolean?>(null) // 등록 상태
    val registerStatus: StateFlow<Boolean?> = _registerStatus.asStateFlow()

    private val _registerError = MutableStateFlow<String?>(null) // 오류 상태
    val registerError: StateFlow<String?> = _registerError.asStateFlow()

    //register 쿼리로 연결, 관리.
    fun register(email: String, password: String, name: String, phoneNumber: String) {
        viewModelScope.launch {
            AuthQuery.registerWithEmailAndPassword(email, password, name, phoneNumber) { isSuccess, errorMessage ->
                _registerStatus.value = isSuccess
                _registerError.value = if (!isSuccess) errorMessage else null
            }
        }
    }

    fun clearRegisterState() { //상태초기화: 회원가입창에서 가입이 끝나고, 또다시 가입 페이지로 들어와야할때 대비
        _registerStatus.value = null
        _registerError.value = null
    }
}
