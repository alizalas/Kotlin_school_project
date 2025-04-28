package com.example.blank.service

import com.example.blank.dto.TestDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.TestEntity
import com.example.blank.repository.TestRepository
import com.example.blank.service.entity.services.TestNotFoundException
import com.example.blank.service.entity.services.TestService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class TestServiceTest {

    private val testRepository = mockk<TestRepository>()
    private val testService = TestService(testRepository)

    @Test
    fun `addTest should save test when test data is valid`() {
        val testDto = TestDto(
            contentType = "Test Content",
            difficulty = "Easy",
            questions = "[]",
            answers = "[]",
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { testRepository.save(any()) } returns testDto.toEntity()

        testService.addTest(testDto)

        verify { testRepository.save(testDto.toEntity()) }
    }

    @Test
    fun `getTestByTestId should return test when test exists`() {
        val testId = 1L
        val testEntity = TestEntity(
            id = testId,
            contentType = "Test Content",
            difficulty = "Easy",
            questions = "[]",
            answers = "[]"
        )
        every { testRepository.findById(testId) } returns Optional.of(testEntity)

        val result = testService.getTestByTestId(testId)

        result shouldBe testEntity
    }

    @Test
    fun `getTestByTestId should throw TestNotFoundException when test does not exist`() {
        val testId = 1L
        every { testRepository.findById(testId) } returns Optional.empty()

        val exception = assertThrows<TestNotFoundException> {
            testService.getTestByTestId(testId)
        }

        exception.message shouldBe "Test with testId $testId not found"
    }

    @Test
    fun `getAllTestsByContentType should return tests when tests exist`() {
        val contentType = "Test Content"
        val testEntities = listOf(
            TestEntity(
                id = 1L,
                contentType = contentType,
                difficulty = "Easy",
                questions = "[]",
                answers = "[]"
            ),
            TestEntity(
                id = 2L,
                contentType = contentType,
                difficulty = "Medium",
                questions = "[]",
                answers = "[]"
            )
        )
        every { testRepository.findAllByContentType(contentType) } returns testEntities

        val result = testService.getAllTestsByContentType(contentType)

        result shouldBe testEntities
    }

    @Test
    fun `getAllTestsByContentType should throw TestNotFoundException when no tests exist`() {
        val contentType = "Test Content"
        every { testRepository.findAllByContentType(contentType) } returns null

        val exception = assertThrows<TestNotFoundException> {
            testService.getAllTestsByContentType(contentType)
        }

        exception.message shouldBe "No tests with contentType $contentType found"
    }

    @Test
    fun `getAllTestsByDifficulty should return tests when tests exist`() {
        val difficulty = "Easy"
        val testEntities = listOf(
            TestEntity(
                id = 1L,
                contentType = "Test Content",
                difficulty = difficulty,
                questions = "[]",
                answers = "[]"
            ),
            TestEntity(
                id = 2L,
                contentType = "Test Content",
                difficulty = difficulty,
                questions = "[]",
                answers = "[]"
            )
        )
        every { testRepository.findAllByDifficulty(difficulty) } returns testEntities

        val result = testService.getAllTestsByDifficulty(difficulty)

        result shouldBe testEntities
    }

    @Test
    fun `getAllTestsByDifficulty should throw TestNotFoundException when no tests exist`() {
        val difficulty = "Easy"
        every { testRepository.findAllByDifficulty(difficulty) } returns null

        val exception = assertThrows<TestNotFoundException> {
            testService.getAllTestsByDifficulty(difficulty)
        }

        exception.message shouldBe "No tests with difficulty $difficulty found"
    }

    @Test
    fun `deleteTestByTestId should return true when test exists`() {
        val testId = 1L
        every { testRepository.existsById(testId) } returns true
        every { testRepository.deleteById(testId) } returns Unit

        val result = testService.deleteTestByTestId(testId)

        result shouldBe true
    }

    @Test
    fun `deleteTestByTestId should return false when test does not exist`() {
        val testId = 1L
        every { testRepository.existsById(testId) } returns false

        val result = testService.deleteTestByTestId(testId)

        result shouldBe false
    }

    @Test
    fun `deleteAllTestByContentType should return true when tests exist`() {
        val contentType = "Test Content"
        every { testRepository.existsByContentType(contentType) } returns true
        every { testRepository.deleteAllByContentType(contentType) } returns Unit

        val result = testService.deleteAllTestByContentType(contentType)

        result shouldBe true
    }

    @Test
    fun `deleteAllTestByContentType should return false when no tests exist`() {
        val contentType = "Test Content"
        every { testRepository.existsByContentType(contentType) } returns false

        val result = testService.deleteAllTestByContentType(contentType)

        result shouldBe false
    }

    @Test
    fun `deleteAllTestByDifficulty should return true when tests exist`() {
        val difficulty = "Easy"
        every { testRepository.existsByDifficulty(difficulty) } returns true
        every { testRepository.deleteAllByDifficulty(difficulty) } returns Unit

        val result = testService.deleteAllTestByDifficulty(difficulty)

        result shouldBe true
    }

    @Test
    fun `deleteAllTestByDifficulty should return false when no tests exist`() {
        val difficulty = "Easy"
        every { testRepository.existsByDifficulty(difficulty) } returns false

        val result = testService.deleteAllTestByDifficulty(difficulty)

        result shouldBe false
    }

    @Test
    fun `updateTestDifficulty should return updated test when test exists`() {
        val testId = 1L
        val newDifficulty = "Hard"
        val testEntity = TestEntity(
            id = testId,
            contentType = "Test Content",
            difficulty = "Easy",
            questions = "[]",
            answers = "[]"
        )
        every { testRepository.findById(testId) } returns Optional.of(testEntity)
        every { testRepository.save(any()) } returns testEntity

        val result = testService.updateTestDifficulty(testId, newDifficulty)

        result.difficulty shouldBe newDifficulty
        verify { testRepository.save(testEntity) }
    }

    @Test
    fun `updateTestDifficulty should throw TestNotFoundException when test does not exist`() {
        val testId = 1L
        val newDifficulty = "Hard"
        every { testRepository.findById(testId) } returns Optional.empty()

        val exception = assertThrows<TestNotFoundException> {
            testService.updateTestDifficulty(testId, newDifficulty)
        }

        exception.message shouldBe "Test with testId $testId not found"
    }

    @Test
    fun `updateTestQuestions should return updated test when test exists`() {
        val testId = 1L
        val newQuestions = "[{\"question\": \"What is Kotlin?\"}]"
        val testEntity = TestEntity(
            id = testId,
            contentType = "Test Content",
            difficulty = "Easy",
            questions = "[]",
            answers = "[]"
        )
        every { testRepository.findById(testId) } returns Optional.of(testEntity)
        every { testRepository.save(any()) } returns testEntity

        val result = testService.updateTestQuestions(testId, newQuestions)

        result.questions shouldBe newQuestions
        verify { testRepository.save(testEntity) }
    }

    @Test
    fun `updateTestQuestions should throw TestNotFoundException when test does not exist`() {
        val testId = 1L
        val newQuestions = "[{\"question\": \"What is Kotlin?\"}]"
        every { testRepository.findById(testId) } returns Optional.empty()

        val exception = assertThrows<TestNotFoundException> {
            testService.updateTestQuestions(testId, newQuestions)
        }

        exception.message shouldBe "Test with testId $testId not found"
    }

    @Test
    fun `updateTestAnswers should return updated test when test exists`() {
        val testId = 1L
        val newAnswers = "[{\"answer\": \"A programming language\"}]"
        val testEntity = TestEntity(
            id = testId,
            contentType = "Test Content",
            difficulty = "Easy",
            questions = "[]",
            answers = "[]"
        )
        every { testRepository.findById(testId) } returns Optional.of(testEntity)
        every { testRepository.save(any()) } returns testEntity

        val result = testService.updateTestAnswers(testId, newAnswers)

        result.answers shouldBe newAnswers
        verify { testRepository.save(testEntity) }
    }

    @Test
    fun `updateTestAnswers should throw TestNotFoundException when test does not exist`() {
        val testId = 1L
        val newAnswers = "[{\"answer\": \"A programming language\"}]"
        every { testRepository.findById(testId) } returns Optional.empty()

        val exception = assertThrows<TestNotFoundException> {
            testService.updateTestAnswers(testId, newAnswers)
        }

        exception.message shouldBe "Test with testId $testId not found"
    }
}