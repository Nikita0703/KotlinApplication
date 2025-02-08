package com.example.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.repository.AuthRepository
import com.example.app.repository.ProductRepository
import com.example.app.repository.UserRepository
import java.io.File

class InfoActivity : AppCompatActivity(){
    private lateinit var backButton: Button
    private lateinit var favoriteButton: Button
    private lateinit var imageView: ImageView
    private lateinit var deleteButton: Button

    private val authRepository = AuthRepository()
    private val userRepository =  UserRepository()
    private val productRepository = ProductRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info) // Убедитесь, что вы создали layout для этого активити

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener{back()}
        deleteButton = findViewById(R.id.deleteButton)
        deleteButton.setOnClickListener{delete()}
        favoriteButton = findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener{addToFavorite()}

        // Получаем данные из Intent
        val brand = intent.getStringExtra("BRAND")
        val model = intent.getStringExtra("MODEL")
        val os = intent.getStringExtra("OS")
        val storage = intent.getStringExtra("STORAGE")

        // Находим TextView и устанавливаем текст
        findViewById<TextView>(R.id.brandTextView).text = brand
        findViewById<TextView>(R.id.modelTextView).text = model
        findViewById<TextView>(R.id.osTextView).text = os
        findViewById<TextView>(R.id.storageTextView).text = storage

        imageView = findViewById(R.id.imageView)

        // Путь к директории
        val directory = File(filesDir, "images")
        // Путь к изображению
        val imageFile = File(directory, "$model.png")

        // Проверяем, существует ли файл
        if (imageFile.exists()) {
            // Загружаем изображение в ImageView
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        } else {
            // Обработка случая, если файл не найден
           // imageView.setImageResource(R.drawable.placeholder) // Замените на ваш ресурс-заполнитель
        }
    }

    private fun back() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun addToFavorite(){
        val model = intent.getStringExtra("MODEL")?: "model"
        val user = authRepository.getCurrentUser()
        val email: String = user?.email ?: "default@example.com"

        userRepository.addFavoriteByEmail(email, model) { success, errorMessage ->
            if (success) {
                Toast.makeText(this, "добавлено успешна!", Toast.LENGTH_SHORT).show()
            } else {
                println("Не удалосъ добавить в избранное: $errorMessage")
            }
        }
    }

    private fun delete(){
        val brand = intent.getStringExtra("BRAND")
        val model = intent.getStringExtra("MODEL")?: "model"
        val os = intent.getStringExtra("OS")
        val storage = intent.getStringExtra("STORAGE")

        productRepository.getDocumentIdByModel(model, { documentId ->
            productRepository.deletePhone(documentId, {
                // Успешное удаление
                Toast.makeText(this, "удалено успешна!", Toast.LENGTH_SHORT).show()
            }, { exception ->
                // Обработка ошибки удаления
                Toast.makeText(this, "ошибка при удалении!", Toast.LENGTH_SHORT).show()
            })
        }, { exception ->
            // Обработка ошибки получения documentId
            Toast.makeText(this, "ошибки получения documentId!", Toast.LENGTH_SHORT).show()
        })
    }
}