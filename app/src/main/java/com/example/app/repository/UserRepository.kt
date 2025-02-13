package com.example.app.repository

import com.example.app.entity.Phone
import com.example.app.entity.User
import com.google.firebase.firestore.FirebaseFirestore

class UserRepository {
    fun updateUserData(userId: String, user: User, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        db.collection("users").document(userId).set(user)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    onComplete(true, null)
                } else {
                    onComplete(false, task.exception?.message)
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

    fun getUserByEmail(email: String, onSuccess: (User?) -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { documents ->
                if (documents.isEmpty) {
                    onSuccess(null)
                } else {
                    for (document in documents) {
                        val user = document.toObject(User::class.java)
                        onSuccess(user)
                        return@addOnSuccessListener
                    }
                }
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    fun updateUser(userId: String, updatedUser: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").document(userId)
            .set(updatedUser)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

    /*fun addUser(newUser: User, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users").add(newUser)
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { exception ->
                onFailure(exception)
            }
    }

     */

    fun findUserByEmail(email: String, onComplete: (User?, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        val user = documents[0].toObject(User::class.java)
                        onComplete(user, null)
                    } else {
                        onComplete(null, "Пользователь не найден")
                    }
                } else {
                    onComplete(null, task.exception?.message)
                }
            }
    }

    fun getUserByEmail(email: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        onComplete(true, null)
                    } else {
                        onComplete(false, null)
                    }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }
    }

    fun addFavoriteByEmail(email: String, favorite: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        findUserByEmail(email) { user, errorMessage ->
            if (user != null) {
                val updatedFavorites = user.favorites.toMutableList()
                updatedFavorites.add(favorite)

                db.collection("users").whereEqualTo("email", email).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null && task.result.documents.isNotEmpty()) {
                            val userDocument = task.result.documents[0]
                            userDocument.reference.update("favorites", updatedFavorites)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        onComplete(true, null)
                                    } else {
                                        onComplete(false, updateTask.exception?.message)
                                    }
                                }
                        } else {
                            onComplete(false, "Ошибка при получении документа пользователя")
                        }
                    }
            } else {
                onComplete(false, errorMessage)
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
                        onComplete(user?.favorites, null)
                    } else {
                        onComplete(null, "Пользователь не найден")
                    }
                } else {
                    onComplete(null, task.exception?.message)
                }
            }
    }

    fun deleteModelFromFavoritesByEmail(favorite: String, email: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        val userDocument = documents[0]
                        val user = userDocument.toObject(User::class.java)

                        if (user?.favorites?.contains(favorite) == true) {
                            user.favorites.remove(favorite)

                            userDocument.reference.update("favorites", user.favorites)
                                .addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        onComplete(true, null)
                                    } else {
                                        onComplete(
                                            false,
                                            updateTask.exception?.message
                                        )
                                    }
                                }
                        } else {
                            onComplete(false, "Элемент не найден в избранном")
                        }
                    } else {
                        onComplete(false, "Пользователь не найден")
                    }
                } else {
                    onComplete(false, task.exception?.message)
                }
            }

    }

    fun getDocumentIdByEmail(email: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("users")
            .whereEqualTo("email", email)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    val document = querySnapshot.documents.first()
                    onSuccess(document.id)
                } else {
                    onFailure(Exception("Document not found"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }
}



