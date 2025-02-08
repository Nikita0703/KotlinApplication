package com.example.app.entity

data class Phone(
   val brand: String = "",
   val model: String = "",
   val os: String = "",
   val storage: String = "",
   val price: String = "не указано", // Добавлено поле для цены
   val color: String = "не указано",   // Добавлено поле для цвета
   val releaseYear: String = "не указано", // Добавлено поле для года выпуска
   val warrantyPeriod: String = "не указано" // Добавлено поле для гарантии в месяцах
)


