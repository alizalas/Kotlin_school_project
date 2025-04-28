package com.example.blank.repository

import com.example.blank.entity.TopicEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface TopicRepository : JpaRepository<TopicEntity, Long> {
    //fun findByTopicId(topicId: Long): TopicEntity?
    fun findByName(name: String): TopicEntity?
    fun findAllByCreatedBy(createdBy: String): List<TopicEntity>?
    //fun deleteByTopicId(topicId: Long): TopicEntity?
    fun deleteByName(name: String)
    fun deleteAllByCreatedBy(createdBy: String)
    fun existsByName(name: String): Boolean
    fun existsByCreatedBy(createdBy: String): Boolean
}
