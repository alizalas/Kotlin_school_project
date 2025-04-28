package com.example.blank.repository

import com.example.blank.entity.UserTestResultEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
//import eu.vendeli.tgbot.types.User

@Repository
interface UserTestResultRepository : JpaRepository<UserTestResultEntity, Long> {
    //fun findByUserTestResultId(userTestResultId: Long): UserTestResultEntity?
    fun findAllByUserId(userId: Long): List<UserTestResultEntity>?
    fun findAllByTestId(testId: Int): List<UserTestResultEntity>?
    fun findByUserIdAndTestId(userId: Long, testId: Int): UserTestResultEntity?
    fun findAllByTestIdAndScore(testId: Int, score: Int): List<UserTestResultEntity>?
    //fun deleteByUserTestResultId(userTestResultId: Long): UserTestResultEntity?
}
