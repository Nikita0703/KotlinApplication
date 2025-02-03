package com.example.app.repository

import com.example.app.entity.Phone
import com.example.app.entity.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    fun updateUserData(userId: String, user: User, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        // Сохраняем данные пользователя в коллекции "users"
        db.collection("users").document(userId).set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null) // Успех
                } else {
                    onComplete(false, task.exception?.message) // Ошибка
                }
            }
    }

    fun addUser(user: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
            db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Метод для поиска пользователя по email
    fun findUserByEmail(email: String, onComplete: (User?, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        // Предполагаем, что email уникален и берем первый документ
                        val user = documents[0].toObject(User::class.java)
                        onComplete(user, null)
                    } else {
                        onComplete(null, "Пользователь не найден")
                    }
                } else {
                    onComplete(null, task.exception?.message) // Ошибка
                }
            }
    }

    // Метод для проверки существования пользователя по email
    fun isExistByEmail(email: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        onComplete(true, null) // Пользователь существует
                    } else {
                        onComplete(false, null) // Пользователь не найден
                    }
                } else {
                    onComplete(false, task.exception?.message) // Ошибка
                }
            }
    }
}