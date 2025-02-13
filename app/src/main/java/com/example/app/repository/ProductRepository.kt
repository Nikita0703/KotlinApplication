package com.example.app.repository

import com.example.app.entity.Phone
import com.example.app.entity.User
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class ProductRepository {
    private val db: FirebaseFirestore = Firebase.firestore

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
                        val user = documents[0].toObject(Phone::class.java)
                        onComplete(user, null)
                    } else {
                        onComplete(null, "Пользователь не найден")
                    }
                } else {
                    onComplete(null, task.exception?.message)
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
            querySnapshot.documents.first().toObject(Phone::class.java)
        }
    }

    */

    fun getDocumentIdByModel(model: String, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("phones")
            .whereEqualTo("model", model)
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

    fun addCommentByModel(model: String, favorite: String, onComplete: (Boolean, String?) -> Unit) {
        val db = FirebaseFirestore.getInstance()

        findPhoneByModelForComment(model) { phone, errorMessage ->
            if (phone != null) {
                val updatedFavorites = phone.comments.toMutableList()
                updatedFavorites.add(favorite)

                db.collection("phones").whereEqualTo("model", model).get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful && task.result != null && task.result.documents.isNotEmpty()) {
                            val userDocument = task.result.documents[0]
                            userDocument.reference.update("comments", updatedFavorites)
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
                        onComplete(user?.comments, null)
                    } else {
                        onComplete(null, "Пользователь не найден")
                    }
                } else {
                    onComplete(null, task.exception?.message)
                }
            }
    }



}