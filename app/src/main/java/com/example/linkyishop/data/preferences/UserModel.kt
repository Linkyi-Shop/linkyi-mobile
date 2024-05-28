package com.example.linkyishop.data.preferences

data class UserModel(
    val name: String,
    val email: String,
    val token: String,
    val isActive: Boolean
)
