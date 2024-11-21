package com.example.yaksok.ui.friend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.Friends
import com.example.yaksok.query.FriendsQuery
import com.example.yaksok.query.FriendsQuery.Companion.getFriends
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddFriendViewModel : ViewModel() {

    private val _isAddSuccess = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isAddSuccess.asStateFlow()

    private val _isAddError = MutableStateFlow<String?>("아이디가 존재하지 않습니다.") //아이디가 존재하지 않거나 등
    val isAddError : StateFlow<String?> = _isAddError.asStateFlow()

    fun addFriend(friendId: String) {
        viewModelScope.launch() {
            _isAddSuccess.value = false
            _isAddError.value = null

            val currentUserId = AuthQuery.getCurrentUserId()
            if (currentUserId == null) {
                _isAddError.value = "User not logged in."
                return@launch
            }

            try {
                //친구목록 이미 있음 갖고와...
                val result = FriendsQuery.getFriends(currentUserId)
                val friendsList = result.getOrThrow()
                if (friendsList.isEmpty()) {
                    //친구목록이 없으면 Friends.createUser 해야함.
                    FriendsQuery.createUser(currentUserId) { success, message ->
                        if (success){
                            addFriendToUser(currentUserId, friendId)
                        } else {
                            _isAddError.value = message ?: "친구목록 생성 실패"
                        }
                    }
                } else {
                    //이미 있으면 추가 ㄱㄱ
                    addFriendToUser(currentUserId, friendId)
                }
            } catch (e: Exception) {
                _isAddError.value = e.message ?: "Unknown error"
            }
        }
    }

    // 친구 추가 메서드
    private fun addFriendToUser(userId: String, friendId: String) {
        FriendsQuery.addFriend(userId, friendId) { success, errorMessage ->
            _isAddSuccess.value = success
            _isAddError.value = if (success) null else errorMessage
        }
    }
}