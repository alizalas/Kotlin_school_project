package com.example.blank.service

import com.example.blank.dto.UserDto
import com.example.blank.dto.toEntity
import com.example.blank.entity.UserEntity
import com.example.blank.repository.UserRepository
import com.example.blank.service.entity.services.UserNotFoundException
import com.example.blank.service.entity.services.UserService
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDateTime
import java.util.*

class UserServiceTest {

    private val userRepository = mockk<UserRepository>()
    private val userService = UserService(userRepository)

    @Test
    fun `addUser should save user when user data is valid`() {
        val userDto = UserDto(
            telegramId = 123L,
            username = "testUser",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        every { userRepository.save(any()) } returns userDto.toEntity()

        userService.addUser(userDto)

        verify { userRepository.save(userDto.toEntity()) }
    }

    @Test
    fun `getUserByUserId should return user when user exists`() {
        val userId = 1L
        val userEntity = UserEntity(
            id = userId,
            telegramId = 123L,
            username = "testUser",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findById(userId) } returns Optional.of(userEntity)

        val result = userService.getUserByUserId(userId)

        result shouldBe userEntity
    }

    @Test
    fun `getUserByUserId should throw UserNotFoundException when user does not exist`() {
        val userId = 1L
        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.getUserByUserId(userId)
        }

        exception.message shouldBe "User with userId $userId not found"
    }

    @Test
    fun `getUserByTelegramId should return user when user exists`() {
        val telegramId = 123L
        val userEntity = UserEntity(
            id = 1L,
            telegramId = telegramId,
            username = "testUser",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findByTelegramId(telegramId) } returns userEntity

        val result = userService.getUserByTelegramId(telegramId)

        result shouldBe userEntity
    }

    @Test
    fun `getUserByTelegramId should throw UserNotFoundException when user does not exist`() {
        val telegramId = 123L
        every { userRepository.findByTelegramId(telegramId) } returns null

        val exception = assertThrows<UserNotFoundException> {
            userService.getUserByTelegramId(telegramId)
        }

        exception.message shouldBe "User with telegramId $telegramId not found"
    }

    @Test
    fun `getAllUsersByRating should return users when users exist`() {
        val rating = 5
        val userEntities = listOf(
            UserEntity(
                id = 1L,
                telegramId = 123L,
                username = "testUser1",
                fullName = "Test User1",
                profileData = "{}",
                rating = rating,
                streak = 0
            ),
            UserEntity(
                id = 2L,
                telegramId = 456L,
                username = "testUser2",
                fullName = "Test User2",
                profileData = "{}",
                rating = rating,
                streak = 0
            )
        )
        every { userRepository.findAllByRating(rating) } returns userEntities

        val result = userService.getAllUsersByRating(rating)

        result shouldBe userEntities
    }

    @Test
    fun `getAllUsersByRating should throw UserNotFoundException when no users exist`() {
        val rating = 5
        every { userRepository.findAllByRating(rating) } returns null

        val exception = assertThrows<UserNotFoundException> {
            userService.getAllUsersByRating(rating)
        }

        exception.message shouldBe "No users with rating $rating found"
    }

    @Test
    fun `getAllUsersByStreak should return users when users exist`() {
        val streak = 3
        val userEntities = listOf(
            UserEntity(
                id = 1L,
                telegramId = 123L,
                username = "testUser1",
                fullName = "Test User1",
                profileData = "{}",
                rating = 0,
                streak = streak
            ),
            UserEntity(
                id = 2L,
                telegramId = 456L,
                username = "testUser2",
                fullName = "Test User2",
                profileData = "{}",
                rating = 0,
                streak = streak
            )
        )
        every { userRepository.findAllByStreak(streak) } returns userEntities

        val result = userService.getAllUsersByStreak(streak)

        result shouldBe userEntities
    }

    @Test
    fun `getAllUsersByStreak should throw UserNotFoundException when no users exist`() {
        val streak = 3
        every { userRepository.findAllByStreak(streak) } returns null

        val exception = assertThrows<UserNotFoundException> {
            userService.getAllUsersByStreak(streak)
        }

        exception.message shouldBe "No users with streak $streak found"
    }

    @Test
    fun `deleteUserByUserId should return true when user exists`() {
        val userId = 1L
        every { userRepository.existsById(userId) } returns true
        every { userRepository.deleteById(userId) } returns Unit

        val result = userService.deleteUserByUserId(userId)

        result shouldBe true
    }

    @Test
    fun `deleteUserByUserId should return false when user does not exist`() {
        val userId = 1L
        every { userRepository.existsById(userId) } returns false

        val result = userService.deleteUserByUserId(userId)

        result shouldBe false
    }

