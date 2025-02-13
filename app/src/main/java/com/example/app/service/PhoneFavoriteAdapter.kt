package com.example.app.service

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.app.InfoActivity
import com.example.app.R
import com.example.app.entity.Phone
import com.example.app.repository.AuthRepository
import com.example.app.repository.ProductRepository
import com.example.app.repository.UserRepository
import java.io.File

class PhoneFavoriteAdapter (context: Context, private val phones: List<Phone>) : ArrayAdapter<Phone>(context, 0, phones) {
    private lateinit var favoriteButton: Button
    private val authRepository = AuthRepository()
    private val userRepository =  UserRepository()
    private val productRepository = ProductRepository()
    private var model = ""

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val phone = getItem(position)

        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_favorite_product, parent, false)

        val brandTextView = listItemView.findViewById<TextView>(R.id.brandTextView)
        val modelTextView = listItemView.findViewById<TextView>(R.id.modelTextView)

        val imageView = listItemView.findViewById<ImageView>(R.id.imageView)

        favoriteButton = listItemView.findViewById(R.id.favoriteButton)
        favoriteButton.setOnClickListener {
            phone?.model?.let { model ->
                removeFromFavorite(model)
            }
        }

        val directory = File(context.filesDir, "images")

        model = phone?.model?:"model"

        val imageFile = File(directory, "1$model.png")

        if (imageFile.exists()) {
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        } else {
            // imageView.setImageResource(R.drawable.placeholder) // Замените на ваш ресурс-заполнитель
        }

        brandTextView.text = phone?.brand
        modelTextView.text = phone?.model

        listItemView.setOnClickListener {
            val intent = Intent(context, InfoActivity::class.java).apply {
                putExtra("BRAND", phone?.brand)
                putExtra("MODEL", phone?.model)
                putExtra("OS", phone?.os)
                putExtra("STORAGE", phone?.storage)
            }
            context.startActivity(intent)
        }

        return listItemView
    }

    private fun removeFromFavorite(model1:String){
        val user = authRepository.getCurrentUser()
        val email: String = user?.email ?: "default@example.com"

        userRepository.deleteModelFromFavoritesByEmail(model1, email) { success, errorMessage ->
            if (success) {
                //context.Toast.makeText(this, "добавлено успешна!", Toast.LENGTH_SHORT).show()
            } else {
                println("Не удалосъ добавить в избранное: $errorMessage")
            }
        }
    }
}