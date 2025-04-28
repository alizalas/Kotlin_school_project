package com.example.blank.controller

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import com.example.blank.dto.responses.LoginResponse
import com.example.blank.dto.requests.RegisterUserRequest
import com.example.blank.dto.requests.LoginUserRequest
import com.example.blank.dto.responses.ServiceResponse
import com.example.blank.service.userstory.services.AuthorizationService

@RestController
@RequestMapping("authorization")
class AuthorizationController(
    private val authorizationService: AuthorizationService
) {
    @PostMapping("/register")
    fun registerUser(@RequestBody request: RegisterUserRequest) =
        ServiceResponse(authorizationService.registerUser(request))

    @PostMapping("/login")
    fun loginUser(@RequestBody request: LoginUserRequest) = LoginResponse(authorizationService.loginUser(request))
}