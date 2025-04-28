package com.example.blank.repository

import org.springframework.data.jpa.repository.JpaRepository
import com.example.blank.entity.TokenEntity
import com.example.blank.entity.UserAuthorizationEntity
import org.springframework.stereotype.Repository

@Repository
interface TokenRepository: JpaRepository<TokenEntity, Long> {
    fun findByUser(user: UserAuthorizationEntity): List<TokenEntity>?
    fun findByValue(value: String): TokenEntity?
}