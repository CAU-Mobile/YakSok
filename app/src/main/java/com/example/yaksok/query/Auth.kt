package com.example.yaksok.query

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser

class AuthQuery {
    companion object {
        private val auth = FirebaseAuth.getInstance()

        fun loginWithEmailAndPassword(
            email: String,
            password: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callBack(true, "login Success!")
                    } else {
                        val errorMessage = when (task.exception) {
                            is FirebaseAuthInvalidUserException -> "등록되지 않은 이메일입니다."
                            is FirebaseAuthInvalidCredentialsException -> "비밀번호가 틀렸습니다."
                            else -> "알 수 없는 오류가 발생했습니다. 다시 시도해주세요."
                        }
                        callBack(false, errorMessage)
                    }
                }
        }

        fun logout() {
            auth.signOut()
        }

        fun registerWithEmailAndPassword(
            email: String,
            password: String,
            callBack: (Boolean, String?) -> Unit
        ) {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        callBack(true, "register Success!")
                    } else {
                        callBack(false, task.exception?.localizedMessage)
                    }
                }
        }

        fun getCurrentUser(): FirebaseUser? {
            return auth.currentUser
        }

        fun getCurrentUserId(): String? {
            return getCurrentUser()?.uid
        }

        }
    }