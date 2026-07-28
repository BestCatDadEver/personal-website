package com.carlos

import com.carlos.plugins.configureHTTP
import com.carlos.plugins.configureRouting
import com.carlos.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting()
}
