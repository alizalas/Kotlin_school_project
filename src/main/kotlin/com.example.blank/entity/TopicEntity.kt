package com.example.blank.entity

import com.example.blank.dto.TopicDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "Topics",
    indexes = [
        Index(columnList = "id", name = "topic_id_hidx"),
    ]
)
class TopicEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, length = 255, unique = true)
    var name: String,

    @Column(nullable = true)
    var description: String? = null,

    @Column(name = "created_by", nullable = false, length = 255)
    val createdBy: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

fun TopicEntity.updateTimestamp() {
        updatedAt = LocalDateTime.now()
    }

fun TopicEntity.toDto() = TopicDto(
    name = name,
    description = description,
    createdBy = createdBy,
    createdAt = createdAt,
    updatedAt = updatedAt
)