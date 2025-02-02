package com.example.app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class InfoActivity : AppCompatActivity(){
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info) // Убедитесь, что вы создали layout для этого активити

        backButton = findViewById(R.id.backButton)
        backButton.setOnClickListener{back()}

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
    }

    private fun back() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }
}