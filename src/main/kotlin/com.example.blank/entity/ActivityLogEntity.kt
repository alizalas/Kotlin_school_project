package com.example.blank.entity

import com.example.blank.dto.ActivityLogDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "activity_log",
    indexes = [
        Index(columnList = "id", name = "activity_log_id_hidx"),
        Index(columnList = "user_id", name = "user_id_hidx")
    ]
)
class ActivityLogEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(nullable = false, length = 255)
    val action: String,

    @Column(nullable = true, columnDefinition = "jsonb")
    val details: String?,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)

fun ActivityLogEntity.toDto() = ActivityLogDto(
    userId = userId,
    action = action,
    details = details,
    createdAt = createdAt
)