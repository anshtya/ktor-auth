package com.anshtya.jetx.server.model

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String
)
