package com.anshtya.jetx.server.services

import com.anshtya.jetx.server.model.AuthResponse
import com.anshtya.jetx.server.model.User
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import io.ktor.server.auth.jwt.JWTCredential
import io.ktor.server.auth.jwt.JWTPrincipal
import java.util.Date

class JwtService(private val application: Application) {
    val secret = getConfigProperty("jwt.secret")
    val issuer = getConfigProperty("jwt.issuer")
    val realm = getConfigProperty("jwt.realm")
    private val verification = Algorithm.HMAC256(secret)

    val jwtVerifier = JWT
        .require(verification)
        .withIssuer(issuer)
        .build()

    fun authenticate(username: String): AuthResponse {
        val accessToken = generateAccessToken(username)
        val refreshToken = generateRefreshToken(username)

        return AuthResponse(accessToken = accessToken, refreshToken = refreshToken)
    }

    fun validate(
        credential: JWTCredential,
        searchUser: (String) -> User?
    ): JWTPrincipal? {
        val username = credential.payload.getClaim("username").asString()
        val foundUser = searchUser(username)

        return foundUser?.let { JWTPrincipal(credential.payload) }
    }

    private fun generateAccessToken(username: String) =
        generateJwtToken(username, 15 * 60 * 1_000L) // 15 min

    private fun generateRefreshToken(username: String) =
        generateJwtToken(username, 90 * 24 * 60 * 60 * 1_000L) // 90 days

    private fun generateJwtToken(
        username: String,
        duration: Long
    ): String =
        JWT.create()
            .withIssuer(issuer)
            .withClaim("username", username)
            .withExpiresAt(Date(System.currentTimeMillis() + duration))
            .sign(verification)

    private fun getConfigProperty(path: String) =
        application.environment.config.property(path).getString()
}