package com.example.app

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.entity.Phone
import com.example.app.repository.ProductRepository
import java.io.File
import java.io.FileOutputStream

class AddProductActivity  : AppCompatActivity() {
    private lateinit var brandEditText: EditText
    private lateinit var modelEditText: EditText
    private lateinit var osEditText: EditText
    private lateinit var storageEditText: EditText
    private lateinit var addPhoneButton: Button
    private lateinit var backButton: Button
    private lateinit var imageView: ImageView

    private val IMAGE_PICK_CODE = 1000


    private val phoneRepo = ProductRepository() // Предполагается, что у вас есть класс PhoneRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        // Инициализация полей
        brandEditText = findViewById(R.id.brandEditText)
        modelEditText = findViewById(R.id.modelEditText)
        osEditText = findViewById(R.id.osEditText)
        storageEditText = findViewById(R.id.storageEditText)
        addPhoneButton = findViewById(R.id.addPhoneButton)
        backButton = findViewById(R.id.backButton)
        imageView = findViewById(R.id.imageView)

        backButton.setOnClickListener{back()}

        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            //pickImageFromGallery()
            pickImageFromDownloads()
        }

        // Установка обработчика нажатия на кнопку
        addPhoneButton.setOnClickListener {
            addPhone()
        }
    }

   /* private fun pickImageFromGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    */
   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
       super.onActivityResult(requestCode, resultCode, data)
       if (requestCode == IMAGE_PICK_CODE && resultCode == Activity.RESULT_OK) {
           val imageUri: Uri? = data?.data
           imageView.setImageURI(imageUri) // Отобразить выбранное изображение
       }
   }

    private fun pickImageFromDownloads() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "image/*" // Указываем, что мы хотим выбрать изображение
            putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png")) // Можно указать конкретные типы изображений
        }
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }


    private fun addPhone() {
        val brand = brandEditText.text.toString()
        val model = modelEditText.text.toString()
        val os = osEditText.text.toString()
        val storage = storageEditText.text.toString()

        if (brand.isEmpty() || model.isEmpty() || os.isEmpty() || storage == null) {
            Toast.makeText(this, "Пожалуйста, заполните все поля корректно", Toast.LENGTH_SHORT).show()
            return
        }

        val phone = Phone(brand, model, os, storage)

        saveImageToStorage()

        phoneRepo.addPhone(phone,
            onSuccess = {
                Toast.makeText(this, "Телефон успешно добавлен", Toast.LENGTH_SHORT).show()
                clearFields()
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
                finish()
            },
            onFailure = { exception ->
                Toast.makeText(this, "Ошибка при добавлении телефона: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
        )

       // saveImageToStorage()
    }

    private fun clearFields() {
        brandEditText.text.clear()
        modelEditText.text.clear()
        osEditText.text.clear()
        storageEditText.text.clear()
    }

    private fun back() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveImageToStorage() {
        val brandName = brandEditText.text.toString()
        val model = modelEditText.text.toString()

        // Получите изображение из ImageView (предполагаем, что оно уже выбрано)
        imageView.isDrawingCacheEnabled = true
        imageView.buildDrawingCache()
        val bitmap = imageView.drawingCache

        // Создайте папку для хранения изображений, если она не существует
        val directory = File(filesDir, "images")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        // Сохраните изображение в файл с именем из поля brandEditText
        try {
            val file = File(directory, "$model.png")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
            imageView.isDrawingCacheEnabled = false // Отключить кэширование
            // Вы можете добавить уведомление об успешном сохранении
        } catch (e: Exception) {
            e.printStackTrace()
            // Обработка ошибок
        }
    }
}