package com.example.blank.service.entity.services

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.UserTopicDto
import com.example.blank.dto.toEntity
import com.example.blank.repository.UserTopicRepository
import com.example.blank.entity.UserTopicEntity
import org.slf4j.LoggerFactory

@Service
class UserTopicService(
    private val userTopicRepository: UserTopicRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)
    fun addUserTopic(userTopic: UserTopicDto) {
        logger.info("Запрос на добавление темы $userTopic")
        userTopicRepository.save(userTopic.toEntity())
    }

//    fun getUserTopicById(userTopicId: Long): UserTopicEntity {
//        return userTopicRepository.findByUserTopicId(userTopicId)
//            ?: throw UserTopicNotFoundException("User topic with id $userTopicId not found")
//    }

    fun getUserTopicById(userTopicId: Long): UserTopicEntity {
        logger.info("Запрос на получение темы по Id:$userTopicId")
        return userTopicRepository.findById(userTopicId)
            .orElseThrow { UserTopicNotFoundException("User topic with id $userTopicId not found") }
    }

    fun getAllUserTopicsByUserId(userId: Long): List<UserTopicEntity> {
        logger.info("Запрос на получение тем пользователя по Id:$userId")
        return userTopicRepository.findAllByUserId(userId)
            ?: throw UserTopicNotFoundException("No topics found for user with id $userId")
    }

    fun getAllUserTopicsByTopicId(topicId: Int): List<UserTopicEntity> {
        logger.info("Запрос на получение всех пользователей по теме с Id:$topicId")
        return userTopicRepository.findAllByTopicId(topicId)
            ?: throw UserTopicNotFoundException("No users found for topic id $topicId")
    }

//    @Transactional
//    fun deleteUserTopicById(userTopicId: Long): UserTopicEntity {
//        val userTopic = userTopicRepository.findByUserTopicId(userTopicId)
//            ?: throw UserTopicNotFoundException("User topic with id $userTopicId not found")
//        userTopicRepository.deleteByUserTopicId(userTopicId)
//        return userTopic
//    }

    @Transactional
    fun deleteUserTopicById(userTopicId: Long): Boolean {
        logger.info("Запрос на удаление темы по Id:$userTopicId")
        return if (userTopicRepository.existsById(userTopicId)) {
            userTopicRepository.deleteById(userTopicId)
            true
        } else {
            false
        }
    }

}

@ResponseStatus(HttpStatus.NOT_FOUND)
class UserTopicNotFoundException(message: String) : RuntimeException(message)