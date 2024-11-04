package com.anshtya.jetx.server.model

import com.anshtya.jetx.server.util.errorMessage
import com.anshtya.jetx.server.util.statusCode
import io.ktor.http.HttpStatusCode

sealed interface Result<out T> {
    data class Success<T>(val message: T) : Result<T> {
        companion object {
            fun <T> from(data: T) = Success(data)
        }
    }

    data class Error(
        val statusCode: HttpStatusCode,
        val errorMessage: String?
    ) : Result<Nothing> {
        companion object {
            fun from(e: Exception): Error {
                val statusCode = e.statusCode()
                val errorMessage = statusCode.errorMessage(e)
                return Error(
                    statusCode = statusCode,
                    errorMessage = errorMessage
                )
            }
        }
    }
}
