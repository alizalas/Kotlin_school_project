package com.example.blank.service

import com.example.blank.dto.UserTopicDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.UserTopicEntity
import com.example.blank.repository.UserTopicRepository
import com.example.blank.service.entity.services.UserTopicNotFoundException
import com.example.blank.service.entity.services.UserTopicService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class UserTopicServiceTest {

    private val userTopicRepository = mockk<UserTopicRepository>()
    private val userTopicService = UserTopicService(userTopicRepository)

    @Test
    fun `addUserTopic should save user topic when data is valid`() {
        val userTopicDto = UserTopicDto(
            userId = 1L,
            topicId = 1L,
            selectedAt = LocalDateTime.now()
        )
        every { userTopicRepository.save(any()) } returns userTopicDto.toEntity()

        userTopicService.addUserTopic(userTopicDto)

        verify { userTopicRepository.save(userTopicDto.toEntity()) }
    }

    @Test
    fun `getUserTopicById should return user topic when it exists`() {
        val userTopicId = 1L
        val userTopicEntity = UserTopicEntity(
            id = userTopicId,
            userId = 1L,
            topicId = 1L,
            selectedAt = LocalDateTime.now()
        )
        every { userTopicRepository.findById(userTopicId) } returns Optional.of(userTopicEntity)

        val result = userTopicService.getUserTopicById(userTopicId)

        result shouldBe userTopicEntity
    }

    @Test
    fun `getUserTopicById should throw UserTopicNotFoundException when user topic does not exist`() {
        val userTopicId = 1L
        every { userTopicRepository.findById(userTopicId) } returns Optional.empty()

        val exception = assertThrows<UserTopicNotFoundException> {
            userTopicService.getUserTopicById(userTopicId)
        }

        exception.message shouldBe "User topic with id $userTopicId not found"
    }

    @Test
    fun `getAllUserTopicsByUserId should return user topics when they exist`() {
        val userId = 1L
        val userTopicEntities = listOf(
            UserTopicEntity(
                id = 1L,
                userId = userId,
                topicId = 1L,
                selectedAt = LocalDateTime.now()
            ),
            UserTopicEntity(
                id = 2L,
                userId = userId,
                topicId = 2L,
                selectedAt = LocalDateTime.now()
            )
        )
        every { userTopicRepository.findAllByUserId(userId) } returns userTopicEntities

        val result = userTopicService.getAllUserTopicsByUserId(userId)

        result shouldBe userTopicEntities
    }

    @Test
    fun `getAllUserTopicsByUserId should throw UserTopicNotFoundException when no user topics exist`() {
        val userId = 1L
        every { userTopicRepository.findAllByUserId(userId) } returns null

        val exception = assertThrows<UserTopicNotFoundException> {
            userTopicService.getAllUserTopicsByUserId(userId)
        }

        exception.message shouldBe "No topics found for user with id $userId"
    }

    @Test
    fun `getAllUserTopicsByTopicId should return user topics when they exist`() {
        val topicId = 1L
        val userTopicEntities = listOf(
            UserTopicEntity(
                id = 1L,
                userId = 1L,
                topicId = topicId,
                selectedAt = LocalDateTime.now()
            ),
            UserTopicEntity(
                id = 2L,
                userId = 2L,
                topicId = topicId,
                selectedAt = LocalDateTime.now()
            )
        )
        every { userTopicRepository.findAllByTopicId(topicId.toInt()) } returns userTopicEntities

        val result = userTopicService.getAllUserTopicsByTopicId(topicId.toInt())

        result shouldBe userTopicEntities
    }

    @Test
    fun `getAllUserTopicsByTopicId should throw UserTopicNotFoundException when no user topics exist`() {
        val topicId = 1L
        every { userTopicRepository.findAllByTopicId(topicId.toInt()) } returns null

        val exception = assertThrows<UserTopicNotFoundException> {
            userTopicService.getAllUserTopicsByTopicId(topicId.toInt())
        }

        exception.message shouldBe "No users found for topic id $topicId"
    }

    @Test
    fun `deleteUserTopicById should return true when user topic exists`() {
        val userTopicId = 1L
        every { userTopicRepository.existsById(userTopicId) } returns true
        every { userTopicRepository.deleteById(userTopicId) } returns Unit

        val result = userTopicService.deleteUserTopicById(userTopicId)

        result shouldBe true
    }

    @Test
    fun `deleteUserTopicById should return false when user topic does not exist`() {
        val userTopicId = 1L
        every { userTopicRepository.existsById(userTopicId) } returns false

        val result = userTopicService.deleteUserTopicById(userTopicId)

        result shouldBe false
    }
}