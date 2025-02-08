package com.example.app

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.app.R.id.backButton
import com.example.app.entity.Phone
import com.example.app.entity.User
import com.example.app.repository.AuthRepository
import com.example.app.repository.UserRepository
import java.io.File
import java.io.FileOutputStream

class ProfileActivity : AppCompatActivity() {
    private lateinit var editTextName : EditText
    private lateinit var editTextSurname : EditText
    private lateinit var editTextEmail : EditText
    private lateinit var editTextBirthday: EditText
    private lateinit var editTextPhone: EditText
    private lateinit var editTextGender: EditText

    private lateinit var backButton: Button
    private lateinit var applyButton: Button
    private lateinit var favoriteButton: Button

    private lateinit var imageView: ImageView

    private val IMAGE_PICK_CODE = 1000


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
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextGender = findViewById(R.id.editTextGender);

        backButton = findViewById(R.id.backButton)
        applyButton = findViewById(R.id.applyButton)

        backButton.setOnClickListener{back()}
        applyButton.setOnClickListener{apply()}

        favoriteButton = findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener{favorite()}

        imageView = findViewById(R.id.imageView)

        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        selectImageButton.setOnClickListener {
            //pickImageFromGallery()
            pickImageFromDownloads()
        }

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
                editTextPhone.text = Editable.Factory.getInstance().newEditable("${user.phoneNumber}")
                editTextGender.text = Editable.Factory.getInstance().newEditable("${user.gender}")
            } else {
                editTextName.text = Editable.Factory.getInstance().newEditable("не указано")
                editTextSurname.text = Editable.Factory.getInstance().newEditable("не указвоно")
                editTextEmail.text = Editable.Factory.getInstance().newEditable("${email}")
                editTextBirthday.text = Editable.Factory.getInstance().newEditable("не указано")
                editTextPhone.text = Editable.Factory.getInstance().newEditable("не указано")
                editTextGender.text = Editable.Factory.getInstance().newEditable("не указано")
            }
        }


        val directory = File(filesDir, "images")
// Путь к изображению
        val imageFile = File(directory, "$email.png")

// Проверяем, существует ли файл
        if (imageFile.exists()) {
            // Загружаем изображение в ImageView
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        } else {
            // Загружаем изображение из drawable
            val drawableId = R.drawable.unnamed // Убедитесь, что у вас есть изображение unnamed.jpg в папке drawable
            val bitmap = BitmapFactory.decodeResource(resources, drawableId)
            imageView.setImageBitmap(bitmap)
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
        val phoneNumber = editTextPhone.text.toString()
        val gender = editTextGender.text.toString()

        val new = User(name, surname, email, birthday,phoneNumber,gender)

    /*userRepository.addUser(new,
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


     */

        userRepository.getUserByEmail(email, onSuccess = { existingUser ->
            if (existingUser != null) {
                userRepository.getDocumentIdByEmail(email, { documentId ->
                    userRepository.updateUser(documentId, new, onSuccess = {
                        Toast.makeText(this, "Данные пользователя успешно обновлены", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, ProductActivity::class.java)
                        startActivity(intent)
                        finish()
                    }, onFailure = { exception ->
                        Toast.makeText(this, "Ошибка при обновлении данных: ${exception.message}", Toast.LENGTH_SHORT).show()
                    })
                    }, { exception ->
                        // Обработка ошибки удаления
                        Toast.makeText(this, "ошибка при удалении!", Toast.LENGTH_SHORT).show()
                    })
            } else {
                // Пользователь не существует, вызываем метод add
                userRepository.addUser(new, onSuccess = {
                    Toast.makeText(this, "Пользователь успешно добавлен", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, ProductActivity::class.java)
                    startActivity(intent)
                    finish()
                }, onFailure = { exception ->
                    Toast.makeText(this, "Ошибка при добавлении пользователя: ${exception.message}", Toast.LENGTH_SHORT).show()
                })
            }
        }, onFailure = { exception ->
            Toast.makeText(this, "Ошибка при проверке существования пользователя: ${exception.message}", Toast.LENGTH_SHORT).show()
        })

        saveImageToStorage()

    }

    private fun favorite(){
        val intent = Intent(this, FavoriteActivity::class.java)
        startActivity(intent)
        finish()
    }

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

    private fun saveImageToStorage() {
        val email = editTextEmail.text.toString()

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
            val file = File(directory, "$email.png")
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