package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.UserTopicEntity
import java.time.LocalDateTime

data class UserTopicDto(
    @Schema(
        description = "Идентификатор пользователя",
    )
    val userId: Long,

    @Schema(
        description = "Идентификатор темы",
    )
    val topicId: Long,

    @Schema(
        description = "Дата и время выбора темы",
    )
    val selectedAt: LocalDateTime = LocalDateTime.now()
)

fun UserTopicDto.toEntity() = UserTopicEntity(
    userId = userId,
    topicId = topicId,
    selectedAt = selectedAt
)