package com.example.blank.service.entity.services

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.ActivityLogDto
import com.example.blank.dto.toEntity
import com.example.blank.repository.ActivityLogRepository
import com.example.blank.entity.ActivityLogEntity
import java.time.LocalDateTime
import org.slf4j.LoggerFactory

@Service
class ActivityLogService(
    private val activityLogRepository: ActivityLogRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun addActivityLog(activityLog: ActivityLogDto) {
        logger.info("Запрос на добавление журнала действий по Id:${activityLog}")
        activityLogRepository.save(activityLog.toEntity())


    }

//    fun getActivityLogById(activityLogId: Long): ActivityLogEntity {
//        return activityLogRepository.findByActivityLogId(activityLogId) ?: throw ActivityLogNotFoundException("Activity log with id $activityLogId not found")
//    }

    fun getActivityLogById(activityLogId: Long): ActivityLogEntity {
        logger.info("Запрос на получение журнала действий по Id:${activityLogId}")
        return activityLogRepository.findById(activityLogId)
            .orElseThrow { ActivityLogNotFoundException("Activity log with id $activityLogId not found") }
    }

    fun getAllActivityLogsByUserId(userId: Long): List<ActivityLogEntity> {
        logger.info("Запрос на получение журнала действий по UserId${userId}")
        return activityLogRepository.findAllByUserId(userId) ?: throw ActivityLogNotFoundException("Activity logs for user with id $userId not found")
    }

    fun getAllActivityLogsByAction(action: String): List<ActivityLogEntity> {
        logger.info("Запрос на получение всех логов по действию ${action}")
        return activityLogRepository.findAllByAction(action) ?: throw ActivityLogNotFoundException("Activity logs with action $action not found")
    }

//    @Transactional
//    fun deleteActivityLogById(activityLogId: Long): ActivityLogEntity {
//        val activityLog = activityLogRepository.findByActivityLogId(activityLogId) ?: throw ActivityLogNotFoundException("Activity log with id $activityLogId not found")
//        activityLogRepository.deleteByActivityLogId(activityLogId)
//        return activityLog
//    }

    @Transactional
    fun deleteActivityLogById(activityLogId: Long): Boolean {
        logger.info("Запрос на удаление всех логов по Id: ${activityLogId}")
        return if (activityLogRepository.existsById(activityLogId)) {
            activityLogRepository.deleteById(activityLogId)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteAllActivityLogsByUserId(userId: Long): ActivityLogEntity {
//        val activityLog = activityLogRepository.findAllByUserId(userId) ?: throw ActivityLogNotFoundException("Activity logs for user with id $userId not found")
//        activityLogRepository.deleteAllByUserId(userId)
//        return activityLog
//    }

    @Transactional
    fun deleteAllActivityLogsByUserId(userId: Long): Boolean {
        logger.info("Запрос на удаление всех логов по UserId: ${userId}")
        return if (activityLogRepository.existsByUserId(userId)) {
            activityLogRepository.deleteAllByUserId(userId)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteAllActivityLogsByAction(action: String): List<ActivityLogEntity> {
//        val activityLogs = activityLogRepository.deleteAllByAction(action) ?: throw ActivityLogNotFoundException("Activity logs with action $action not found")
//        return activityLogs
//    }

    @Transactional
    fun deleteAllActivityLogsByAction(action: String): Boolean {
        logger.info("Запрос на удаление всех логов по действию ${action}")
        return if (activityLogRepository.existsByAction(action)) {
            activityLogRepository.deleteAllByAction(action)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteAllActivityLogsOlderThan(date: LocalDateTime): List<ActivityLogEntity> {
//        val activityLogs = activityLogRepository.deleteAllByCreatedAtBefore(date) ?: throw ActivityLogNotFoundException("Activity logs older than date $date not found")
//        return activityLogs
//    }

    @Transactional
    fun deleteAllActivityLogsOlderThan(date: LocalDateTime): Boolean {
        logger.info("Запрос на удаление всех логов старше чем ${date}")
        return if (activityLogRepository.existsByCreatedAtBefore(date)) {
            activityLogRepository.deleteAllByCreatedAtBefore(date)
            true
        } else {
            false
        }
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class ActivityLogNotFoundException(message: String) : RuntimeException(message)