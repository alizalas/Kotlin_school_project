package com.example.blank.repository

import com.example.blank.entity.TestEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface TestRepository : JpaRepository<TestEntity, Long> {
    //fun findByTestId(testId: Long): TestEntity?
    fun findAllByContentType(contentType: String): List<TestEntity>?
    fun findAllByDifficulty(difficulty: String): List<TestEntity>?
    //fun deleteByTestId(testId: Long): TestEntity?
    fun deleteAllByContentType(contentType: String)
    fun deleteAllByDifficulty(difficulty: String)
    fun existsByContentType(contentType: String): Boolean
    fun existsByDifficulty(difficulty: String): Boolean
}
