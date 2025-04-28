package com.example.blank.service

import com.example.blank.dto.UserTestResultDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.UserTestResultEntity
import com.example.blank.repository.UserTestResultRepository
import com.example.blank.service.entity.services.UserTestResultNotFoundException
import com.example.blank.service.entity.services.UserTestResultService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class UserTestResultServiceTest {

    private val userTestResultRepository = mockk<UserTestResultRepository>()
    private val userTestResultService = UserTestResultService(userTestResultRepository)

    @Test
    fun `addUserTestResult should save user test result when data is valid`() {
        val userTestResultDto = UserTestResultDto(
            userId = 1L,
            testId = 1L,
            score = 100,
            time = 60.5f,
            count = 1,
            firstCompletedAt = LocalDateTime.now(),
            lastCompletedAt = LocalDateTime.now()
        )
        every { userTestResultRepository.save(any()) } returns userTestResultDto.toEntity()

        userTestResultService.addUserTestResult(userTestResultDto)

        verify { userTestResultRepository.save(userTestResultDto.toEntity()) }
    }

    @Test
    fun `getUserTestResultById should return user test result when it exists`() {
        val userTestResultId = 1L
        val userTestResultEntity = UserTestResultEntity(
            id = userTestResultId,
            userId = 1L,
            testId = 1L,
            score = 100,
            time = 60.5f
        )
        every { userTestResultRepository.findById(userTestResultId) } returns Optional.of(userTestResultEntity)

        val result = userTestResultService.getUserTestResultById(userTestResultId)

        result shouldBe userTestResultEntity
    }

    @Test
    fun `getUserTestResultById should throw UserTestResultNotFoundException when user test result does not exist`() {
        val userTestResultId = 1L
        every { userTestResultRepository.findById(userTestResultId) } returns Optional.empty()

        val exception = assertThrows<UserTestResultNotFoundException> {
            userTestResultService.getUserTestResultById(userTestResultId)
        }

        exception.message shouldBe "User test result with id $userTestResultId not found"
    }

    @Test
    fun `getAllUserTestResultsByUserId should return user test results when they exist`() {
        val userId = 1L
        val userTestResultEntities = listOf(
            UserTestResultEntity(
                id = 1L,
                userId = userId,
                testId = 1L,
                score = 100,
                time = 60.5f
            ),
            UserTestResultEntity(
                id = 2L,
                userId = userId,
                testId = 2L,
                score = 90,
                time = 70.5f
            )
        )
        every { userTestResultRepository.findAllByUserId(userId) } returns userTestResultEntities

        val result = userTestResultService.getAllUserTestResultsByUserId(userId)

        result shouldBe userTestResultEntities
    }

    @Test
    fun `getAllUserTestResultsByUserId should throw UserTestResultNotFoundException when no user test results exist`() {
        val userId = 1L
        every { userTestResultRepository.findAllByUserId(userId) } returns null

        val exception = assertThrows<UserTestResultNotFoundException> {
            userTestResultService.getAllUserTestResultsByUserId(userId)
        }

        exception.message shouldBe "No test results found for user with id $userId"
    }

    @Test
    fun `getAllUserTestResultsByTestId should return user test results when they exist`() {
        val testId = 1L
        val userTestResultEntities = listOf(
            UserTestResultEntity(
                id = 1L,
                userId = 1L,
                testId = testId,
                score = 100,
                time = 60.5f
            ),
            UserTestResultEntity(
                id = 2L,
                userId = 2L,
                testId = testId,
                score = 90,
                time = 70.5f
            )
        )
        every { userTestResultRepository.findAllByTestId(testId.toInt()) } returns userTestResultEntities

        val result = userTestResultService.getAllUserTestResultsByTestId(testId.toInt())

        result shouldBe userTestResultEntities
    }

    @Test
    fun `getAllUserTestResultsByTestId should throw UserTestResultNotFoundException when no user test results exist`() {
        val testId = 1L
        every { userTestResultRepository.findAllByTestId(testId.toInt()) } returns null

        val exception = assertThrows<UserTestResultNotFoundException> {
            userTestResultService.getAllUserTestResultsByTestId(testId.toInt())
        }

        exception.message shouldBe "No test results found for test with id $testId"
    }

    @Test
    fun `getUserTestResultByUserIdAndTestId should return user test result when it exists`() {
        val userId = 1L
        val testId = 1L
        val userTestResultEntity = UserTestResultEntity(
            id = 1L,
            userId = userId,
            testId = testId,
            score = 100,
            time = 60.5f
        )
        every { userTestResultRepository.findByUserIdAndTestId(userId, testId.toInt()) } returns userTestResultEntity

        val result = userTestResultService.getUserTestResultByUserIdAndTestId(userId, testId.toInt())

        result shouldBe userTestResultEntity
    }

    @Test
    fun `getUserTestResultByUserIdAndTestId should throw UserTestResultNotFoundException when user test result does not exist`() {
        val userId = 1L
        val testId = 1L
        every { userTestResultRepository.findByUserIdAndTestId(userId, testId.toInt()) } returns null

        val exception = assertThrows<UserTestResultNotFoundException> {
            userTestResultService.getUserTestResultByUserIdAndTestId(userId, testId.toInt())
        }

        exception.message shouldBe "No test result found for user $userId and test $testId"
    }

    @Test
    fun `getAllUserTestResultsByTestIdAndScore should return user test results when they exist`() {
        val testId = 1L
        val score = 100
        val userTestResultEntities = listOf(
            UserTestResultEntity(
                id = 1L,
                userId = 1L,
                testId = testId,
                score = score,
                time = 60.5f
            ),
            UserTestResultEntity(
                id = 2L,
                userId = 2L,
                testId = testId,
                score = score,
                time = 70.5f
            )
        )
        every { userTestResultRepository.findAllByTestIdAndScore(testId.toInt(), score) } returns userTestResultEntities

        val result = userTestResultService.getAllUserTestResultsByTestIdAndScore(testId.toInt(), score)

        result shouldBe userTestResultEntities
    }

    @Test
    fun `getAllUserTestResultsByTestIdAndScore should throw UserTestResultNotFoundException when no user test results exist`() {
        val testId = 1L
        val score = 100
        every { userTestResultRepository.findAllByTestIdAndScore(testId.toInt(), score) } returns null

        val exception = assertThrows<UserTestResultNotFoundException> {
            userTestResultService.getAllUserTestResultsByTestIdAndScore(testId.toInt(), score)
        }

        exception.message shouldBe "No test results found for test $testId with score $score"
    }

    @Test
    fun `deleteUserTestResultById should return true when user test result exists`() {
        val userTestResultId = 1L
        every { userTestResultRepository.existsById(userTestResultId) } returns true
        every { userTestResultRepository.deleteById(userTestResultId) } returns Unit

        val result = userTestResultService.deleteUserTestResultById(userTestResultId)

        result shouldBe true
    }

    @Test
    fun `deleteUserTestResultById should return false when user test result does not exist`() {
        val userTestResultId = 1L
        every { userTestResultRepository.existsById(userTestResultId) } returns false

        val result = userTestResultService.deleteUserTestResultById(userTestResultId)

        result shouldBe false
    }

    @Test
    fun `updateUserTestResultResult should return updated user test result when it exists`() {
        val userTestResultId = 1L
        val newScore = 90
        val newTime = 70.5f
        val userTestResultEntity = UserTestResultEntity(
            id = userTestResultId,
            userId = 1L,
            testId = 1L,
            score = 100,
            time = 60.5f
        )
        every { userTestResultRepository.findById(userTestResultId) } returns Optional.of(userTestResultEntity)
        every { userTestResultRepository.save(any()) } returns userTestResultEntity

        val result = userTestResultService.updateUserTestResultResult(userTestResultId, newScore, newTime)

        result.score shouldBe newScore
        result.time shouldBe newTime
        result.count shouldBe 1
        verify { userTestResultRepository.save(userTestResultEntity) }
    }

    @Test
    fun `updateUserTestResultResult should throw UserTestResultNotFoundException when user test result does not exist`() {
        val userTestResultId = 1L
        val newScore = 90
        val newTime = 70.5f
        every { userTestResultRepository.findById(userTestResultId) } returns Optional.empty()

        val exception = assertThrows<UserTestResultNotFoundException> {
            userTestResultService.updateUserTestResultResult(userTestResultId, newScore, newTime)
        }

        exception.message shouldBe "User test result with id $userTestResultId not found"
    }
}