package com.example.yaksok.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.AuthQuery
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    //아래 변수는 로그인 화면을 거칠 때에만 사용됩니다
    private val _isLoggedIn = MutableStateFlow<Boolean?>(null) // 로그인 상태
    val isLoggedIn: StateFlow<Boolean?> = _isLoggedIn

    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog


    private val _loginError = MutableStateFlow<String?>(null) //로그인오류 상태관리
    val loginError: StateFlow<String?> = _loginError


    private val _loginStatus = MutableStateFlow<String?>(null)
    val loginStatus: MutableStateFlow<String?> = _loginStatus

    private var _userData = MutableStateFlow<FirebaseUser?>(null)
    val userData: StateFlow<FirebaseUser?> get() = _userData.asStateFlow()

    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = AuthQuery.loginWithEmailAndPassword(email, password)

            if (result.isSuccess) {
                _isLoggedIn.value = true
                _showDialog.value = true
                fetchCurrentUser()
                _loginError.value = null
            } else {
                _showDialog.value = false
                _loginError.value = result.getOrElse { it.message }.toString()
            }
        }
    }

    fun changeDialogState() {
        _showDialog.value = false
    }

    private fun fetchCurrentUser() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = AuthQuery.getCurrentUser()
            if (_userData.value != currentUser) {
                _userData.value = currentUser
            }
        }
    }

//    fun clearLoginState() {
//        _isLoggedIn.value = false
//        _loginError.value = null.toString()
//    }

//    fun logout() { //아직은 쓸일없는듯
//        AuthQuery.logout()
//        _isLoggedIn.value = false
//    }
}