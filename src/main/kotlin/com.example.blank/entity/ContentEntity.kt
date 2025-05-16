package com.example.blank.entity

import com.example.blank.dto.ContentDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "content",
    indexes = [
        Index(columnList = "id", name = "content_id_hidx"),
        Index(columnList = "topic_id", name = "topic_id_hidx")
    ]
)
class ContentEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "topic_id", nullable = false)
    val topicId: Long,

    @Column(nullable = false, length = 50)
    var type: String,

    @Column(name = "content_data", nullable = false)
    var contentData: String,

    @Column(name = "created_at", nullable = false, updatable = false)
    val createdAt: LocalDateTime = LocalDateTime.now(),

    @Column(name = "updated_at", nullable = false)
    var updatedAt: LocalDateTime = LocalDateTime.now()
)

fun ContentEntity.updateTimestamp() {
    updatedAt = LocalDateTime.now()
}

fun ContentEntity.toDto() = ContentDto(
    topicId = topicId,
    type = type,
    contentData = contentData,
    createdAt = createdAt,
    updatedAt = updatedAt
)