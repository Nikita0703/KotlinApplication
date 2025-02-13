package com.example.app.entity

data class Phone(
   val brand: String = "",
   val model: String = "",
   val os: String = "",
   val storage: String = "",
   val price: String = "не указано",
   val color: String = "не указано",
   val releaseYear: String = "не указано",
   val warrantyPeriod: String = "не указано",
   val comments: MutableList<String> = mutableListOf()
)


