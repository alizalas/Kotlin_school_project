package com.example.blank.entity

import com.example.blank.dto.UserTopicDto
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(
    name = "user_topics",
    indexes = [
        Index(columnList = "id", name = "user_topic_id_hidx"),
        Index(columnList = "user_id", name = "user_id_hidx"),
        Index(columnList = "topic_id", name = "topic_id_hidx")
    ]
)
class UserTopicEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "topic_id", nullable = false)
    val topicId: Long,

    @Column(name = "selected_at", nullable = false)
    val selectedAt: LocalDateTime = LocalDateTime.now()
)

fun UserTopicEntity.toDto() = UserTopicDto(
    userId = userId,
    topicId = topicId,
    selectedAt = selectedAt
)