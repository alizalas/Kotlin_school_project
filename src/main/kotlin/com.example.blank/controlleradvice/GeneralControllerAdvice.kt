package com.example.blank.controlleradvice

import com.example.blank.exception.BadRequestException
import com.example.blank.exception.UnAuthorisedException
import io.swagger.v3.oas.annotations.Hidden
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import com.example.blank.responses.ErrorResponse
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import com.example.blank.exception.UserIsAlreadyExistsException
import com.example.blank.responses.StatusResponse
import com.example.blank.exception.UserNotFoundException

@Hidden
@RestControllerAdvice
class GeneralControllerAdvice {
    private val logger = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(UnAuthorisedException::class)
    fun unauthorisedExceptionHandler(exception: UnAuthorisedException) : ResponseEntity<ErrorResponse> {
        logger.warn("Failed to authorize user", exception)
        return ResponseEntity.status(401).body(ErrorResponse(exception.message ?: "UNAUTHRISED"))
    }

    @ExceptionHandler(UserIsAlreadyExistsException::class)
    fun handleUnauthorized(exception: UserIsAlreadyExistsException): ResponseEntity<StatusResponse> {
        logger.warn("User is unauthorized")
        return ResponseEntity.status(401).body(StatusResponse(message = exception.message ?: "Unauthorized"))
    }

    @ExceptionHandler(UserNotFoundException::class)
    fun handleUserNotFound(exception: UserNotFoundException): ResponseEntity<StatusResponse> =
        ResponseEntity.status(404).body(StatusResponse(message = exception.message ?: "User not found"))

    @ExceptionHandler(BadRequestException::class)
    fun badRequestExceptionHandler(exception: BadRequestException): ResponseEntity<ErrorResponse> {
        logger.warn(exception.message ?: "BAD REQUEST")
        return ResponseEntity.status(400).body(ErrorResponse(exception.message ?: "BAD REQUEST"))
    }
}