package com.anshtya.jetx.server.model

import com.anshtya.jetx.server.util.errorMessage
import com.anshtya.jetx.server.util.statusCode
import io.ktor.http.HttpStatusCode

sealed interface Result<out T> {
    data class Success<T>(val message: Map<String, T>) : Result<T> {
        companion object {
            fun <T> toMessage(key: String, data: T) = Success(mapOf(key to data))
        }
    }

    data class Error(
        val statusCode: HttpStatusCode,
        val errorMessage: String?
    ) : Result<Nothing> {
        companion object {
            fun toResult(e: Exception): Error {
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
