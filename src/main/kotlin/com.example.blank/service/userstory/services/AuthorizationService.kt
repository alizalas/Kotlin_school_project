package com.example.blank.service.userstory.services

import jakarta.transaction.Transactional
import org.springframework.stereotype.Component
import org.slf4j.LoggerFactory
import com.example.blank.requests.RegisterUserRequest
import com.example.blank.requests.LoginUserRequest
import com.example.blank.entity.TokenEntity
import com.example.blank.entity.UserAuthorizationEntity
import com.example.blank.exception.BadRequestException
import com.example.blank.exception.UnAuthorisedException
import com.example.blank.repository.TokenRepository
import com.example.blank.repository.UserAuthorizationRepository
import java.util.*

@Component
class AuthorizationService(
    private val userAuthorizationRepository: UserAuthorizationRepository,
    private val tokenRepository: TokenRepository
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @Transactional
    fun registerUser(request: RegisterUserRequest): String {
        logger.info("Запрос $request на регистрацию пользователя")
        val user = userAuthorizationRepository.findByUsernameAndPassword(request.username, request.password)
        if (user != null) throw BadRequestException("Пользователь уже зарегистрирован")
        userAuthorizationRepository.save(
            UserAuthorizationEntity(
                username = request.username,
                password = request.password,
                telegramId = request.telegramId,
                fullName = request.fullName,
                profileData = request.profileData
            )
        )
        return "Пользователь успешно зарегистрирован"
    }

    @Transactional
    fun loginUser(request: LoginUserRequest): String {
        logger.info("Запрос $request на вход пользователя в систему")
        val username = userAuthorizationRepository.findByUsername(request.username)
        val user = userAuthorizationRepository.findByUsernameAndPassword(request.username, request.password)
        if (username == null) {
            throw BadRequestException("Такого пользователя не существует")
        }
        if (user == null) {
            throw UnAuthorisedException("Неверный логин или пароль")
        }

        val token = tokenRepository.findByUser(user)!!.firstOrNull()
        if (token == null) {
            return tokenRepository.save(
                TokenEntity(
                    value = UUID.randomUUID().toString(),
                    user = user
                )
            ).value
        }
        return  token.value
    }
}