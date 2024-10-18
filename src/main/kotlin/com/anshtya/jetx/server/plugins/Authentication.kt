package com.anshtya.jetx.server.plugins

import com.anshtya.jetx.server.services.JwtService
import com.anshtya.jetx.server.util.AUTH_JWT
import io.ktor.server.application.Application
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.http.content.staticFiles
import io.ktor.server.routing.routing
import java.io.File

fun Application.configureAuthentication(jwtService: JwtService) {
    authentication {
        jwt(AUTH_JWT) {
            realm = jwtService.realm
            verifier(jwtService.jwkProvider, jwtService.issuer) {
                acceptLeeway(3)
            }
        }
    }

    routing {
        staticFiles("/.well-known/jwks.json", File("jwks.json"))
    }
}