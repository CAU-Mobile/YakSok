package com.example.yaksok.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.AuthQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false) // 로그인 상태
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null) //로그인오류 상태관리
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    fun login(email: String, password: String) {//데베 쿼리에 전달
        viewModelScope.launch {
            AuthQuery.loginWithEmailAndPassword(email, password) { isSuccess, errorMessage ->
                _isLoggedIn.value = isSuccess
                _loginError.value = if (!isSuccess) errorMessage else null
            }
        }
    }

    fun clearLoginState() {
        _isLoggedIn.value = false
        _loginError.value = null
    }

    fun logout() { //아직은 쓸일없는듯
        AuthQuery.logout()
        _isLoggedIn.value = false
    }
}