package com.anshtya.jetx.server.controllers

import com.anshtya.jetx.server.model.Result
import com.anshtya.jetx.server.model.User
import com.anshtya.jetx.server.repository.UserRepository
import com.anshtya.jetx.server.services.JwtService
import com.anshtya.jetx.server.util.AlreadyExistsException
import com.anshtya.jetx.server.util.BadRequestException
import com.anshtya.jetx.server.util.ErrorMessage
import com.anshtya.jetx.server.util.NotFoundException
import com.anshtya.jetx.server.util.UnauthorizedException

class AuthController(
    private val jwtService: JwtService,
    private val userRepository: UserRepository
) {
    fun login(username: String, password: String): Result<String> =
        try {
            validateCredentials(username = username, password = password)
            val foundUser = userRepository.getUser(username)

            if (foundUser == null) {
                throw NotFoundException(
                    message = ErrorMessage.USER_NOT_FOUND
                )
            }

            if (foundUser.password != password) {
                throw UnauthorizedException(
                    message = ErrorMessage.INCORRECT_CREDENTIALS
                )
            }

            val token = jwtService.generateJwtToken(claim = username)

            Result.Success.toMessage(key = "token", data = token)
        } catch (e: Exception) {
            Result.Error.toResult(e)
        }

    fun signUp(username: String, password: String): Result<String> =
        try {
            validateCredentials(username = username, password = password)

            if (userRepository.getUser(username) != null) {
                throw AlreadyExistsException(
                    message = ErrorMessage.USER_ALREADY_EXISTS
                )
            }

            userRepository.saveUser(
                User(username = username, password = password)
            )

            val token = jwtService.generateJwtToken(claim = username)

            Result.Success.toMessage(key = "token", data = token)
        } catch (e: Exception) {
            Result.Error.toResult(e)
        }

    private fun validateCredentials(username: String, password: String) {
        val message = if (username.isBlank() || password.isBlank()) {
            "Username or password should not be blank."
        } else if (password.length !in (8..50)) {
            "Password should contain minimum 8 and maximum 50 characters."
        } else if (username.length !in (8..50)) {
            "Username should contain minimum 8 and maximum 50 characters."
        } else {
            null
        }

        if (message != null) {
            throw BadRequestException(message)
        }
    }
}