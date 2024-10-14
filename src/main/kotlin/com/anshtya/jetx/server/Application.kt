package com.anshtya.jetx.server

import com.anshtya.jetx.server.plugins.configureRouting
import com.anshtya.jetx.server.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}
