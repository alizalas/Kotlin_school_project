package com.example.blank.service

import com.example.blank.dto.ActivityLogDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.ActivityLogEntity
import com.example.blank.repository.ActivityLogRepository
import com.example.blank.service.entity.services.ActivityLogNotFoundException
import com.example.blank.service.entity.services.ActivityLogService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class ActivityLogServiceTest {

    private val activityLogRepository = mockk<ActivityLogRepository>()
    private val activityLogService = ActivityLogService(activityLogRepository)

    @Test
    fun `addActivityLog should save activity log when data is valid`() {
        val activityLogDto = ActivityLogDto(
            userId = 1L,
            action = "Test Action",
            details = "Test Details",
            createdAt = LocalDateTime.now()
        )
        every { activityLogRepository.save(any()) } returns activityLogDto.toEntity()

        activityLogService.addActivityLog(activityLogDto)

        verify { activityLogRepository.save(activityLogDto.toEntity()) }
    }

    @Test
    fun `getActivityLogById should return activity log when it exists`() {
        val activityLogId = 1L
        val activityLogEntity = ActivityLogEntity(
            id = activityLogId,
            userId = 1L,
            action = "Test Action",
            details = "Test Details"
        )
        every { activityLogRepository.findById(activityLogId) } returns Optional.of(activityLogEntity)

        val result = activityLogService.getActivityLogById(activityLogId)

        result shouldBe activityLogEntity
    }

    @Test
    fun `getActivityLogById should throw ActivityLogNotFoundException when activity log does not exist`() {
        val activityLogId = 1L
        every { activityLogRepository.findById(activityLogId) } returns Optional.empty()

        val exception = assertThrows<ActivityLogNotFoundException> {
            activityLogService.getActivityLogById(activityLogId)
        }

        exception.message shouldBe "Activity log with id $activityLogId not found"
    }

    @Test
    fun `getAllActivityLogsByUserId should return activity logs when they exist`() {
        val userId = 1L
        val activityLogEntities = listOf(
            ActivityLogEntity(
                id = 1L,
                userId = userId,
                action = "Test Action 1",
                details = "Test Details 1"
            ),
            ActivityLogEntity(
                id = 2L,
                userId = userId,
                action = "Test Action 2",
                details = "Test Details 2"
            )
        )
        every { activityLogRepository.findAllByUserId(userId) } returns activityLogEntities

        val result = activityLogService.getAllActivityLogsByUserId(userId)

        result shouldBe activityLogEntities
    }

    @Test
    fun `getAllActivityLogsByUserId should throw ActivityLogNotFoundException when no activity logs exist`() {
        val userId = 1L
        every { activityLogRepository.findAllByUserId(userId) } returns null

        val exception = assertThrows<ActivityLogNotFoundException> {
            activityLogService.getAllActivityLogsByUserId(userId)
        }

        exception.message shouldBe "Activity logs for user with id $userId not found"
    }

    @Test
    fun `getAllActivityLogsByAction should return activity logs when they exist`() {
        val action = "Test Action"
        val activityLogEntities = listOf(
            ActivityLogEntity(
                id = 1L,
                userId = 1L,
                action = action,
                details = "Test Details 1"
            ),
            ActivityLogEntity(
                id = 2L,
                userId = 2L,
                action = action,
                details = "Test Details 2"
            )
        )
        every { activityLogRepository.findAllByAction(action) } returns activityLogEntities

        val result = activityLogService.getAllActivityLogsByAction(action)

        result shouldBe activityLogEntities
    }

    @Test
    fun `getAllActivityLogsByAction should throw ActivityLogNotFoundException when no activity logs exist`() {
        val action = "Test Action"
        every { activityLogRepository.findAllByAction(action) } returns null

        val exception = assertThrows<ActivityLogNotFoundException> {
            activityLogService.getAllActivityLogsByAction(action)
        }

        exception.message shouldBe "Activity logs with action $action not found"
    }

    @Test
    fun `deleteActivityLogById should return true when activity log exists`() {
        val activityLogId = 1L
        every { activityLogRepository.existsById(activityLogId) } returns true
        every { activityLogRepository.deleteById(activityLogId) } returns Unit

        val result = activityLogService.deleteActivityLogById(activityLogId)

        result shouldBe true
    }

    @Test
    fun `deleteActivityLogById should return false when activity log does not exist`() {
        val activityLogId = 1L
        every { activityLogRepository.existsById(activityLogId) } returns false

        val result = activityLogService.deleteActivityLogById(activityLogId)

        result shouldBe false
    }

    @Test
    fun `deleteAllActivityLogsByUserId should return true when activity logs exist`() {
        val userId = 1L
        every { activityLogRepository.existsByUserId(userId) } returns true
        every { activityLogRepository.deleteAllByUserId(userId) } returns Unit

        val result = activityLogService.deleteAllActivityLogsByUserId(userId)

        result shouldBe true
    }

    @Test
    fun `deleteAllActivityLogsByUserId should return false when no activity logs exist`() {
        val userId = 1L
        every { activityLogRepository.existsByUserId(userId) } returns false

        val result = activityLogService.deleteAllActivityLogsByUserId(userId)

        result shouldBe false
    }

    @Test
    fun `deleteAllActivityLogsByAction should return true when activity logs exist`() {
        val action = "Test Action"
        every { activityLogRepository.existsByAction(action) } returns true
        every { activityLogRepository.deleteAllByAction(action) } returns Unit

        val result = activityLogService.deleteAllActivityLogsByAction(action)

        result shouldBe true
    }

    @Test
    fun `deleteAllActivityLogsByAction should return false when no activity logs exist`() {
        val action = "Test Action"
        every { activityLogRepository.existsByAction(action) } returns false

        val result = activityLogService.deleteAllActivityLogsByAction(action)

        result shouldBe false
    }

    @Test
    fun `deleteAllActivityLogsOlderThan should return true when activity logs exist`() {
        val date = LocalDateTime.now()
        every { activityLogRepository.existsByCreatedAtBefore(date) } returns true
        every { activityLogRepository.deleteAllByCreatedAtBefore(date) } returns Unit

        val result = activityLogService.deleteAllActivityLogsOlderThan(date)

        result shouldBe true
    }

    @Test
    fun `deleteAllActivityLogsOlderThan should return false when no activity logs exist`() {
        val date = LocalDateTime.now()
        every { activityLogRepository.existsByCreatedAtBefore(date) } returns false

        val result = activityLogService.deleteAllActivityLogsOlderThan(date)

        result shouldBe false
    }
}