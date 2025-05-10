package com.example.blank.service.entity.services

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.UserTestResultDto
import com.example.blank.entity.updateTimestamp
import com.example.blank.dto.toEntity
import com.example.blank.repository.UserTestResultRepository
import com.example.blank.entity.UserTestResultEntity
import org.slf4j.LoggerFactory

@Service
class UserTestResultService(
    private val userTestResultRepository: UserTestResultRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun addUserTestResult(userTestResult: UserTestResultDto) {
        logger.info("Запрос на добаление результата $userTestResult")
        userTestResultRepository.save(userTestResult.toEntity())
    }

//    fun getUserTestResultById(userTestResultId: Long): UserTestResultEntity {
//        return userTestResultRepository.findByUserTestResultId(userTestResultId)
//            ?: throw UserTestResultNotFoundException("User test result with id $userTestResultId not found")
//    }

    fun getUserTestResultById(userTestResultId: Long): UserTestResultEntity {
        logger.info("Запрос на получение результата $userTestResultId по тесту пользователя")
        return userTestResultRepository.findById(userTestResultId)
            .orElseThrow { UserTestResultNotFoundException("User test result with id $userTestResultId not found") }
    }

    fun getAllUserTestResultsByUserId(userId: Long): List<UserTestResultEntity> {
        logger.info("Запрос на получение всех результатов пользователя по Id: $userId")
        return userTestResultRepository.findAllByUserId(userId)
            ?: throw UserTestResultNotFoundException("No test results found for user with id $userId")
    }

    fun getAllUserTestResultsByTestId(testId: Int): List<UserTestResultEntity> {
        logger.info("Запрос на получение результатов всех пользователей по TestId:  $testId")
        return userTestResultRepository.findAllByTestId(testId)
            ?: throw UserTestResultNotFoundException("No test results found for test with id $testId")
    }

    fun getUserTestResultByUserIdAndTestId(userId: Long, testId: Int): UserTestResultEntity {
        logger.info("Запрос на получение результата по тесту $testId пользователя с Id: $userId")
        return userTestResultRepository.findByUserIdAndTestId(userId, testId)
            ?: throw UserTestResultNotFoundException("No test result found for user $userId and test $testId")
    }

    fun getAllUserTestResultsByTestIdAndScore(testId: Int, score: Int): List<UserTestResultEntity> {
        logger.info("Запрос на получение результатов всех пользователей по тесту $testId и само количество очков: $score")
        return userTestResultRepository.findAllByTestIdAndScore(testId, score)
            ?: throw UserTestResultNotFoundException("No test results found for test $testId with score $score")
    }

//    @Transactional
//    fun deleteUserTestResultById(userTestResultId: Long): UserTestResultEntity {
//        val userTestResult = userTestResultRepository.findByUserTestResultId(userTestResultId)
//            ?: throw UserTestResultNotFoundException("User test result with id $userTestResultId not found")
//        userTestResultRepository.deleteByUserTestResultId(userTestResultId)
//        return userTestResult
//    }

    @Transactional
    fun deleteUserTestResultById(userTestResultId: Long, userId: Int): Boolean {
        logger.info("Запрос на удаление результата теста $userTestResultId пользователя $userId")
        return if (userTestResultRepository.existsById(userTestResultId)) {
            userTestResultRepository.deleteById(userTestResultId)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun updateUserTestResultResult(userTestResultId: Long, newScore: Int, newTime: Float): UserTestResultEntity {
//        val userTestResult = userTestResultRepository.findByUserTestResultId(userTestResultId)
//            ?: throw UserTestResultNotFoundException("User test result with id $userTestResultId not found")
//        userTestResult.score = newScore
//        userTestResult.time = newTime
//        userTestResult.count += 1
//        userTestResult.updateTimestamp()
//        return userTestResultRepository.save(userTestResult)
//    }

    fun updateUserTestResultResult(userTestResultId: Long, newScore: Int, newTime: Float    ): UserTestResultEntity {
        logger.info("Запрос на обновление результата пользователя с результатом $newScore и временем $newTime в TestResultId: $userTestResultId")
        val i = getUserTestResultById(userTestResultId);

        val userTestResult = userTestResultRepository.findById(userTestResultId)
            .orElseThrow { UserTestResultNotFoundException("User test result with id $userTestResultId not found") }
        userTestResult.score = newScore
        userTestResult.time = newTime
        userTestResult.count += 1
        userTestResult.updateTimestamp()
        return userTestResultRepository.save(userTestResult)
    }

//    @Transactional
//    fun updateUserTestResultScore(userTestResultId: Long, newScore: Int): UserTestResultEntity {
//        val userTestResult = userTestResultRepository.findByUserTestResultId(userTestResultId)
//            ?: throw UserTestResultNotFoundException("User test result with id $userTestResultId not found")
//        userTestResult.score = newScore
//        userTestResult.updateTimestamp()
//        return userTestResultRepository.save(userTestResult)
//    }
//
//    @Transactional
//    fun updateUserTestResultTime(userTestResultId: Long, newTime: Float): UserTestResultEntity {
//        val userTestResult = userTestResultRepository.findByUserTestResultId(userTestResultId)
//            ?: throw UserTestResultNotFoundException("User test result with id $userTestResultId not found")
//        userTestResult.time = newTime
//        userTestResult.updateTimestamp()
//        return userTestResultRepository.save(userTestResult)
//    }
}
@ResponseStatus(HttpStatus.NOT_FOUND)
class UserTestResultNotFoundException(message: String) : RuntimeException(message)