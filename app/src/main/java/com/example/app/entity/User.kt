package com.example.app.entity

data class User (
    val name: String = " ",
    val surname: String = " ",
    val email: String = " ",
    val birthday: String = " ",
    val favorites: MutableList<String> = mutableListOf()
)
