package com.example.yaksok.query

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class Friends(
    val friendIds: List<String> = emptyList()
)

class FriendsQuery {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val friendsCollection = db.collection("friends")

        fun createUser(
            userId: String,
            friendIds: List<String> = emptyList<String>(),
            callBack: (Boolean, String?) -> Unit
        ) {
            friendsCollection.document(userId).set(Friends(friendIds))
                .addOnSuccessListener {
                    callBack(true, "Create Success!")
                }.addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun getFriends(
            userId: String,
            callBack: (Boolean, List<String>?, String?) -> Unit
        ) {
            friendsCollection.document(userId).get()
                .addOnSuccessListener {
                    val friendsList = it.toObject<Friends>()?.friendIds
                    callBack(true, friendsList, "Get Success!")
                }
                .addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun deleteUser(
            userId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            friendsCollection.document(userId).delete()
                .addOnSuccessListener {
                    callBack(true, "Delete Success!")
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun addFriend(userId: String, friendId: String, callBack: (Boolean, String?) -> Unit) {
            db.runTransaction { transaction ->
                val document = friendsCollection.document(userId)
                val friends = transaction.get(document).toObject<Friends>()?.friendIds?.toMutableList() ?: mutableListOf()
                if (!friends.contains(friendId)) {
                    friends.add(friendId)
                    transaction.update(document, "friendIds", friends)
                }
            }.addOnSuccessListener {
                callBack(true, "Add Success!")
            }.addOnFailureListener {
                callBack(false, it.message)
            }
        }

        fun getFriendNumberByName(friendName: String, callBack: (Boolean, String?) -> Unit){
            friendsCollection
                .whereEqualTo("name", friendName)
                .get()
                .addOnSuccessListener { result ->
                    if (!result.isEmpty) {
                        val friend = result.documents.firstOrNull()?.toObject<User>()
                        val phoneNumber = friend?.phoneNumber

                        if (phoneNumber != null) {
                            callBack(true, phoneNumber)
                        } else {
                            callBack(false, "Phone number not found")
                        }
                    } else {
                        callBack(false, "Friend not found")
                    }
                }
                .addOnFailureListener { exception ->
                    callBack(false, exception.message)
                }
        }
    }
}

class FriendsQueryCoroutine {
    companion object {
        suspend fun createUser(
            userId: String,
            friendIds: List<String> = emptyList()
        ): Unit {
            return suspendCoroutine { continuation ->
                FriendsQuery.createUser(
                    userId, friendIds
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun getFriends(
            userId: String
        ): List<String>? {
            return suspendCoroutine { continuation ->
                FriendsQuery.getFriends(
                    userId
                ) { isSuccess, friends, log ->
                    if (isSuccess) {
                        continuation.resume(friends)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun deleteUser(
            userId: String
        ): Unit {
            return suspendCoroutine { continuation ->
                FriendsQuery.deleteUser(
                    userId
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun addFriend(
            userId: String,
            friendId: String
        ): Unit {
            return suspendCoroutine { continuation ->
                FriendsQuery.addFriend(
                    userId, friendId
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun findFriendNumberByName(
            friendName: String,
        ):String {
            return suspendCoroutine { continuation ->
                FriendsQuery.getFriendNumberByName(
                    friendName
                ){
                    isSuccess, log->
                    if(isSuccess){
                        continuation.resume(String.toString())
                    } else{
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }
        
        //사용하지 않아 주석처리
        //suspend fun deleteFriend
    }
}