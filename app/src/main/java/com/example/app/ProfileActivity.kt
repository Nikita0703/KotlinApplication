package com.example.app

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app.entity.Phone

class ProfileActivity : AppCompatActivity() {
    private lateinit var textViewBrand: TextView
    private lateinit var textViewModel: TextView
    private lateinit var textViewOs: TextView
    private lateinit var textViewStorage: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        textViewBrand= findViewById(R.id.textViewBrand);
        textViewModel= findViewById(R.id.textViewModel);
        textViewOs= findViewById(R.id.textViewOs);
        textViewStorage = findViewById(R.id.textViewStorage);

        // Получение данных о телефоне (например, из Intent или создания нового объекта)
        val phone = Phone("Samsung", "Galaxy S21", "Android", "128")

        // Установка данных в TextView
        textViewBrand.text = "Бренд: ${phone.brand}"
        textViewModel.text = "Модель: ${phone.model}"
        textViewOs.text = "ОС: ${phone.os}"
        textViewStorage.text = "Хранилище: ${phone.storage}GB"
    }
}