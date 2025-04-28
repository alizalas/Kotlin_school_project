package com.example.blank.repository

import com.example.blank.entity.UserAuthorizationEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface UserAuthorizationRepository : JpaRepository<UserAuthorizationEntity, Long> {
    fun findByUsername(username: String): UserAuthorizationEntity?
    fun findByUsernameAndPassword(username: String, password: String): UserAuthorizationEntity?
}
