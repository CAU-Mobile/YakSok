package com.example.yaksok.query

import com.example.yaksok.query.UsersQuery.Companion.createUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthQuery {
    companion object {
        private val auth = FirebaseAuth.getInstance()

//        fun loginWithEmailAndPassword(
//            email: String,
//            password: String,
//            callBack: (Boolean, String?) -> Unit
//        ) {
//            auth.signInWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        callBack(true, "login Success!")
//                    } else {
//                        val errorMessage = when (task.exception) {
//                            is FirebaseAuthInvalidUserException -> "등록되지 않은 이메일입니다."
//                            is FirebaseAuthInvalidCredentialsException -> "비밀번호가 틀렸습니다."
//                            else -> "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
//                        }
//                        callBack(false, errorMessage)
//                    }
//                }
//        }

        fun logout() {
            auth.signOut()
        }
        // 로그인 함수 - 코루틴 활용
        suspend fun loginWithEmailAndPassword(
            email: String,
            password: String
        ): Result<String> {
            return try {
                auth.signInWithEmailAndPassword(email, password).await()
                Result.success("Login Success!")
            } catch (e: FirebaseAuthInvalidUserException) {
                Result.failure(Exception("등록되지 않은 이메일입니다."))
            } catch (e: FirebaseAuthInvalidCredentialsException) {
                Result.failure(Exception("비밀번호가 틀렸습니다."))
            } catch (e: Exception) {
                Result.failure(Exception("알 수 없는 오류가 발생했습니다. 다시 시도해주세요."))
            }
        }

        // 회원가입 함수 - 코루틴 활용
        suspend fun registerWithEmailAndPassword(
            email: String,
            password: String,
            name: String,
            phoneNumber: String
        ): Result<String> {
            return try {
                val authResult = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = auth.currentUser?.uid ?: return Result.failure(Exception("Failed to get User ID"))
                UsersQueryCoroutine.createUser(userId, name, phoneNumber)
                Result.success("회원가입 성공!")
            } catch (e: FirebaseAuthUserCollisionException) {
                Result.failure(Exception("이미 가입된 이메일입니다."))
            } catch (e: Exception) {
                Result.failure(Exception("회원가입에 실패했습니다."))
            }
        }

//        fun registerWithEmailAndPassword( //회원가입에서 CreateUser 를 같이 처리하려고 합니다.
//            email: String,
//            password: String,
//            name: String,
//            phoneNumber: String,
//            callBack: (Boolean, String?) -> Unit
//        ) {
//            auth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener { task ->
//                    if (task.isSuccessful) { //유저아이디는 식별자로만 쓰여 일단 Firebase uid를 활용.
//                        val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
//                        createUser(userId, name, phoneNumber) { isSuccess, _, errorMessage ->
//                            if (isSuccess) {
//                                callBack(true, "회원가입 성공!")
//                            } else {
//                                callBack(false, errorMessage)
//                            }
//                        }
//                    } else {
//                        val errorMessage = if (task.exception is FirebaseAuthUserCollisionException) {
//                            "이미 가입된 이메일입니다."
//                        } else {
//                            task.exception?.localizedMessage ?: "회원가입에 실패했습니다."
//                        }
//                        callBack(false, errorMessage)
//                    }
//                }
//        }

        fun getCurrentUser(): FirebaseUser? {
            return auth.currentUser
        }

        fun getCurrentUserId(): String? {
            return getCurrentUser()?.uid
        }

        }
    }