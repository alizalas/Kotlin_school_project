package com.example.blank.entity

import jakarta.persistence.*

@Entity
@Table(name = "users_authorization")
class UserAuthorizationEntity (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false, unique = true)
    val username: String,

    @Column(nullable = false)
    val password: String,

    @Column(name = "telegram_id", nullable = false, unique = true)
    val telegramId: Long,

    @Column(name = "full_name", nullable = true, length = 255)
    var fullName: String? = null,

    @Column(name = "profile_data", nullable = true, columnDefinition = "jsonb")
    var profileData: String? = null,
)