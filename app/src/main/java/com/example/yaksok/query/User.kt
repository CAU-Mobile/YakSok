package com.example.yaksok.query

import android.annotation.SuppressLint
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await

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

        suspend fun createUser(
            userId: String,
            name: String? = null,
            phoneNumber: String? = null
        ): Result<String> {
            val userCode = createCode()
            val newUser = User(
                userCode = userCode,
                name = name ?: "",
                phoneNumber = phoneNumber ?: ""
            )

            return try {
                FirebaseFirestore.getInstance().collection("users").document(userId)
                    .set(newUser)
                    .await() //이걸 적용해줘야 하는건가;;;;;

                // 성공적으로 저장된 경우
                Result.success("Create Success! User Code: $userCode")
            } catch (e: Exception) {
                // 실패한 경우
                Result.failure(Exception("Failed to create user: ${e.message}"))
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
    }
}