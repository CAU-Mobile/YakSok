package com.example.yaksok.query

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

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

        fun addFriend(
            userId: String,
            friendId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            getFriends(userId) { s, r, e ->
                if (s) {
                    val friendIds = r?.toMutableList()
                    friendIds?.add(friendId)

                    friendsCollection.document(userId).update("friendIds", friendIds)
                        .addOnCompleteListener {
                            callBack(true, "Add Success!")
                        }
                        .addOnFailureListener {
                            callBack(false, it.toString())
                        }
                } else {
                    callBack(false, e)
                }
            }
        }

        fun deleteFriend(
            userId: String,
            friendId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            getFriends(userId) { s, r, e ->
                if (s) {
                    val friendIds = r?.toMutableList()
                    if (friendIds?.remove(friendId) == true) {
                        friendsCollection.document(userId).update("friendIds", friendIds)
                            .addOnCompleteListener {
                                callBack(true, "Delete Success!")
                            }
                            .addOnFailureListener {
                                callBack(false, it.toString())
                            }
                    } else {
                        callBack(false, "Cannot find friend ID!")
                    }
                } else {
                    callBack(false, e)
                }
            }
        }
    }
}