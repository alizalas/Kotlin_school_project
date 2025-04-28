package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.TopicEntity
import java.time.LocalDateTime

data class TopicDto (
    @Schema(
        description = "Название темы",
    )
    val name: String,

    @Schema(
        description = "Описание темы",
    )
    val description: String? = null,

    @Schema(
        description = "Имя создателя темы",
    )
    val createdBy: String,

    @Schema(
        description = "Дата и время создания темы",
    )
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Schema(
        description = "Дата и время последнего обновления темы",
    )
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

fun TopicDto.toEntity() = TopicEntity(
    name = name,
    description = description,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt
)