package com.example.app.repository

import com.example.app.entity.Phone
import com.example.app.entity.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val db: FirebaseFirestore = Firebase.firestore

    // Метод для добавления телефона в Firestore
    fun addPhone(phone: Phone, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("phones")
            .add(phone)
            .addOnSuccessListener { documentReference ->
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    // Метод для удаления телефона из Firestore по ID документа
    fun deletePhone(documentId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("phones").document(documentId)
            .delete()
            .addOnSuccessListener {
                onSuccess()
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun readAll(onSuccess: (List<Phone>) -> Unit, onFailure: (Any?) -> Unit) {
        db.collection("phones")
            .get()
            .addOnSuccessListener { querySnapshot ->
                val phones = mutableListOf<Phone>()
                for (document in querySnapshot.documents) {
                    val phone = document.toObject(Phone::class.java)
                    phone?.let {
                        phones.add(it)
                    }
                }
                onSuccess(phones)
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun findPhoneByModel(model: String, onSuccess: (Phone) -> Unit, onFailure: (Any?) -> Unit) {
        db.collection("phones")
            .whereEqualTo("model", model)
            .get()
            .addOnSuccessListener {querySnapshot ->
                val phone = querySnapshot.documents.first().toObject(Phone::class.java)
                if (phone != null) {
                    onSuccess(phone)
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun findPhoneByModelForComment(model: String, onComplete: (Phone?, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("phones")
            .whereEqualTo("model", model)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        // Предполагаем, что email уникален и берем первый документ
                        val user = documents[0].toObject(Phone::class.java)
                        onComplete(user, null)
                    } else {
                        onComplete(null, "Пользователь не найден")
                    }
                } else {
                    onComplete(null, task.exception?.message) // Ошибка
                }
            }
    }


   /* suspend fun findPhoneByModel(model: String): Phone? {
        val querySnapshot = db.collection("phones")
            .whereEqualTo("model", model)
            .get()
            .await()

        return if (querySnapshot.isEmpty) {
            null
        } else {
            // Предполагаем, что модель уникальна, и возвращаем первый найденный телефон
            querySnapshot.documents.first().toObject(Phone::class.java)
        }
    }

    */

    // Метод для получения documentId по модели телефона
    fun getDocumentIdByModel(model: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("phones")
            .whereEqualTo("model", model)
            .get()
            .addOnSuccessListener { querySnapshot ->
                if (!querySnapshot.isEmpty) {
                    // Получаем первый документ из результатов
                    val document = querySnapshot.documents.first()
                    // Возвращаем его ID
                    onSuccess(document.id)
                } else {
                    // Если документы не найдены
                    onFailure(Exception("Document not found"))
                }
            }
            .addOnFailureListener { e ->
                onFailure(e)
            }
    }

    fun addCommentByModel(model: String, favorite: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        findPhoneByModelForComment(model) { phone, errorMessage ->
            if (phone != null) {
                // Добавляем новый элемент в список favorites
                val updatedFavorites = phone.comments.toMutableList()
                updatedFavorites.add(favorite)

                // Обновляем пользователя в базе данных
                db.collection("phones").whereEqualTo("model", model).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null && task.result.documents.isNotEmpty()) {
                            val userDocument = task.result.documents[0]
                            userDocument.reference.update("comments", updatedFavorites)
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

    fun getCommentsByModel(model: String, onComplete: (List<String>?, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        db.collection("phones")
            .whereEqualTo("model", model)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documents = task.result?.documents
                    if (documents != null && documents.isNotEmpty()) {
                        val user = documents[0].toObject(Phone::class.java)
                        onComplete(user?.comments, null) // Возвращаем список favorites
                    } else {
                        onComplete(null, "Пользователь не найден") // Пользователь не найден
                    }
                } else {
                    onComplete(null, task.exception?.message) // Ошибка при выполнении запроса
                }
            }
    }



}