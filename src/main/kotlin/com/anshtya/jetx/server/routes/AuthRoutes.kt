package com.anshtya.jetx.server.routes

import com.anshtya.jetx.server.controllers.AuthController
import com.anshtya.jetx.server.model.AuthRequest
import com.anshtya.jetx.server.util.respond
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoute(authController: AuthController) {
    route("/auth") {
        post("/login") {
            val authRequest = call.receive<AuthRequest>()
            val authResult = authController.login(
                email = authRequest.email,
                password = authRequest.password
            )
            respond(authResult)
        }
        post("/signup") {
            val authRequest = call.receive<AuthRequest>()
            val authResult = authController.signUp(
                email = authRequest.email,
                password = authRequest.password
            )
            respond(authResult)
        }
    }
}