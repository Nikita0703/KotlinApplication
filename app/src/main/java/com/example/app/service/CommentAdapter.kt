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

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val listItemView = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)

        val currentComment = comments[position]

        val autorTextView = listItemView.findViewById<TextView>(R.id.autorTextView)
        val textTextView = listItemView.findViewById<TextView>(R.id.textTextView)
        val rateTextView = listItemView.findViewById<TextView>(R.id.rateTextView)

        autorTextView.text = "От: ${currentComment.writeBy}"
        textTextView.text = "Текст: ${currentComment.text}"
        rateTextView.text = "Рейтинг: ${currentComment.rate}"

        return listItemView
    }

}