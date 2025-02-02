package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.entity.Phone
import com.example.app.repository.ProductRepository

class AddProductActivity  : AppCompatActivity() {
    private lateinit var brandEditText: EditText
    private lateinit var modelEditText: EditText
    private lateinit var osEditText: EditText
    private lateinit var storageEditText: EditText
    private lateinit var addPhoneButton: Button
    private lateinit var backButton: Button


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

        backButton.setOnClickListener{back()}

        // Установка обработчика нажатия на кнопку
        addPhoneButton.setOnClickListener {
            addPhone()
        }
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
}