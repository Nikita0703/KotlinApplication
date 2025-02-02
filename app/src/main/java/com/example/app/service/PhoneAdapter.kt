package com.example.app.service

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.example.app.R
import com.example.app.entity.Phone

class PhoneAdapter (context: Context, private val phones: List<Phone>) : ArrayAdapter<Phone>(context, 0, phones) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Получаем телефон по позиции
        val phone = getItem(position)

        // Проверяем, существует ли уже представление (view), если нет, создаем новое
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_product, parent, false)

        // Заполняем данные в представление
        val brandTextView = listItemView.findViewById<TextView>(R.id.brandTextView)
        val modelTextView = listItemView.findViewById<TextView>(R.id.modelTextView)
        val osTextView = listItemView.findViewById<TextView>(R.id.osTextView)
        val storageTextView = listItemView.findViewById<TextView>(R.id.storageTextView)
        brandTextView.text = phone?.brand
        modelTextView.text = phone?.model
        osTextView.text = phone?.os;
        storageTextView.text = phone?.storage;
        return listItemView
    }
}