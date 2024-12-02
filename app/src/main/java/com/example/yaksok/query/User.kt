package com.example.yaksok.query

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

data class User(
    val userCode: String = "",
    val name: String = "",
    val phoneNumber: String = ""
)

class UsersQuery {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private val db = FirebaseFirestore.getInstance()
        private val usersCollection = db.collection("users")

        fun createUser(
            userId: String,
            name: String? = null,
            phoneNumber: String? = null,
            callBack: (Boolean, String?, String?) -> Unit
        ) {
            val userCode = createCode()
            val newUser = User(
                userCode = userCode,
                name = name ?: "",
                phoneNumber = phoneNumber ?: ""
            )
            usersCollection.document(userId).set(newUser)
                .addOnSuccessListener {
                    callBack(true, userCode, "Create Success!")
                }.addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getUser(
            userId: String,
            callBack: (Boolean, User?, String?) -> Unit
        ) {
            usersCollection.document(userId).get()
                .addOnSuccessListener {
                    callBack(true, it.toObject<User>(), "Get Success!")
                }.addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getUserCodeWithId(
            userId: String,
            callBack: (Boolean, String?, String?) -> Unit
        ) {
            usersCollection.document(userId).get()
                .addOnSuccessListener { documents ->
                    if (documents.exists()) {
                        val userCode = documents.getString("userCode")
                        Log.d("UsersQuery", "Found userCode: $userCode")
                        callBack(true, userCode, "Get Success!")
                    } else {
                        callBack(false, null, "No User Found!")
                    }
                }
                .addOnFailureListener { exception ->
                    callBack(false, null, exception.toString())
                }
        }

        fun getUserIdWithCode(
            userCode: String,
            callBack: (Boolean, String?, String?) -> Unit
        ) {
            usersCollection.whereEqualTo("userCode", userCode.lowercase()).get()
                .addOnSuccessListener {
                    if (!it.isEmpty) {
                        callBack(true, it.documents[0].id, "Get Success!")
                    } else {
                        callBack(false, null, "No User!")
                    }
                }.addOnFailureListener {
                    callBack(false, null, it.toString())
                }
        }

        fun getUserWithCode(
            userCode: String,
            callBack: (Boolean, User?, String?) -> Unit
        ) {
            getUserIdWithCode(userCode) { isSuccess1, userId, log ->
                if (isSuccess1) {
                    userId?.let {
                        getUser(userId) { isSuccess2, user, log ->
                            if (isSuccess2) {
                                callBack(true, user, log)
                            } else {
                                callBack(false, null, log)
                            }
                        }
                    }
                } else {
                    callBack(false, null, log)
                }
            }
        }

        fun getUserNameWithCode(
            userCode: String,
            callBack: (Boolean, String?, String?) -> Unit
        ) {
            getUserWithCode(userCode) { isSuccess, user, log ->
                if (isSuccess) {
                    user?.let {
                        callBack(true, user.name, log)
                    }
                } else {
                    callBack(false, null, log)
                }
            }
        }

        fun checkUserExists(
            userId: String,
            callBack: (Boolean) -> Unit
        ) {
            usersCollection.document(userId).get()
                .addOnSuccessListener { document ->
                    callBack(document.exists())//존재시 true 반환
                }.addOnFailureListener {
                    callBack(false)
                }
        }

        fun updateUser(
            userId: String,
            name: String? = null,
            phoneNumber: String? = null,
            callBack: (Boolean, String?) -> Unit
        ) {
            usersCollection.document(userId).get()
                .addOnSuccessListener {
                    val originUser = it.toObject<User>()
                    usersCollection.document(userId).update(
                        "name", name ?: originUser?.name,
                        "phoneNumber", phoneNumber ?: originUser?.phoneNumber
                    )
                        .addOnSuccessListener {
                            callBack(true, "Update Success!")
                        }
                        .addOnFailureListener { e ->
                            callBack(false, e.toString())
                        }
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        fun deleteUser(
            userId: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            usersCollection.document(userId).delete()
                .addOnSuccessListener {
                    callBack(true, "Delete Success!")
                }
                .addOnFailureListener {
                    callBack(false, it.toString())
                }
        }

        private fun createCode(): String {
            val timestamp = System.currentTimeMillis()
                .toString(36)
                .padStart(8, '0')

            val timePart1 = timestamp.slice(0 until 4)
            val timePart2 = timestamp.slice(4 until 8)

            val randomPart = (1..4)
                .map { (('a'..'z') + ('0'..'9')).random() }
                .joinToString("")

            return "$timePart1-$timePart2-$randomPart"
        }

        fun getUserNumberByCode(friendCode: String, callBack: (Boolean, String?) -> Unit){
            usersCollection
                .whereEqualTo("userCode", friendCode)
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

class UsersQueryCoroutine {
    companion object {
        suspend fun createUser(
            userId: String,
            name: String? = null,
            phoneNumber: String? = null
        ): String? {
            return suspendCoroutine { continuation ->
                UsersQuery.createUser(
                    userId, name, phoneNumber
                ) { isSuccess, userCode, log ->
                    if (isSuccess) {
                        continuation.resume(userCode)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun getUser(
            userId: String
        ): User? {
            return suspendCoroutine { continuation ->
                UsersQuery.getUser(
                    userId
                ) { isSuccess, user, log ->
                    if (isSuccess) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun getUserCodeWithId(
            userId: String
        ): String? {
            return suspendCoroutine { continuation ->
                UsersQuery.getUserCodeWithId(
                    userId
                ) { isSuccess, userCode, log ->
                    if (isSuccess) {
                        continuation.resume(userCode)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun getUserIdWithCode(
            userCode: String
        ): String? {
            return suspendCoroutine { continuation ->
                UsersQuery.getUserIdWithCode(
                    userCode
                ) { isSuccess, userId, log ->
                    if (isSuccess) {
                        continuation.resume(userId)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun getUserWithCode(
            userCode: String
        ): User? {
            return suspendCoroutine { continuation ->
                UsersQuery.getUserWithCode(
                    userCode
                ) { isSuccess, user, log ->
                    if (isSuccess) {
                        continuation.resume(user)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun getUserNameWithCode(
            userCode: String
        ): String? {
            return suspendCoroutine { continuation ->
                UsersQuery.getUserNameWithCode(
                    userCode
                ) { isSuccess, userName, log ->
                    if (isSuccess) {
                        continuation.resume(userName)
                    } else {
                        continuation.resumeWithException(Exception(log))
                    }
                }
            }
        }

        suspend fun checkUserExists(
            userId: String
        ): Boolean? {
            return suspendCoroutine { continuation ->
                UsersQuery.checkUserExists(
                    userId
                ) { isExists ->
                    continuation.resume(isExists)
                }
            }
        }

        suspend fun updateUser(
            userId: String,
            name: String? = null,
            phoneNumber: String? = null
        ): Unit {
            return suspendCoroutine { continuation ->
                UsersQuery.updateUser(
                    userId, name, phoneNumber
                ) { isSuccess, log ->
                    if (isSuccess) {
                        continuation.resume(Unit)
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
                UsersQuery.deleteUser(
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
        suspend fun findUserNumberByCode(
            friendCode: String,
        ):String {
            return suspendCoroutine { continuation ->
                UsersQuery.getUserNumberByCode(friendCode){ isSuccess, result->
                    if (isSuccess) {
                        if (result != null) {
                            continuation.resume(result) // 사용자 번호 반환
                        } else {
                            continuation.resumeWithException(Exception("Phone number is null"))
                        }
                    } else{
                        continuation.resumeWithException(Exception("Null"))
                    }
                }
            }
        }
    }
}