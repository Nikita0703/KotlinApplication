package com.example.app

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R.id.backButton
import com.example.app.entity.Phone
import com.example.app.entity.User
import com.example.app.repository.AuthRepository
import com.example.app.repository.UserRepository

class ProfileActivity : AppCompatActivity() {
    private lateinit var editTextName : EditText
    private lateinit var editTextSurname : EditText
    private lateinit var editTextEmail : EditText
    private lateinit var editTextBirthday: EditText
    private lateinit var backButton: Button
    private lateinit var applyButton: Button
    private lateinit var favoriteButton: Button


    private val authRepository = AuthRepository()
    private val userRepository =  UserRepository()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        editTextName = findViewById(R.id.editTextName );
        editTextSurname = findViewById(R.id.editTextSurname );
        editTextEmail = findViewById(R.id.editTextEmail );
        editTextBirthday = findViewById(R.id.editTextBirthday);
        backButton = findViewById(R.id.backButton)
        applyButton = findViewById(R.id.applyButton)

        backButton.setOnClickListener{back()}
        applyButton.setOnClickListener{apply()}

        favoriteButton = findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener{favorite()}

        // Получение данных о телефоне (например, из Intent или создания нового объекта)
        val phone = Phone("Novichenko", "Nikita", "Nikita@gmail.com", "07.03.2005")

        val user = authRepository.getCurrentUser()
        val email: String = user?.email ?: "default@example.com"

        userRepository.findUserByEmail(email) { user, errorMessage ->
            if (user != null) {
                editTextName.text = Editable.Factory.getInstance().newEditable("${user.name}")
                editTextSurname.text = Editable.Factory.getInstance().newEditable("${user.surname}")
                editTextEmail.text = Editable.Factory.getInstance().newEditable("${user.email}")
                editTextBirthday.text = Editable.Factory.getInstance().newEditable("${user.birthday}")
            } else {
                editTextName.text = Editable.Factory.getInstance().newEditable("не указано")
                editTextSurname.text = Editable.Factory.getInstance().newEditable("не указвоно")
                editTextEmail.text = Editable.Factory.getInstance().newEditable("${email}")
                editTextBirthday.text = Editable.Factory.getInstance().newEditable("не указано")
            }
        }

        // Установка данных в TextView
        //editTextName.text = Editable.Factory.getInstance().newEditable("${phone.brand}")
        //editTextSurname.text = Editable.Factory.getInstance().newEditable("${phone.model}")
        //editTextEmail.text = Editable.Factory.getInstance().newEditable("${phone.os}")
       // editTextBirthday.text = Editable.Factory.getInstance().newEditable("${phone.storage}")
    }

    private fun back() {
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun apply(){
        val name = editTextName.text.toString();
        val surname = editTextSurname.text.toString();
        val birthday = editTextBirthday.text.toString();
        val user = authRepository.getCurrentUser()
        val email: String = user?.email ?: "default@example.com"

        val new = User(name, surname, email, birthday)

    userRepository.addUser(new,
    onSuccess = {
        Toast.makeText(this, "Телефон успешно добавлен", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, ProductActivity::class.java)
        startActivity(intent)
        finish()
    },
    onFailure = { exception ->
        Toast.makeText(this, "Ошибка при добавлении телефона: ${exception.message}", Toast.LENGTH_SHORT).show()
    }
    )

    }

    private fun favorite(){
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
        finish()
    }
}