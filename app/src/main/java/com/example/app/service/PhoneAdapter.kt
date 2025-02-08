package com.example.app.service

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.app.InfoActivity
import com.example.app.R
import com.example.app.entity.Phone
import java.io.File

class PhoneAdapter (context: Context, private val phones: List<Phone>) : ArrayAdapter<Phone>(context, 0, phones) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Получаем телефон по позиции
        val phone = getItem(position)

        // Проверяем, существует ли уже представление (view), если нет, создаем новое
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        // Заполняем данные в представление
        val brandTextView = listItemView.findViewById<TextView>(R.id.brandTextView)
        val modelTextView = listItemView.findViewById<TextView>(R.id.modelTextView)

        val imageView = listItemView.findViewById<ImageView>(R.id.imageView)

        // Путь к директории
        val directory = File(context.filesDir, "images")
        // Путь к изображению

        val model = phone?.model

        val imageFile = File(directory, "$model.png")

        // Проверяем, существует ли файл
        if (imageFile.exists()) {
            // Загружаем изображение в ImageView
            val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            imageView.setImageBitmap(bitmap)
        } else {
            // Обработка случая, если файл не найден
            // imageView.setImageResource(R.drawable.placeholder) // Замените на ваш ресурс-заполнитель
        }

        brandTextView.text = phone?.brand
        modelTextView.text = phone?.model

        // Устанавливаем обработчик клика
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
}