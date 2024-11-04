package com.anshtya.jetx.server.plugins

import com.anshtya.jetx.server.model.ErrorResponse
import com.anshtya.jetx.server.repository.UserRepository
import com.anshtya.jetx.server.services.JwtService
import com.anshtya.jetx.server.util.AUTH_JWT
import com.anshtya.jetx.server.util.ErrorMessage
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.response.respond

fun Application.configureAuthentication(
    jwtService: JwtService,
    userRepository: UserRepository
) {
    authentication {
        jwt(AUTH_JWT) {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)
            validate { credential ->
                jwtService.validate(
                    credential = credential,
                    searchUser = userRepository::getUser
                )
            }
            challenge { defaultScheme, realm ->
                call.respond(
                    status = HttpStatusCode.Unauthorized,
                    message = ErrorResponse(ErrorMessage.INVALID_EXPIRED_TOKEN)
                )
            }
        }
    }
}