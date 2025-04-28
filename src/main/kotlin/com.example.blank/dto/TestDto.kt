package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.TestEntity
import java.time.LocalDateTime

data class TestDto (
    @Schema(
        description = "Тип контента теста",
    )
    val contentType: String,

    @Schema(
        description = "Сложность теста",
    )
    val difficulty: String? = null,

    @Schema(
        description = "Вопросы теста в формате JSON",
    )
    val questions: String,

    @Schema(
        description = "Ответы на вопросы теста в формате JSON",
    )
    val answers: String,

    @Schema(
        description = "Дата и время создания теста",
    )
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Schema(
        description = "Дата и время последнего обновления теста",
    )
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

fun TestDto.toEntity() = TestEntity(
    contentType = contentType,
    difficulty = difficulty,
    questions = questions,
    answers = answers,
    createdAt = createdAt,
    updatedAt = updatedAt
)