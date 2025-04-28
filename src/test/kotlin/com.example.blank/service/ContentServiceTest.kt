package com.example.blank.service

import com.example.blank.dto.ContentDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.ContentEntity
import com.example.blank.repository.ContentRepository
import com.example.blank.service.entity.services.ContentNotFoundException
import com.example.blank.service.entity.services.ContentService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class ContentServiceTest {

    private val contentRepository = mockk<ContentRepository>()
    private val contentService = ContentService(contentRepository)

    @Test
    fun `addContent should save content when content data is valid`() {
        val contentDto = ContentDto(
            topicId = 1L,
            type = "Test Type",
            contentData = "Test Content Data",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { contentRepository.save(any()) } returns contentDto.toEntity()

        contentService.addContent(contentDto)

        verify { contentRepository.save(contentDto.toEntity()) }
    }

    @Test
    fun `getContentByContentId should return content when content exists`() {
        val contentId = 1L
        val contentEntity = ContentEntity(
            id = contentId,
            topicId = 1L,
            type = "Test Type",
            contentData = "Test Content Data"
        )
        every { contentRepository.findById(contentId) } returns Optional.of(contentEntity)

        val result = contentService.getContentByContentId(contentId)

        result shouldBe contentEntity
    }

    @Test
    fun `getContentByContentId should throw ContentNotFoundException when content does not exist`() {
        val contentId = 1L
        every { contentRepository.findById(contentId) } returns Optional.empty()

        val exception = assertThrows<ContentNotFoundException> {
            contentService.getContentByContentId(contentId)
        }

        exception.message shouldBe "Content with contentId $contentId not found"
    }

    @Test
    fun `getAllContentByTopicId should return content list when content exists`() {
        val topicId = 1L
        val contentEntities = listOf(
            ContentEntity(
                id = 1L,
                topicId = topicId,
                type = "Test Type",
                contentData = "Test Content Data"
            ),
            ContentEntity(
                id = 2L,
                topicId = topicId,
                type = "Test Type",
                contentData = "Test Content Data"
            )
        )
        every { contentRepository.findAllByTopicId(topicId) } returns contentEntities

        val result = contentService.getAllContentByTopicId(topicId)

        result shouldBe contentEntities
    }

    @Test
    fun `getAllContentByTopicId should throw ContentNotFoundException when no content exists`() {
        val topicId = 1L
        every { contentRepository.findAllByTopicId(topicId) } returns null

        val exception = assertThrows<ContentNotFoundException> {
            contentService.getAllContentByTopicId(topicId)
        }

        exception.message shouldBe "Content with topicId $topicId not found"
    }

    @Test
    fun `getAllContentByType should return content list when content exists`() {
        val type = "Test Type"
        val contentEntities = listOf(
            ContentEntity(
                id = 1L,
                topicId = 1L,
                type = type,
                contentData = "Test Content Data"
            ),
            ContentEntity(
                id = 2L,
                topicId = 2L,
                type = type,
                contentData = "Test Content Data"
            )
        )
        every { contentRepository.findAllByType(type) } returns contentEntities

        val result = contentService.getAllContentByType(type)

        result shouldBe contentEntities
    }

    @Test
    fun `getAllContentByType should throw ContentNotFoundException when no content exists`() {
        val type = "Test Type"
        every { contentRepository.findAllByType(type) } returns null

        val exception = assertThrows<ContentNotFoundException> {
            contentService.getAllContentByType(type)
        }

        exception.message shouldBe "No content with type $type found"
    }

    @Test
    fun `deleteContentByContentId should return true when content exists`() {
        val contentId = 1L
        every { contentRepository.existsById(contentId) } returns true
        every { contentRepository.deleteById(contentId) } returns Unit

        val result = contentService.deleteContentByContentId(contentId)

        result shouldBe true
    }

    @Test
    fun `deleteContentByContentId should return false when content does not exist`() {
        val contentId = 1L
        every { contentRepository.existsById(contentId) } returns false

        val result = contentService.deleteContentByContentId(contentId)

        result shouldBe false
    }

    @Test
    fun `deleteAllContentByTopicId should return true when content exists`() {
        val topicId = 1L
        every { contentRepository.existsByTopicId(topicId) } returns true
        every { contentRepository.deleteAllByTopicId(topicId) } returns Unit

        val result = contentService.deleteAllContentByTopicId(topicId)

        result shouldBe true
    }

    @Test
    fun `deleteAllContentByTopicId should return false when no content exists`() {
        val topicId = 1L
        every { contentRepository.existsByTopicId(topicId) } returns false

        val result = contentService.deleteAllContentByTopicId(topicId)

        result shouldBe false
    }

    @Test
    fun `deleteAllContentByType should return true when content exists`() {
        val type = "Test Type"
        every { contentRepository.existsByType(type) } returns true
        every { contentRepository.deleteAllByType(type) } returns Unit

        val result = contentService.deleteAllContentByType(type)

        result shouldBe true
    }

    @Test
    fun `deleteAllContentByType should return false when no content exists`() {
        val type = "Test Type"
        every { contentRepository.existsByType(type) } returns false

        val result = contentService.deleteAllContentByType(type)

        result shouldBe false
    }

    @Test
    fun `updateContentType should return updated content when content exists`() {
        val contentId = 1L
        val newType = "New Type"
        val contentEntity = ContentEntity(
            id = contentId,
            topicId = 1L,
            type = "Old Type",
            contentData = "Test Content Data"
        )
        every { contentRepository.findById(contentId) } returns Optional.of(contentEntity)
        every { contentRepository.save(any()) } returns contentEntity

        val result = contentService.updateContentType(contentId, newType)

        result.type shouldBe newType
        verify { contentRepository.save(contentEntity) }
    }

    @Test
    fun `updateContentType should throw ContentNotFoundException when content does not exist`() {
        val contentId = 1L
        val newType = "New Type"
        every { contentRepository.findById(contentId) } returns Optional.empty()

        val exception = assertThrows<ContentNotFoundException> {
            contentService.updateContentType(contentId, newType)
        }

        exception.message shouldBe "Content with contentId $contentId not found"
    }

    @Test
    fun `updateContentData should return updated content when content exists`() {
        val contentId = 1L
        val newData = "New Content Data"
        val contentEntity = ContentEntity(
            id = contentId,
            topicId = 1L,
            type = "Test Type",
            contentData = "Old Content Data"
        )
        every { contentRepository.findById(contentId) } returns Optional.of(contentEntity)
        every { contentRepository.save(any()) } returns contentEntity

        val result = contentService.updateContentData(contentId, newData)

        result.contentData shouldBe newData
        verify { contentRepository.save(contentEntity) }
    }

    @Test
    fun `updateContentData should throw ContentNotFoundException when content does not exist`() {
        val contentId = 1L
        val newData = "New Content Data"
        every { contentRepository.findById(contentId) } returns Optional.empty()

        val exception = assertThrows<ContentNotFoundException> {
            contentService.updateContentData(contentId, newData)
        }

        exception.message shouldBe "Content with contentId $contentId not found"
    }
}