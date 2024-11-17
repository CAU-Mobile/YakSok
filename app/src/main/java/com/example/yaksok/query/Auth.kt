package com.example.yaksok.query

import com.google.firebase.auth.FirebaseAuth
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
                        callBack(false, task.exception?.localizedMessage)
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