package com.anshtya.jetx.server.plugins

import com.anshtya.jetx.server.controllers.AuthController
import com.anshtya.jetx.server.repository.UserRepository
import com.anshtya.jetx.server.routes.authRoute
import com.anshtya.jetx.server.services.JwtService
import io.ktor.server.application.*
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.*

fun Application.configureRouting(
    jwtService: JwtService,
    userRepository: UserRepository
) {
    routing {
        authRoute(AuthController(jwtService, userRepository))

        get("/") {
            call.respondText("Hello World!")
        }

        get("/user/{username}") {
            val username = call.parameters["username"]
            val user = userRepository.getUser(username!!)

            call.respond(mapOf("user" to user))
        }
    }
}
