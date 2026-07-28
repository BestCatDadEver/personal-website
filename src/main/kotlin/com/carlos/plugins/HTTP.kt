package com.carlos.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.event.Level

fun Application.configureHTTP() {
    install(DefaultHeaders)

    install(CallLogging) {
        level = Level.INFO
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.environment.log.error("Unhandled error processing ${call.request.uri}", cause)
            call.respondText(
                text = "Ha ocurrido un error inesperado.",
                status = HttpStatusCode.InternalServerError,
            )
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "Recurso no encontrado.", status = status)
        }
    }
}
