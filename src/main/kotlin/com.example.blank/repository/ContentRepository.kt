package com.example.blank.repository

import com.example.blank.entity.ContentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface ContentRepository : JpaRepository<ContentEntity, Long> {
    //fun findByContentId(contentId: Long): ContentEntity?
    fun findAllByTopicId(topicId: Long): List<ContentEntity>?
    fun findAllByType(type: String): List<ContentEntity>?
    //fun deleteByContentId(contentId: Long): ContentEntity?
    fun deleteAllByTopicId(topicId: Long)
    fun deleteAllByType(type: String)
    fun existsByTopicId(topicId: Long): Boolean
    fun existsByType(type: String): Boolean
}
