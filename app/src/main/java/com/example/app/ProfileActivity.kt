package com.example.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R.id.backButton
import com.example.app.entity.Phone

class ProfileActivity : AppCompatActivity() {
    private lateinit var editTextName : EditText
    private lateinit var editTextSurname : EditText
    private lateinit var editTextEmail : EditText
    private lateinit var editTextBirthday: EditText
    private lateinit var backButton: Button

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editTextName = findViewById(R.id.editTextName );
        editTextSurname = findViewById(R.id.editTextSurname );
        editTextEmail = findViewById(R.id.editTextEmail );
        editTextBirthday = findViewById(R.id.editTextBirthday);
        backButton = findViewById(R.id.backButton)

        backButton.setOnClickListener{back()}

        // Получение данных о телефоне (например, из Intent или создания нового объекта)
        val phone = Phone("Novichenko", "Nikita", "Nikita@gmail.com", "07.03.2005")

        // Установка данных в TextView
        editTextName.text = Editable.Factory.getInstance().newEditable("${phone.brand}")
        editTextSurname.text = Editable.Factory.getInstance().newEditable("${phone.model}")
        editTextEmail.text = Editable.Factory.getInstance().newEditable("${phone.os}")
        editTextBirthday.text = Editable.Factory.getInstance().newEditable("${phone.storage}")
    }

    private fun back() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }
}