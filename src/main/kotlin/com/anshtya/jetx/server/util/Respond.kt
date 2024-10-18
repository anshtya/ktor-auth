package com.anshtya.jetx.server.util

import com.anshtya.jetx.server.model.Result
import io.ktor.http.HttpStatusCode
import io.ktor.server.response.respond
import io.ktor.server.routing.RoutingContext

suspend inline fun <reified T> RoutingContext.respond(result: Result<T>) =
    when (result) {
        is Result.Success -> {
            call.respond(
                status = HttpStatusCode.OK,
                message = result.message
            )
        }

        is Result.Error -> {
            call.respond(
                status = result.statusCode,
                message = mapOf("message" to result.errorMessage)
            )
        }
    }