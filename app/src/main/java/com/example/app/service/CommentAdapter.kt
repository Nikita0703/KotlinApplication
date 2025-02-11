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
import com.example.app.entity.Comment
import com.example.app.entity.Phone
import java.io.File

class CommentAdapter(context: Context, private val comments: List<Comment>) : ArrayAdapter<Comment>(context, 0, comments) {

    // Получаем представление для элемента списка
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        // Получаем представление для элемента списка
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)

        // Получаем текущий комментарий по позиции
        val currentComment = comments[position]

        // Заполняем данные в представление
        val autorTextView = listItemView.findViewById<TextView>(R.id.autorTextView)
        val textTextView = listItemView.findViewById<TextView>(R.id.textTextView)
        val rateTextView = listItemView.findViewById<TextView>(R.id.rateTextView)

        // Устанавливаем текст в TextView
        autorTextView.text = "От: ${currentComment.writeBy}" // Предполагается, что у вас есть поле writeBy в классе Comment
        textTextView.text = "Текст: ${currentComment.text}" // Предполагается, что у вас есть поле text в классе Comment
        rateTextView.text = "Рейтинг: ${currentComment.rate}" // Предполагается, что у вас есть поле rate в классе Comment

        return listItemView
    }

}