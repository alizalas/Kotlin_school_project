package com.example.blank.repository

import com.example.blank.entity.UserTopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface UserTopicRepository : JpaRepository<UserTopicEntity, Long> {
    //fun findByUserTopicId(userTopicId: Long): UserTopicEntity?
    fun findAllByUserId(userId: Long): List<UserTopicEntity>?
    fun findAllByTopicId(topicId: Int): List<UserTopicEntity>?
    //fun deleteByUserTopicId(userTopicId: Long): UserTopicEntity?
}
