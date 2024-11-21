package com.example.yaksok.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.UsersQuery
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false) // 로그인 상태
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _loginError = MutableStateFlow<String?>(null) //로그인오류 상태관리
    val loginError: StateFlow<String?> = _loginError.asStateFlow()

    private var _userData = MutableStateFlow<FirebaseUser?>(null)
    @SuppressLint("RestrictedApi")
    var userData : StateFlow<FirebaseUser?> = _userData.asStateFlow()


    fun login(email: String, password: String) {//데베 쿼리에 전달
        viewModelScope.launch {
            AuthQuery.loginWithEmailAndPassword(email, password) { isSuccess, errorMessage ->
                _isLoggedIn.value = isSuccess
                _loginError.value = if (!isSuccess) errorMessage else null
                if(isSuccess){
                    fetchCurrentUser()
                }
            }
        }
    }
    private fun fetchCurrentUser() {
        val currentUser = AuthQuery.getCurrentUser()
        _userData.value = currentUser
    }

    fun checkLoginState() {
        val currentUser = AuthQuery.getCurrentUser()
        _isLoggedIn.value = currentUser != null
        _userData.value = currentUser
    }

    fun logout() { //아직은 쓸일없는듯
        AuthQuery.logout()
        _isLoggedIn.value = false
    }
}