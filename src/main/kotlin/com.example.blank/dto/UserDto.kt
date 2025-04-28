package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.UserEntity
import java.time.LocalDateTime

data class UserDto (
    @Schema(
        description = "Идентификатор пользователя в Telegram",
    )
    val telegramId: Long,

    @Schema(
        description = "Имя пользователя",
    )
    val username: String,

    @Schema(
        description = "Полное имя пользователя",
    )
    val fullName: String? = null,

    @Schema(
        description = "Данные профиля пользователя в формате JSON",
    )
    val profileData: String? = null,

    @Schema(
        description = "Рейтинг пользователя",
    )
    val rating: Int = 0,

    @Schema(
        description = "Текущая серия (streak) пользователя",
    )
    val streak: Int = 0,

    @Schema(
        description = "Дата и время создания пользователя",
    )
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Schema(
        description = "Дата и время последнего обновления пользователя",
    )
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

fun UserDto.toEntity() = UserEntity(
    telegramId = telegramId,
    username = username,
    fullName = fullName,
    profileData = profileData,
    rating = rating,
    streak = streak,
    createdAt = createdAt,
    updatedAt = updatedAt
)