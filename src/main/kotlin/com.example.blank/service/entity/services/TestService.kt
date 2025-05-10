package com.example.blank.service.entity.services

import jakarta.transaction.Transactional
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.ResponseStatus
import com.example.blank.dto.TestDto
import com.example.blank.dto.toEntity
import com.example.blank.repository.TestRepository
import com.example.blank.entity.TestEntity
import com.example.blank.entity.updateTimestamp
import org.slf4j.LoggerFactory


@Service
class TestService(
    private val testRepository: TestRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun addTest(test: TestDto) {
        testRepository.save(test.toEntity())
    }

//    fun getTestByTestId(testId: Long): TestEntity {
//        return testRepository.findByTestId(testId) ?: throw TestNotFoundException("Test with testId $testId not found")
//    }

    fun getTestByTestId(testId: Long): TestEntity {
        logger.info("Запрос на получение теста Id:${testId}")
        return testRepository.findById(testId)
            .orElseThrow { TestNotFoundException("Test with testId $testId not found") }
    }

    fun getAllTestsByContentType(contentType: String): List<TestEntity> {
        logger.info("Запрос на получение тестов по типу Type:${contentType}")
        return testRepository.findAllByContentType(contentType) ?: throw TestNotFoundException("No tests with contentType $contentType found")
    }

    fun getAllTestsByDifficulty(difficulty: String): List<TestEntity> {
        logger.info("Запрос на получение тестов по сложности Diff:${difficulty}")
        return testRepository.findAllByDifficulty(difficulty) ?: throw TestNotFoundException("No tests with difficulty $difficulty found")
    }

//    @Transactional
//    fun deleteTestByTestId(testId: Long): TestEntity {
//        val test = testRepository.findByTestId(testId) ?: throw TestNotFoundException("No test with testId $testId found")
//        testRepository.deleteByTestId(testId)
//        return test
//    }

    @Transactional
    fun deleteTestByTestId(testId: Long): Boolean {
        logger.info("Запрос на удаление теста Id:${testId}")
        return if (testRepository.existsById(testId)) {
            testRepository.deleteById(testId)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteTestByContentType(contentType: String): List<TestEntity> {
//        val tests = testRepository.findAllByContentType(contentType) ?: throw TestNotFoundException("No test with contentType $contentType found")
//
//        if (tests.isEmpty()) {
//            throw TestNotFoundException("No tests with contentType $contentType found")
//        }
//
//        testRepository.deleteAllByContentType(contentType)
//        return tests
//    }

    @Transactional
    fun deleteAllTestByContentType(contentType: String): Boolean {
        logger.info("Запрос на удаление тестов по типу Type:${contentType}")
        return if (testRepository.existsByContentType(contentType)) {
            testRepository.deleteAllByContentType(contentType)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun deleteTestByDifficulty(difficulty: String): List<TestEntity> {
//        val tests = testRepository.findAllByDifficulty(difficulty) ?: throw TestNotFoundException("No tests with difficulty $difficulty found")
//
//        if (tests.isEmpty()) {
//            throw TestNotFoundException("No tests with difficulty $difficulty found")
//        }
//
//        testRepository.deleteAllByDifficulty(difficulty)
//        return tests
//    }

    @Transactional
    fun deleteAllTestByDifficulty(difficulty: String): Boolean {
        logger.info("Запрос на удаление теста по сложности ${difficulty}")
        return if (testRepository.existsByDifficulty(difficulty)) {
            testRepository.deleteAllByDifficulty(difficulty)
            true
        } else {
            false
        }
    }

//    @Transactional
//    fun updateTestDifficulty(testId: Long, newDifficulty: String): TestEntity {
//        val test = testRepository.findByTestId(testId) ?: throw TestNotFoundException("No test with testId $testId found")
//        test.difficulty = newDifficulty
//        test.updateTimestamp()
//        return testRepository.save(test)
//    }

    @Transactional
    fun updateTestDifficulty(testId: Long, newDifficulty: String): TestEntity {
        logger.info("Запрос на обновление сложности теста с id {testId} на {newDifficulty}")
        val test = testRepository.findById(testId)
            .orElseThrow { TestNotFoundException("Test with testId $testId not found") }
        test.difficulty = newDifficulty
        test.updateTimestamp()
        return testRepository.save(test)
    }

//    @Transactional
//    fun updateTestQuestions(testId: Long, newQuestions: String): TestEntity {
//        val test = testRepository.findByTestId(testId) ?: throw TestNotFoundException("Test with testId $testId not found")
//        test.questions = newQuestions
//        test.updateTimestamp()
//        return testRepository.save(test)
//    }

    @Transactional
    fun updateTestQuestions(testId: Long, newQuestions: String): TestEntity {
        logger.info("Запрос на обновление вопросов теста:  Id:${testId}, Questions:${newQuestions}")
        val test = testRepository.findById(testId)
            .orElseThrow { TestNotFoundException("Test with testId $testId not found") }
        test.questions = newQuestions
        test.updateTimestamp()
        return testRepository.save(test)
    }


//    @Transactional
//    fun updateTestAnswers(testId: Long, newAnswers: String): TestEntity {
//        val test = testRepository.findByTestId(testId) ?: throw TestNotFoundException("Test with testId $testId not found")
//        test.answers = newAnswers
//        test.updateTimestamp()
//        return testRepository.save(test)
//    }

    @Transactional
    fun updateTestAnswers(testId: Long, newAnswers: String): TestEntity {
        logger.info("Запрос на обновление ответов теста  Id:${testId}, Answers:${newAnswers}")
        val test = testRepository.findById(testId)
            .orElseThrow { TestNotFoundException("Test with testId $testId not found") }
        test.answers = newAnswers
        test.updateTimestamp()
        return testRepository.save(test)
    }
}

@ResponseStatus(HttpStatus.NOT_FOUND)
class TestNotFoundException(message: String) : RuntimeException(message)