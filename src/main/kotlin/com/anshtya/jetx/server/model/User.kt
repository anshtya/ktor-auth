package com.anshtya.jetx.server.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String,
    val password: String // will encrypt later
)
