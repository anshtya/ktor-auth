package com.anshtya.jetx.server.routes

import com.anshtya.jetx.server.controllers.AuthController
import com.anshtya.jetx.server.model.AuthRequest
import com.anshtya.jetx.server.util.AUTH_JWT
import com.anshtya.jetx.server.util.respond
import io.ktor.http.HttpHeaders
import io.ktor.server.auth.authenticate
import io.ktor.server.request.header
import io.ktor.server.request.receive
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.authRoute(authController: AuthController) {
    route("/auth") {
        post("/login") {
            val authRequest = call.receive<AuthRequest>()
            val authResult = authController.login(
                username = authRequest.username,
                password = authRequest.password
            )
            respond(authResult)
        }
        post("/signup") {
            val authRequest = call.receive<AuthRequest>()
            val authResult = authController.signUp(
                username = authRequest.username,
                password = authRequest.password
            )
            respond(authResult)
        }

        authenticate(AUTH_JWT) {
            get("/refresh") {
                val refreshToken = call.request.header(HttpHeaders.Authorization)!!
                val authResponse = authController.refresh(refreshToken)
                respond(authResponse)
            }
        }
    }
}