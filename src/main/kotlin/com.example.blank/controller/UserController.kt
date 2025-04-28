package com.example.blank.controller

import com.example.blank.service.entity.services.UserService
import com.example.blank.dto.UserDto
import com.example.blank.entity.toDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*


@RestController
@RequestMapping("/users")
class UserController(
    val userService: UserService
) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addUser(
        @RequestBody user: UserDto
    ) {
        userService.addUser(user)
    }

    @GetMapping("/{id}")
    fun getUserById(
        @PathVariable id: Long
    ): UserDto {
        return userService.getUserByUserId(id).toDto()
    }

    @GetMapping("/telegram/{telegramId}")
    fun getUserByTelegramId(
        @PathVariable telegramId: Long
    ): UserDto {
        return userService.getUserByTelegramId(telegramId).toDto()
    }

    @GetMapping("/rating/{rating}")
    fun getUsersByRating(
        @PathVariable rating: Int
    ): List<UserDto> {
        return userService.getAllUsersByRating(rating).map { it.toDto() }
    }

    @GetMapping("/streak/{streak}")
    fun getUsersByStreak(
        @PathVariable streak: Int
    ): List<UserDto> {
        return userService.getAllUsersByStreak(streak).map { it.toDto() }
    }

    @DeleteMapping("/{id}")
    fun deleteUserById(
        @PathVariable id: Long
    ): Boolean {
        return userService.deleteUserByUserId(id)
    }

    @DeleteMapping("/telegram/{telegramId}")
    fun deleteUserByTelegramId(
        @PathVariable telegramId: Long
    ): Boolean {
        return userService.deleteUserByTelegramId(telegramId)
    }

    @PatchMapping("/{id}/username")
    fun updateUsername(
        @PathVariable id: Long,
        @RequestBody newUsername: Map<String, String>
    ): UserDto {
        return userService.updateUserUsername(id, newUsername["username"]!!).toDto()
    }

    @PatchMapping("/{id}/fullname")
    fun updateFullName(
        @PathVariable id: Long,
        @RequestBody newFullName: Map<String, String>
    ): UserDto {
        return userService.updateUserFullName(id, newFullName["fullName"]!!).toDto()
    }

    @PatchMapping("/{id}/profiledata")
    fun updateProfileData(
        @PathVariable id: Long,
        @RequestBody newProfileData: Map<String, String>
    ): UserDto {
        return userService.updateUserProfileData(id, newProfileData["profileData"]!!).toDto()
    }

    @PatchMapping("/{id}/rating")
    fun updateRating(
        @PathVariable id: Long,
        @RequestBody newRating: Map<String, Int>
    ): UserDto {
        return userService.updateUserRating(id, newRating["rating"]!!).toDto()
    }

    @PatchMapping("/{id}/streak")
    fun updateStreak(
        @PathVariable id: Long,
        @RequestBody newStreak: Map<String, Int>
    ): UserDto {
        return userService.updateUserStreak(id, newStreak["streak"]!!).toDto()
    }
}