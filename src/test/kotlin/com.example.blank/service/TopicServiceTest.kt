package com.example.blank.service

import com.example.blank.dto.TopicDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.TopicEntity
import com.example.blank.repository.TopicRepository
import com.example.blank.service.entity.services.TopicNotFoundException
import com.example.blank.service.entity.services.TopicService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class TopicServiceTest {

    private val topicRepository = mockk<TopicRepository>()
    private val topicService = TopicService(topicRepository)

    @Test
    fun `addTopic should save topic when topic data is valid`() {
        val topicDto = TopicDto(
            name = "Test Topic",
            description = "Test Description",
            createdBy = "testUser",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { topicRepository.save(any()) } returns topicDto.toEntity()

        topicService.addTopic(topicDto)

        verify { topicRepository.save(topicDto.toEntity()) }
    }

    @Test
    fun `getTopicByTopicId should return topic when topic exists`() {
        val topicId = 1L
        val topicEntity = TopicEntity(
            id = topicId,
            name = "Test Topic",
            description = "Test Description",
            createdBy = "testUser"
        )
        every { topicRepository.findById(topicId) } returns Optional.of(topicEntity)

        val result = topicService.getTopicByTopicId(topicId)

        result shouldBe topicEntity
    }

    @Test
    fun `getTopicByTopicId should throw TopicNotFoundException when topic does not exist`() {
        val topicId = 1L
        every { topicRepository.findById(topicId) } returns Optional.empty()

        val exception = assertThrows<TopicNotFoundException> {
            topicService.getTopicByTopicId(topicId)
        }

        exception.message shouldBe "Topic with topicId $topicId not found"
    }

    @Test
    fun `getTopicByName should return topic when topic exists`() {
        val name = "Test Topic"
        val topicEntity = TopicEntity(
            id = 1L,
            name = name,
            description = "Test Description",
            createdBy = "testUser"
        )
        every { topicRepository.findByName(name) } returns topicEntity

        val result = topicService.getTopicByName(name)

        result shouldBe topicEntity
    }

    @Test
    fun `getTopicByName should throw TopicNotFoundException when topic does not exist`() {
        val name = "Test Topic"
        every { topicRepository.findByName(name) } returns null

        val exception = assertThrows<TopicNotFoundException> {
            topicService.getTopicByName(name)
        }

        exception.message shouldBe "Topic with name $name not found"
    }

    @Test
    fun `getAllTopicsByCreatedBy should return topics when topics exist`() {
        val createdBy = "testUser"
        val topicEntities = listOf(
            TopicEntity(
                id = 1L,
                name = "Test Topic 1",
                description = "Test Description 1",
                createdBy = createdBy
            ),
            TopicEntity(
                id = 2L,
                name = "Test Topic 2",
                description = "Test Description 2",
                createdBy = createdBy
            )
        )
        every { topicRepository.findAllByCreatedBy(createdBy) } returns topicEntities

        val result = topicService.getAllTopicsByCreatedBy(createdBy)

        result shouldBe topicEntities
    }

    @Test
    fun `getAllTopicsByCreatedBy should throw TopicNotFoundException when no topics exist`() {
        val createdBy = "testUser"
        every { topicRepository.findAllByCreatedBy(createdBy) } returns null

        val exception = assertThrows<TopicNotFoundException> {
            topicService.getAllTopicsByCreatedBy(createdBy)
        }

        exception.message shouldBe "No topics created by $createdBy found"
    }

    @Test
    fun `deleteTopicByTopicId should return true when topic exists`() {
        val topicId = 1L
        every { topicRepository.existsById(topicId) } returns true
        every { topicRepository.deleteById(topicId) } returns Unit

        val result = topicService.deleteTopicByTopicId(topicId)

        result shouldBe true
    }

    @Test
    fun `deleteTopicByTopicId should return false when topic does not exist`() {
        val topicId = 1L
        every { topicRepository.existsById(topicId) } returns false

        val result = topicService.deleteTopicByTopicId(topicId)

        result shouldBe false
    }

    @Test
    fun `deleteTopicByName should return true when topic exists`() {
        val name = "Test Topic"
        every { topicRepository.existsByName(name) } returns true
        every { topicRepository.deleteByName(name) } returns Unit

        val result = topicService.deleteTopicByName(name)

        result shouldBe true
    }

    @Test
    fun `deleteTopicByName should return false when topic does not exist`() {
        val name = "Test Topic"
        every { topicRepository.existsByName(name) } returns false

        val result = topicService.deleteTopicByName(name)

        result shouldBe false
    }

    @Test
    fun `deleteAllTopicsByCreatedBy should return true when topics exist`() {
        val createdBy = "testUser"
        every { topicRepository.existsByCreatedBy(createdBy) } returns true
        every { topicRepository.deleteAllByCreatedBy(createdBy) } returns Unit

        val result = topicService.deleteAllTopicsByCreatedBy(createdBy)

        result shouldBe true
    }

    @Test
    fun `deleteAllTopicsByCreatedBy should return false when no topics exist`() {
        val createdBy = "testUser"
        every { topicRepository.existsByCreatedBy(createdBy) } returns false

        val result = topicService.deleteAllTopicsByCreatedBy(createdBy)

        result shouldBe false
    }

    @Test
    fun `updateTopicName should return updated topic when topic exists`() {
        val topicId = 1L
        val newName = "New Topic Name"
        val topicEntity = TopicEntity(
            id = topicId,
            name = "Old Topic Name",
            description = "Test Description",
            createdBy = "testUser"
        )
        every { topicRepository.findById(topicId) } returns Optional.of(topicEntity)
        every { topicRepository.save(any()) } returns topicEntity

        val result = topicService.updateTopicName(topicId, newName)

        result.name shouldBe newName
        verify { topicRepository.save(topicEntity) }
    }

    @Test
    fun `updateTopicName should throw TopicNotFoundException when topic does not exist`() {
        val topicId = 1L
        val newName = "New Topic Name"
        every { topicRepository.findById(topicId) } returns Optional.empty()

        val exception = assertThrows<TopicNotFoundException> {
            topicService.updateTopicName(topicId, newName)
        }

        exception.message shouldBe "Topic with topicId $topicId not found"
    }

    @Test
    fun `updateTopicDescription should return updated topic when topic exists`() {
        val topicId = 1L
        val newDescription = "New Topic Description"
        val topicEntity = TopicEntity(
            id = topicId,
            name = "Test Topic",
            description = "Old Topic Description",
            createdBy = "testUser"
        )
        every { topicRepository.findById(topicId) } returns Optional.of(topicEntity)
        every { topicRepository.save(any()) } returns topicEntity

        val result = topicService.updateTopicDescription(topicId, newDescription)

        result.description shouldBe newDescription
        verify { topicRepository.save(topicEntity) }
    }

    @Test
    fun `updateTopicDescription should throw TopicNotFoundException when topic does not exist`() {
        val topicId = 1L
        val newDescription = "New Topic Description"
        every { topicRepository.findById(topicId) } returns Optional.empty()

        val exception = assertThrows<TopicNotFoundException> {
            topicService.updateTopicDescription(topicId, newDescription)
        }

        exception.message shouldBe "Topic with topicId $topicId not found"
    }
}