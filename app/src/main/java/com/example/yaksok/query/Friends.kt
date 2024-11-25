package com.example.yaksok.query

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

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
//        코루틴 적용 전
//        fun getFriends(
//            userId: String,
//            callBack: (Boolean, List<String>?, String?) -> Unit
//        ) {
//            friendsCollection.document(userId).get()
//                .addOnSuccessListener {
//                    val friendsList = it.toObject<Friends>()?.friendIds
//                    callBack(true, friendsList, "Get Success!")
//                }
//                .addOnFailureListener {
//                    callBack(false, null, it.toString())
//                }
//        }

        suspend fun getFriends(userId: String): Result<List<String>> = suspendCancellableCoroutine { continuation ->
            friendsCollection.document(userId).get()
                .addOnSuccessListener {
                    val friendsList = it.toObject<Friends>()?.friendIds ?: emptyList()
                    continuation.resume(Result.success(friendsList))
                }
                .addOnFailureListener {
                    continuation.resume(Result.failure(it))
                }
        }

        suspend fun getFriendslist(userId: String): List<String> {
            return suspendCancellableCoroutine { continuation ->
                friendsCollection.document(userId).get()
                    .addOnSuccessListener {
                        val friendsList = it.toObject<Friends>()?.friendIds ?: emptyList()
                        continuation.resume(friendsList)
                    }
                    .addOnFailureListener {
                        continuation.resumeWithException(it) // 실패 시 예외를 던짐
                    }
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

//        fun addFriend(
//            userId: String,
//            friendId: String,
//            callBack: (Boolean, String?) -> Unit
//        ) {
//            getFriends(userId) { s, r, e ->
//                if (s) {
//                    val friendIds = r?.toMutableList()
//                    friendIds?.add(friendId)
//
//                    friendsCollection.document(userId).update("friendIds", friendIds)
//                        .addOnCompleteListener {
//                            callBack(true, "Add Success!")
//                        }
//                        .addOnFailureListener {
//                            callBack(false, it.toString())
//                        }
//                } else {
//                    callBack(false, e)
//                }
//            }
//        }

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

        //사용하지 않을거같아 일단 주석처리 해두겠습니다
//        fun deleteFriend(
//            userId: String,
//            friendId: String,
//            callBack: (Boolean, String?) -> Unit
//        ) {
//            getFriends(userId) { s, r, e ->
//                if (s) {
//                    val friendIds = r?.toMutableList()
//                    if (friendIds?.remove(friendId) == true) {
//                        friendsCollection.document(userId).update("friendIds", friendIds)
//                            .addOnCompleteListener {
//                                callBack(true, "Delete Success!")
//                            }
//                            .addOnFailureListener {
//                                callBack(false, it.toString())
//                            }
//                    } else {
//                        callBack(false, "Cannot find friend ID!")
//                    }
//                } else {
//                    callBack(false, e)
//                }
//            }
//        }
    }
}