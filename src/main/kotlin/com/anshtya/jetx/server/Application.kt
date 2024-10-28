package com.anshtya.jetx.server

import com.anshtya.jetx.server.plugins.configureAuthentication
import com.anshtya.jetx.server.plugins.configureRouting
import com.anshtya.jetx.server.plugins.configureSerialization
import com.anshtya.jetx.server.repository.UserRepository
import com.anshtya.jetx.server.services.JwtService
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    val jwtService = JwtService(this)
    val userRepository = UserRepository()

    configureSerialization()
    configureAuthentication(jwtService, userRepository)
    configureRouting(jwtService, userRepository)
}
