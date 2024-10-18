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
    fun login(email: String, password: String): Result<String> =
        try {
            validateCredentials(email = email, password = password)
            val foundUser = userRepository.getUser(email)

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

            val token = jwtService.generateJwtToken(claim = email)

            Result.Success.toMessage(key = "token", data = token)
        } catch (e: Exception) {
            Result.Error.toResult(e)
        }

    fun signUp(email: String, password: String): Result<String> =
        try {
            validateCredentials(email = email, password = password)

            if (userRepository.getUser(email) != null) {
                throw AlreadyExistsException(
                    message = ErrorMessage.USER_ALREADY_EXISTS
                )
            }

            userRepository.saveUser(
                User(email = email, password = password)
            )

            val token = jwtService.generateJwtToken(claim = email)

            Result.Success.toMessage(key = "token", data = token)
        } catch (e: Exception) {
            Result.Error.toResult(e)
        }

    private fun validateCredentials(email: String, password: String) {
        val message = if (email.isBlank() || password.isBlank()) {
            "Email or password should not be blank"
        } else if (password.length !in (8..50)) {
            "Password should contain minimum 8 and maximum 50 characters"
        } else {
            null
        }

        if (message != null) {
            throw BadRequestException(message)
        }
    }
}