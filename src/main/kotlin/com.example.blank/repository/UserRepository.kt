package com.example.blank.repository

import com.example.blank.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface UserRepository : JpaRepository<UserEntity, Long> {
    //fun findByUserId(userId: Long): UserEntity?
    fun findByTelegramId(telegramId: Long): UserEntity?
    fun findAllByRating(rating: Int): List<UserEntity>?
    fun findAllByStreak(streak: Int): List<UserEntity>?
    //fun deleteByUserId(userId: Long): UserEntity?
    fun deleteByTelegramId(telegramId: Long)
    fun existsByTelegramId(telegramId: Long): Boolean
}
