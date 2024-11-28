package com.example.yaksok.ui.friend

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.yaksok.query.AppointmentQuery
import com.example.yaksok.query.AuthQuery
import com.example.yaksok.query.FriendsQuery
import com.example.yaksok.query.FriendsQueryCoroutine
import com.example.yaksok.query.User
import com.example.yaksok.query.UsersQuery
import com.example.yaksok.query.UsersQuery.Companion.getUserIdWithCode
import com.example.yaksok.query.UsersQueryCoroutine
import com.google.firebase.firestore.GeoPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddFriendViewModel : ViewModel() {

    private val _isAddSuccess = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isAddSuccess

    private val _isAddError = MutableStateFlow<String?>("아이디가 존재하지 않습니다.") //아이디가 존재하지 않거나 등
    val isAddError : StateFlow<String?> = _isAddError


    //친구 약속에 추가 항목
//    private val _friendList = MutableLiveData<List<User>>()
//    val friendList: LiveData<List<User>> get() = _friendList

    private val _friendList = MutableStateFlow<List<User>>(emptyList())
    val friendList: StateFlow<List<User>> = _friendList.asStateFlow()

    private val _loading = MutableLiveData<Boolean>() //만들어놓고 제대로 쓰지도 않았네
    val loading: LiveData<Boolean> get() = _loading

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> get() = _error

    //각 친구 버튼,,,
    private val _isFriendAdded = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val isFriendAdded: StateFlow<Map<String, Boolean>> = _isFriendAdded.asStateFlow()

    private val _selectedFriends = MutableStateFlow<List<User>>(emptyList())
    val selectedFriends: StateFlow<List<User>> = _selectedFriends.asStateFlow()

    private val _myUserCode = MutableStateFlow("")
    val myUserCode : StateFlow<String> = _myUserCode.asStateFlow()

    fun getMyUserCode() {
        viewModelScope.launch {
            val currentUserId = AuthQuery.getCurrentUserId()
            Log.d("ViewModel", "currentUserId: $currentUserId")
            _myUserCode.value = currentUserId?.let {
                UsersQueryCoroutine.getUserCodeWithId(it)
            } ?: "Null Exception"
        }
    }

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
                val friendsList = FriendsQueryCoroutine.getFriends(currentUserId)
                if (friendsList.isNullOrEmpty()) {
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

    //친구 약속에 추가 항목. 약속에서 친구추가 -> 친구로드
    //지금 생각해보니 괜히 이 함수 써서 친구목록 로딩 늘린거같음 걍 위에서 FriendList 에 추가할걸
    fun loadFriends(userId: String) {
        _loading.value = true
        _error.value = null

        viewModelScope.launch {
            try {
                val friendIds = FriendsQueryCoroutine.getFriends(userId)//친구 유저코드임
                val friendsInfo = mutableListOf<User>()//어쨌든 여기 더해줘야함.

                Log.d("AddFriendViewModel", "FriendIds: $friendIds")
                friendIds?.let {
                    for (friendId in friendIds) { //친구아이디 목록만큼
                        getUserIdWithCode(friendId) { success, userId, message ->
                            if (success && userId != null) {
                                UsersQuery.getUser(userId) { userSuccess, user, userMessage ->
                                    if (userSuccess && user != null) {
                                        Log.d("AddFriendViewModel", "User fetched: ${user.name}")
                                        friendsInfo.add(user) //FriendsInfo 리스트에 추가.
                                        _friendList.value = friendsInfo
                                    } else {
                                        _error.value = userMessage ?: "Failed to get user."
                                    }
                                }
                            } else {
                                _error.value = message
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _loading.value = false
            }
        }
    }

    fun addFriendToYaksok(friend: User) { //selectedFriend 리스트에 친구를 추가함.
        _selectedFriends.value += friend
        _isFriendAdded.value = _isFriendAdded.value.toMutableMap().apply {
            this[friend.userCode] = true
        }
    }
    fun removeFriendFromYaksok(friend: User) {
        _selectedFriends.value -= friend
        _isFriendAdded.value = _isFriendAdded.value.toMutableMap().apply {
            this[friend.userCode] = false
        }
    }

}