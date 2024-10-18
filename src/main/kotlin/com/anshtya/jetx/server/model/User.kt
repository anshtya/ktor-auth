package com.anshtya.jetx.server.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val email: String,
    val password: String // will encrypt later
)
