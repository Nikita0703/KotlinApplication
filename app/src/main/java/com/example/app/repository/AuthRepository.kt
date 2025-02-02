package com.example.app.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class AuthRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    fun registerUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Успех
                } else {
                    onComplete(false, task.exception?.message) // Ошибка
                }
            }
    }

    fun loginUser(email: String, password: String, onComplete: (Boolean, String?) -> Unit) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Успех
                } else {
                    onComplete(false, task.exception?.message) // Ошибка
                }
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }
}
