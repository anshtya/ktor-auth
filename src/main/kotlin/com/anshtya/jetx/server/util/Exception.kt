package com.anshtya.jetx.server.util

import io.ktor.http.HttpStatusCode


class BadRequestException(message: String) : Exception(message)

class UnauthorizedException(message: String) : Exception(message)

class NotFoundException(message: String) : Exception(message)

class AlreadyExistsException(message: String) : Exception(message)

fun Exception.statusCode(): HttpStatusCode =
    when (this) {
        is BadRequestException -> HttpStatusCode.BadRequest
        is NotFoundException -> HttpStatusCode.NotFound
        is AlreadyExistsException -> HttpStatusCode.Conflict
        is UnauthorizedException -> HttpStatusCode.Unauthorized
        else -> HttpStatusCode.InternalServerError
    }

fun HttpStatusCode.errorMessage(e: Exception): String? =
    when (this) {
        HttpStatusCode.InternalServerError -> ErrorMessage.INTERNAL_ERROR
        else -> e.message
    }