package com.example.yaksok.ui.login

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.UsersQuery
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.Dispatchers
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

    private val _loginStatus = MutableLiveData<String>()
    val loginStatus: LiveData<String> = _loginStatus

    private var _userData = MutableStateFlow<FirebaseUser?>(null)
    @SuppressLint("RestrictedApi")
    var userData : StateFlow<FirebaseUser?> = _userData.asStateFlow()


    fun login(email: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = AuthQuery.loginWithEmailAndPassword(email, password)
            val isSuccess = result.isSuccess
            _isLoggedIn.value = isSuccess
            _loginError.value = if (!isSuccess) result.getOrElse { it.message } else null
            if (isSuccess) {
                fetchCurrentUser()
            }
        }
    }
//    fun login(email: String, password: String) {//데베 쿼리에 전달
//        viewModelScope.launch {
//            AuthQuery.loginWithEmailAndPassword(email, password) { isSuccess, errorMessage ->
//                _isLoggedIn.value = isSuccess
//                _loginError.value = if (!isSuccess) errorMessage else null
//                if(isSuccess){
//                    fetchCurrentUser()
//                }
//            }
//        }
//    }

    private fun fetchCurrentUser(){
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = AuthQuery.getCurrentUser()
            if (_userData.value != currentUser) {
                _userData.value = currentUser
            }
        }
    }

    fun checkLoginState(){
        viewModelScope.launch {
            val currentUser = AuthQuery.getCurrentUser()
            _isLoggedIn.value = currentUser != null
            _userData.value = currentUser
        }
    }

    fun logout() { //아직은 쓸일없는듯
        AuthQuery.logout()
        _isLoggedIn.value = false
    }
}