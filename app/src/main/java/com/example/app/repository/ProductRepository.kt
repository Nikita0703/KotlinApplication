package com.example.app.repository

import com.example.app.entity.Phone
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

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
}