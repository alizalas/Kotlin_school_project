package com.example.blank.dto

import io.swagger.v3.oas.annotations.media.Schema
import com.example.blank.entity.UserTestResultEntity
import java.time.LocalDateTime

data class UserTestResultDto (
    @Schema(
        description = "Идентификатор пользователя",
    )
    val userId: Long,

    @Schema(
        description = "Идентификатор теста",
    )
    val testId: Long,

    @Schema(
        description = "Результат теста (количество баллов)",
    )
    val score: Int,

    @Schema(
        description = "Время выполнения теста",
    )
    val time: Float,

    @Schema(
        description = "Количество попыток выполнения теста",
    )
    val count: Int,

    @Schema(
        description = "Дата и время завершения первой попытки теста",
    )
    val firstCompletedAt: LocalDateTime = LocalDateTime.now(),

    @Schema(
        description = "Дата и время завершения последней попытки теста",
    )
    val lastCompletedAt: LocalDateTime = LocalDateTime.now()
)

fun UserTestResultDto.toEntity() = UserTestResultEntity(
    userId = userId,
    testId = testId,
    score = score,
    time = time,
    count = count,
    firstCompletedAt = firstCompletedAt,
    lastCompletedAt = lastCompletedAt
)