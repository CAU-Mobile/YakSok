package com.example.yaksok.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.Friends
import com.example.yaksok.query.FriendsQuery
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddFriendViewModel : ViewModel() {

    private val _isAddSuccess = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isAddSuccess.asStateFlow()

    private val _isAddError = MutableStateFlow<String?>("아이디가 존재하지 않습니다.") //아이디가 존재하지 않거나 등
    val isAddError : StateFlow<String?> = _isAddError.asStateFlow()

    fun addFriend(friendId: String){
         viewModelScope.launch{
            AuthQuery.getCurrentUserId()?.let {
                FriendsQuery.addFriend(userId = it, friendId) { isSuccess, errorMessage ->
                    _isAddSuccess.value = isSuccess
                    _isAddError.value = if (!isSuccess) errorMessage else null
                }
            }
        }
    }
}