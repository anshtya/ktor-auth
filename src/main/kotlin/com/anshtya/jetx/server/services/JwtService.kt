package com.anshtya.jetx.server.services

import com.anshtya.jetx.server.util.JWT_KEY_ID
import com.anshtya.jetx.server.util.JWT_KEY_TYPE
import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.server.application.Application
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.Base64
import java.util.Date
import java.util.concurrent.TimeUnit

class JwtService(
    private val application: Application
) {
    val privateKeyString = getConfigProperty("jwt.privateKey")
    val issuer = getConfigProperty("jwt.issuer")
    val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")

    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    fun generateJwtToken(claim: String): String {
        val publicKey = jwkProvider.get(JWT_KEY_ID).publicKey
        val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
        val privateKey = KeyFactory.getInstance(JWT_KEY_TYPE).generatePrivate(keySpecPKCS8)

        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("username", claim)
            .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
            .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
    }

    private fun getConfigProperty(path: String) =
        application.environment.config.property(path).getString()
}