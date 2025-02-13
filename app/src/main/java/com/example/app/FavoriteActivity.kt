package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.example.app.entity.Phone
import com.example.app.repository.AuthRepository
import com.example.app.repository.ProductRepository
import com.example.app.repository.UserRepository
import com.example.app.service.PhoneAdapter
import com.example.app.service.PhoneFavoriteAdapter
import com.google.firebase.database.FirebaseDatabase

class FavoriteActivity  : AppCompatActivity(){
    private lateinit var listViewNames: ListView
    private lateinit var backButton: Button
    private val authRepository = AuthRepository()
    private val userRepository =  UserRepository()
    private val productRepository =  ProductRepository()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)
        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener{back()}
        listViewNames = findViewById(R.id.listViewNames)

        readNames()
    }
    
    fun readNames() {
        val user = authRepository.getCurrentUser()
        val email: String = user?.email ?: "default@example.com"
        userRepository.getFavoritesByEmail(email) { favorites, errorMessage ->
            if (favorites != null) {
                findPhonesByModels(favorites) { foundPhones ->
                        val adapter = PhoneFavoriteAdapter(this, foundPhones)
                        val listView = findViewById<ListView>(R.id.listViewNames)
                        listView.adapter = adapter
                }
            } else {
                println("Ошибка: $errorMessage")
            }
        }
    }

    fun findPhonesByModels(favorites: List<String>, onComplete: (List<Phone>) -> Unit) {
        val phones = mutableListOf<Phone>()
        val totalModels = favorites.size
        var completedRequests = 0

        if (totalModels == 0) {
            onComplete(phones)
            return
        }

        for (model in favorites) {
            productRepository.findPhoneByModel(model,
                onSuccess = { phone ->
                    phones.add(phone)
                    completedRequests++
                    if (completedRequests == totalModels) {
                        onComplete(phones)
                    }
                },
                onFailure = { e ->
                    completedRequests++
                    if (completedRequests == totalModels) {
                        onComplete(phones)
                    }
                }
            )
        }
    }

    private fun back() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
        finish()
    }
}