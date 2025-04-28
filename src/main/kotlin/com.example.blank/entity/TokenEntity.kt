package com.example.blank.entity

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "tokens")
data class TokenEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val value: String,

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    val user: UserAuthorizationEntity,

    val expiresAt: LocalDateTime? = null,

    val revoked: Boolean = false
)
