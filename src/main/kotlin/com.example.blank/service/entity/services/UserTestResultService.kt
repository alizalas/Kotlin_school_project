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

@Service
class UserTestResultService(
    private val userTestResultRepository: UserTestResultRepository
) {

    fun addUserTestResult(userTestResult: UserTestResultDto) {
        userTestResultRepository.save(userTestResult.toEntity())
    }

//    fun getUserTestResultById(userTestResultId: Long): UserTestResultEntity {
//        return userTestResultRepository.findByUserTestResultId(userTestResultId)
//            ?: throw UserTestResultNotFoundException("User test result with id $userTestResultId not found")
//    }

    fun getUserTestResultById(userTestResultId: Long): UserTestResultEntity {
        return userTestResultRepository.findById(userTestResultId)
            .orElseThrow { UserTestResultNotFoundException("User test result with id $userTestResultId not found") }
    }

    fun getAllUserTestResultsByUserId(userId: Long): List<UserTestResultEntity> {
        return userTestResultRepository.findAllByUserId(userId)
            ?: throw UserTestResultNotFoundException("No test results found for user with id $userId")
    }

    fun getAllUserTestResultsByTestId(testId: Int): List<UserTestResultEntity> {
        return userTestResultRepository.findAllByTestId(testId)
            ?: throw UserTestResultNotFoundException("No test results found for test with id $testId")
    }

    fun getUserTestResultByUserIdAndTestId(userId: Long, testId: Int): UserTestResultEntity {
        return userTestResultRepository.findByUserIdAndTestId(userId, testId)
            ?: throw UserTestResultNotFoundException("No test result found for user $userId and test $testId")
    }

    fun getAllUserTestResultsByTestIdAndScore(testId: Int, score: Int): List<UserTestResultEntity> {
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
    fun deleteUserTestResultById(userTestResultId: Long): Boolean {
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

    fun updateUserTestResultResult(userTestResultId: Long, newScore: Int, newTime: Float): UserTestResultEntity {
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