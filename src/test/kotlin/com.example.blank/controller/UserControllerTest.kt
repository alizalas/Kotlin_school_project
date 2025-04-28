package com.example.blank.controller

import com.example.blank.dto.UserDto
import com.example.blank.entity.UserEntity
import com.example.blank.service.entity.services.UserNotFoundException
import com.example.blank.service.entity.services.UserService
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

@SpringBootTest
class UserControllerTest {

    @Autowired
    lateinit var userController: UserController

    @Autowired
    lateinit var userService: UserService

    private val testDateTime = LocalDateTime.now()

    private fun createTestUserEntity(
        id: Long = 1L,
        telegramId: Long = 123L,
        username: String = "testUser",
        fullName: String? = "Test User",
        profileData: String? = "Test profile",
        rating: Int = 5,
        streak: Int = 3
    ) = UserEntity(
        id = id,
        telegramId = telegramId,
        username = username,
        fullName = fullName,
        profileData = profileData,
        rating = rating,
        streak = streak,
        createdAt = testDateTime,
        updatedAt = testDateTime
    )

    private fun createTestUserDto(
        telegramId: Long = 123L,
        username: String = "testUser",
        fullName: String? = "Test User",
        profileData: String? = "Test profile",
        rating: Int = 5,
        streak: Int = 3
    ) = UserDto(
        telegramId = telegramId,
        username = username,
        fullName = fullName,
        profileData = profileData,
        rating = rating,
        streak = streak,
        createdAt = testDateTime,
        updatedAt = testDateTime
    )

    @Test
    fun `addUser should call service with correct parameters`() {
        val userDto = createTestUserDto()
        var capturedUsers = mutableListOf<UserDto>()

        every {
            userService.addUser(capture(capturedUsers))
        } returns Unit

        userController.addUser(userDto)

        verify { userService.addUser(any()) }
        capturedUsers shouldHaveSize 1
        with(capturedUsers[0]) {
            telegramId shouldBe userDto.telegramId
            username shouldBe userDto.username
            fullName shouldBe userDto.fullName
            profileData shouldBe userDto.profileData
            rating shouldBe userDto.rating
            streak shouldBe userDto.streak
        }
    }

    @Test
    fun `getUserById should return user when exists`() {
        val userId = 1L
        val userEntity = createTestUserEntity(id = userId)
        every { userService.getUserByUserId(userId) } returns userEntity

        val result = userController.getUserById(userId)

        result.telegramId shouldBe userEntity.telegramId
        result.username shouldBe userEntity.username
    }

    @Test
    fun `getUserById should throw 404 when user not found`() {
        val userId = 1L
        every { userService.getUserByUserId(userId) } throws
                UserNotFoundException("User with userId $userId not found")

        shouldThrow<ResponseStatusException> {
            userController.getUserById(userId)
        }.apply {
            statusCode shouldBe HttpStatus.NOT_FOUND
        }
    }

    @Test
    fun `getUserByTelegramId should return user when exists`() {
        val telegramId = 123L
        val userEntity = createTestUserEntity(telegramId = telegramId)
        every { userService.getUserByTelegramId(telegramId) } returns userEntity

        val result = userController.getUserByTelegramId(telegramId)

        result.telegramId shouldBe telegramId
    }

    @Test
    fun `getUsersByRating should return list of users with specified rating`() {
        val rating = 5
        val userEntities = listOf(
            createTestUserEntity(id = 1L, rating = rating),
            createTestUserEntity(id = 2L, rating = rating)
        )
        every { userService.getAllUsersByRating(rating) } returns userEntities

        val result = userController.getUsersByRating(rating)

        result.size shouldBe 2
        result.all { it.rating == rating } shouldBe true
    }

    @Test
    fun `getUsersByStreak should return list of users with specified streak`() {
        val streak = 3
        val userEntities = listOf(
            createTestUserEntity(id = 1L, streak = streak),
            createTestUserEntity(id = 2L, streak = streak)
        )
        every { userService.getAllUsersByStreak(streak) } returns userEntities

        val result = userController.getUsersByStreak(streak)

        result.size shouldBe 2
        result.all { it.streak == streak } shouldBe true
    }

    @Test
    fun `deleteUserById should return true when user exists`() {
        val userId = 1L
        every { userService.deleteUserByUserId(userId) } returns true

        val result = userController.deleteUserById(userId)

        result shouldBe true
    }

    @Test
    fun `deleteUserById should return false when user not exists`() {
        val userId = 1L
        every { userService.deleteUserByUserId(userId) } returns false

        val result = userController.deleteUserById(userId)

        result shouldBe false
    }

    @Test
    fun `deleteUserByTelegramId should return true when user exists`() {
        val telegramId = 123L
        every { userService.deleteUserByTelegramId(telegramId) } returns true

        val result = userController.deleteUserByTelegramId(telegramId)

        result shouldBe true
    }

    @Test
    fun `updateUsername should return updated user`() {
        val userId = 1L
        val newUsername = "newUsername"
        val updatedUser = createTestUserEntity(id = userId, username = newUsername)

        every { userService.updateUserUsername(userId, newUsername) } returns updatedUser

        val result = userController.updateUsername(userId, mapOf("username" to newUsername))

        result.username shouldBe newUsername
    }

    @Test
    fun `updateFullName should return updated user`() {
        val userId = 1L
        val newFullName = "New Full Name"
        val updatedUser = createTestUserEntity(id = userId, fullName = newFullName)

        every { userService.updateUserFullName(userId, newFullName) } returns updatedUser

        val result = userController.updateFullName(userId, mapOf("fullName" to newFullName))

        result.fullName shouldBe newFullName
    }

    @Test
    fun `updateProfileData should return updated user`() {
        val userId = 1L
        val newProfileData = "New profile data"
        val updatedUser = createTestUserEntity(id = userId, profileData = newProfileData)

        every { userService.updateUserProfileData(userId, newProfileData) } returns updatedUser

        val result = userController.updateProfileData(userId, mapOf("profileData" to newProfileData))

        result.profileData shouldBe newProfileData
    }

    @Test
    fun `updateRating should return updated user`() {
        val userId = 1L
        val newRating = 10
        val updatedUser = createTestUserEntity(id = userId, rating = newRating)

        every { userService.updateUserRating(userId, newRating) } returns updatedUser

        val result = userController.updateRating(userId, mapOf("rating" to newRating))

        result.rating shouldBe newRating
    }

    @Test
    fun `updateStreak should return updated user`() {
        val userId = 1L
        val newStreak = 10
        val updatedUser = createTestUserEntity(id = userId, streak = newStreak)

        every { userService.updateUserStreak(userId, newStreak) } returns updatedUser

        val result = userController.updateStreak(userId, mapOf("streak" to newStreak))

        result.streak shouldBe newStreak
    }

    @Test
    fun `updateUsername should throw exception when username is missing in request`() {
        val userId = 1L

        shouldThrow<IllegalArgumentException> {
            userController.updateUsername(userId, emptyMap())
        }
    }
}