    @Test
    fun `deleteUserByTelegramId should return true when user exists`() {
        val telegramId = 123L
        every { userRepository.existsByTelegramId(telegramId) } returns true
        every { userRepository.deleteByTelegramId(telegramId) } returns Unit

        val result = userService.deleteUserByTelegramId(telegramId)

        result shouldBe true
    }

    @Test
    fun `deleteUserByTelegramId should return false when user does not exist`() {
        val telegramId = 123L
        every { userRepository.existsByTelegramId(telegramId) } returns false

        val result = userService.deleteUserByTelegramId(telegramId)

        result shouldBe false
    }

    @Test
    fun `updateUserUsername should return updated user when user exists`() {
        val userId = 1L
        val newUsername = "newUsername"
        val userEntity = UserEntity(
            id = userId,
            telegramId = 123L,
            username = "oldUsername",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findById(userId) } returns Optional.of(userEntity)
        every { userRepository.save(any()) } returns userEntity

        val result = userService.updateUserUsername(userId, newUsername)

        result.username shouldBe newUsername
        verify { userRepository.save(userEntity) }
    }

    @Test
    fun `updateUserUsername should throw UserNotFoundException when user does not exist`() {
        val userId = 1L
        val newUsername = "newUsername"
        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.updateUserUsername(userId, newUsername)
        }

        exception.message shouldBe "User with userId $userId not found"
    }

    @Test
    fun `updateUserFullName should return updated user when user exists`() {
        val userId = 1L
        val newFullName = "New Full Name"
        val userEntity = UserEntity(
            id = userId,
            telegramId = 123L,
            username = "testUser",
            fullName = "Old Full Name",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findById(userId) } returns Optional.of(userEntity)
        every { userRepository.save(any()) } returns userEntity

        val result = userService.updateUserFullName(userId, newFullName)

        result.fullName shouldBe newFullName
        verify { userRepository.save(userEntity) }
    }

    @Test
    fun `updateUserFullName should throw UserNotFoundException when user does not exist`() {
        val userId = 1L
        val newFullName = "New Full Name"
        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.updateUserFullName(userId, newFullName)
        }

        exception.message shouldBe "User with userId $userId not found"
    }

    @Test
    fun `updateUserProfileData should return updated user when user exists`() {
        val userId = 1L
        val newProfileData = "{\"key\": \"value\"}"
        val userEntity = UserEntity(
            id = userId,
            telegramId = 123L,
            username = "testUser",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findById(userId) } returns Optional.of(userEntity)
        every { userRepository.save(any()) } returns userEntity

        val result = userService.updateUserProfileData(userId, newProfileData)

        result.profileData shouldBe newProfileData
        verify { userRepository.save(userEntity) }
    }

    @Test
    fun `updateUserProfileData should throw UserNotFoundException when user does not exist`() {
        val userId = 1L
        val newProfileData = "{\"key\": \"value\"}"
        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.updateUserProfileData(userId, newProfileData)
        }

        exception.message shouldBe "User with userId $userId not found"
    }

    @Test
    fun `updateUserRating should return updated user when user exists`() {
        val userId = 1L
        val newRating = 5
        val userEntity = UserEntity(
            id = userId,
            telegramId = 123L,
            username = "testUser",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findById(userId) } returns Optional.of(userEntity)
        every { userRepository.save(any()) } returns userEntity

        val result = userService.updateUserRating(userId, newRating)

        result.rating shouldBe newRating
        verify { userRepository.save(userEntity) }
    }

    @Test
    fun `updateUserRating should throw UserNotFoundException when user does not exist`() {
        val userId = 1L
        val newRating = 5
        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.updateUserRating(userId, newRating)
        }

        exception.message shouldBe "User with userId $userId not found"
    }

    @Test
    fun `updateUserStreak should return updated user when user exists`() {
        val userId = 1L
        val newStreak = 10
        val userEntity = UserEntity(
            id = userId,
            telegramId = 123L,
            username = "testUser",
            fullName = "Test User",
            profileData = "{}",
            rating = 0,
            streak = 0
        )
        every { userRepository.findById(userId) } returns Optional.of(userEntity)
        every { userRepository.save(any()) } returns userEntity

        val result = userService.updateUserStreak(userId, newStreak)

        result.streak shouldBe newStreak
        verify { userRepository.save(userEntity) }
    }

    @Test
    fun `updateUserStreak should throw UserNotFoundException when user does not exist`() {
        val userId = 1L
        val newStreak = 10
        every { userRepository.findById(userId) } returns Optional.empty()

        val exception = assertThrows<UserNotFoundException> {
            userService.updateUserStreak(userId, newStreak)
        }

        exception.message shouldBe "User with userId $userId not found"
    }
}