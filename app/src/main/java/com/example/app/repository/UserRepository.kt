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

    fun addFavoriteByEmail(email: String, favorite: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        findUserByEmail(email) { user, errorMessage ->
            if (user != null) {
                // Добавляем новый элемент в список favorites
                val updatedFavorites = user.favorites.toMutableList()
                updatedFavorites.add(favorite)

                // Обновляем пользователя в базе данных
                db.collection("users").whereEqualTo("email", email).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null && task.result.documents.isNotEmpty()) {
                            val userDocument = task.result.documents[0]
                            userDocument.reference.update("favorites", updatedFavorites)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        onComplete(true, null) // Успех
                                    } else {
                                        onComplete(false, updateTask.exception?.message) // Ошибка при обновлении
                                    }
                                }
                        } else {
                            onComplete(false, "Ошибка при получении документа пользователя") // Ошибка получения документа
                        }
                    }
            } else {
                onComplete(false, errorMessage) // Ошибка поиска пользователя
            }
        }
    }

    fun getFavoritesByEmail(email: String, onComplete: (List<String>?, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        val user = documents[0].toObject(User::class.java)
                        onComplete(user?.favorites, null) // Возвращаем список favorites
                    } else {
                        onComplete(null, "Пользователь не найден") // Пользователь не найден
                    }
                } else {
                    onComplete(null, task.exception?.message) // Ошибка при выполнении запроса
                }
            }
    }

}