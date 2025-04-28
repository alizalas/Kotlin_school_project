package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.ActivityLogEntity
import java.time.LocalDateTime

data class ActivityLogDto (
    @Schema(
        description = "Идентификатор пользователя, связанного с действием",
    )
    val userId: Long,

    @Schema(
        description = "Действие, выполненное пользователем",
    )
    val action: String,

    @Schema(
        description = "Детали действия в формате JSON (опционально)",
    )
    val details: String? = null,

    @Schema(
        description = "Дата и время создания записи",
    )
    val createdAt: LocalDateTime = LocalDateTime.now()
)

fun ActivityLogDto.toEntity() = ActivityLogEntity(
    userId = userId,
    action = action,
    details = details,
    createdAt = createdAt
)