package com.example.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.repository.AuthRepository
import com.google.firebase.database.*
import com.google.firebase.database.DatabaseReference

class MainActivity : AppCompatActivity() {

    private lateinit var authRepository: AuthRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        authRepository = AuthRepository()

        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val registerButton = findViewById<Button>(R.id.registerButton)
        val loginButton = findViewById<Button>(R.id.loginButton)

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            authRepository.registerUser(email, password) { success, message ->
                if (success) {
                    Toast.makeText(this, "Регистрация успешна!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Ошибка: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }

        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            authRepository.loginUser(email, password) { success, message ->
                if (success) {
                    Toast.makeText(this, "Авторизация успешна!", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProductActivity::class.java)
                    startActivity(intent)
                    finish() // Закрываем MainA
                } else {
                    Toast.makeText(this, "Ошибка: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
