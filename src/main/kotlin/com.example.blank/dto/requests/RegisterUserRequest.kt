package com.example.blank.dto.requests

data class RegisterUserRequest (
    val username: String,
    val password: String,
    val telegramId: Long,
    val fullName: String? = null,
    val profileData: String? = null,
)
