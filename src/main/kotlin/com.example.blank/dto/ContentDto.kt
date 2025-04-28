package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.ContentEntity
import java.time.LocalDateTime

data class ContentDto (
    @Schema(
        description = "Идентификатор темы, к которой относится контент",
    )
    val topicId: Long,

    @Schema(
        description = "Тип контента",
    )
    val type: String,

    @Schema(
        description = "Данные контента",
    )
    val contentData: String,

    @Schema(
        description = "Дата и время создания контента",
    )
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Schema(
        description = "Дата и время последнего обновления контента",
    )
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

fun ContentDto.toEntity() = ContentEntity(
    topicId = topicId,
    type = type,
    contentData = contentData,
    createdAt = createdAt,
    updatedAt = updatedAt
)