package com.example.app

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.entity.Comment
import com.example.app.repository.AuthRepository
import com.example.app.repository.ProductRepository
import com.example.app.repository.UserRepository
import com.example.app.service.CommentAdapter
import com.example.app.service.PhoneAdapter
import java.io.File

class InfoActivity : AppCompatActivity(){
    private lateinit var backButton: Button
   // private lateinit var favoriteButton: Button
    private lateinit var imageView1: ImageView
    private lateinit var imageView2: ImageView
    private lateinit var imageView3: ImageView

    private lateinit var deleteButton: Button

    private lateinit var textEditText: EditText
    private lateinit var rateEditText: EditText
    private lateinit var saveButton: Button

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
        //favoriteButton = findViewById(R.id.favoriteButton)
       // favoriteButton.setOnClickListener{addToFavorite()}

        textEditText = findViewById(R.id.textEditText)
        rateEditText = findViewById(R.id.rateEditText)
        saveButton = findViewById(R.id.buttonSave)

        //saveButton.setOnClickListener{saveComment()}

        // Получаем данные из Intent
        val brand = intent.getStringExtra("BRAND")
        val model = intent.getStringExtra("MODEL")?:"model"
        val os = intent.getStringExtra("OS")
        val storage = intent.getStringExtra("STORAGE")
        val price = intent.getStringExtra("PRICE")
        val color = intent.getStringExtra("COLOR")
        val releaseYear = intent.getStringExtra("RELEASEYEAR")
        val warrantyPeriod = intent.getStringExtra("WARRANTYPERIOD")

        saveButton.setOnClickListener{saveComment(model)}

        // Находим TextView и устанавливаем текст
        findViewById<TextView>(R.id.brandTextView).text = brand
        findViewById<TextView>(R.id.modelTextView).text = "Модель: ${model}"
        findViewById<TextView>(R.id.osTextView).text = os
        findViewById<TextView>(R.id.storageTextView).text = storage
        findViewById<TextView>(R.id.priceTextView).text = price
        findViewById<TextView>(R.id.colorTextView).text = color
        findViewById<TextView>(R.id.releaseYearTextView).text = releaseYear
        findViewById<TextView>(R.id.warrantyPeriodTextView).text = warrantyPeriod

        imageView1 = findViewById(R.id.imageView1)
        imageView2 = findViewById(R.id.imageView2)
        imageView3 = findViewById(R.id.imageView3)

        // Путь к директории
        val directory = File(filesDir, "images")
        // Путь к изображению
        val imageFile1 = File(directory, "1$model.png")

        // Проверяем, существует ли файл
        if (imageFile1.exists()) {
            // Загружаем изображение в ImageView
            val bitmap = BitmapFactory.decodeFile(imageFile1.absolutePath)
            imageView1.setImageBitmap(bitmap)
        } else {
            // Обработка случая, если файл не найден
           // imageView.setImageResource(R.drawable.placeholder) // Замените на ваш ресурс-заполнитель
        }

        val imageFile2 = File(directory, "2$model.png")

        // Проверяем, существует ли файл
        if (imageFile2.exists()) {
            // Загружаем изображение в ImageView
            val bitmap = BitmapFactory.decodeFile(imageFile2.absolutePath)
            imageView2.setImageBitmap(bitmap)
        } else {
            // Обработка случая, если файл не найден
            // imageView.setImageResource(R.drawable.placeholder) // Замените на ваш ресурс-заполнитель
        }

        val imageFile3 = File(directory, "3$model.png")

        // Проверяем, существует ли файл
        if (imageFile3.exists()) {
            // Загружаем изображение в ImageView
            val bitmap = BitmapFactory.decodeFile(imageFile3.absolutePath)
            imageView3.setImageBitmap(bitmap)
        } else {
            // Обработка случая, если файл не найден
            // imageView.setImageResource(R.drawable.placeholder) // Замените на ваш ресурс-заполнитель
        }

        readNames(model)

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

    private fun saveComment(model1:String){
        textEditText = findViewById(R.id.textEditText)
        rateEditText = findViewById(R.id.rateEditText)

        val user = authRepository.getCurrentUser()

        val email: String = user?.email ?: "default@example.com"
        val text = textEditText.text.toString()
        val rate = rateEditText.text.toString()

        val combined = combineStrings(email, text, rate)

        productRepository.addCommentByModel(model1, combined) { success, errorMessage ->
            if (success) {
                //context.Toast.makeText(this, "добавлено успешна!", Toast.LENGTH_SHORT).show()
            } else {
                println("Не удалосъ добавить в избранное: $errorMessage")
            }
        }

    }

    fun combineStrings(email: String, text: String, rate: String): String {
        return "$email$$text$$rate"
    }

    // Функция для разделения строки на отдельные элементы
    fun splitString(combinedString: String): Triple<String, String, String> {
        val parts = combinedString.split("$")
        return Triple(parts[0], parts[1], parts[2])
    }

    // Функция для чтения комментариев
    private fun readNames(model1: String) {
        val commentListt = mutableListOf<Comment>()

        productRepository.getCommentsByModel(model1) { comments, errorMessage ->
            if (comments != null) {
                // Преобразование комментариев в список объектов Comment
                val commentList = comments.map { combinedString ->
                    val (writeBy, text, rate) = splitString(combinedString)
                    val comment = Comment(writeBy, text, rate)
                    commentListt.add(comment)
                }

                val adapter = CommentAdapter(this, commentListt)
                val listView = findViewById<ListView>(R.id.listViewNames)
                listView.adapter = adapter

            } else {
                println("Ошибка: $errorMessage")
            }
        }
    }

    private fun updateListView(commentList: List<Comment>) {
        val listView: ListView = findViewById(R.id.listViewNames)

        // Создание адаптера для ListView
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, commentList.map { "${it.writeBy}: ${it.text} (Рейтинг: ${it.rate})" })

        // Установка адаптера в ListView
        listView.adapter = adapter
    }
}