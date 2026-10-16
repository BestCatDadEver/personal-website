package com.carlos.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.calllogging.*
import io.ktor.server.plugins.defaultheaders.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.hsts.*
import io.ktor.server.plugins.httpsredirect.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import org.slf4j.event.Level
import kotlin.time.Duration.Companion.minutes

/**
 * Todos los recursos de la pagina son propios, asi que la politica puede ser
 * estrictamente 'self': ningun origen externo, ni inline, ni eval.
 */
private val CONTENT_SECURITY_POLICY = listOf(
    "default-src 'self'",
    "script-src 'self'",
    "style-src 'self'",
    "img-src 'self' data:",
    "font-src 'self'",
    "connect-src 'self'",
    "form-action 'none'",
    "frame-ancestors 'none'",
    "base-uri 'none'",
    "object-src 'none'",
).joinToString("; ")

/** Evita que una ruta manipulada inyecte lineas falsas en el log. */
private fun sanitizeForLog(value: String): String =
    value.replace(Regex("[\\r\\n\\t]"), "_").take(256)

fun Application.configureHTTP() {
    // Solo se activa detras de un proxy TLS: en local romperia el arranque con un redirect infinito.
    val httpsOnly = environment.config
        .propertyOrNull("app.security.httpsOnly")?.getString().toBoolean()

    if (httpsOnly) {
        install(XForwardedHeaders)
        install(HttpsRedirect) {
            // La sonda de salud de la plataforma llega sin TLS: si se le
            // responde 301 el servicio se marca como caido.
            excludePrefix("/healthz")
        }
        install(HSTS) {
            maxAgeInSeconds = 31_536_000
            includeSubDomains = true
        }
    }

    install(DefaultHeaders) {
        // Sobrescribe el "Server: Ktor/x.y.z" que expone framework y version.
        header(HttpHeaders.Server, "web")
        header("X-Content-Type-Options", "nosniff")
        header("X-Frame-Options", "DENY")
        header("Referrer-Policy", "strict-origin-when-cross-origin")
        header("Permissions-Policy", "geolocation=(), camera=(), microphone=(), payment=(), usb=()")
        header("Content-Security-Policy", CONTENT_SECURITY_POLICY)
        header("Cross-Origin-Opener-Policy", "same-origin")
        header("Cross-Origin-Resource-Policy", "same-origin")
    }

    install(RateLimit) {
        global {
            rateLimiter(limit = 120, refillPeriod = 1.minutes)
        }
    }

    install(CallLogging) {
        level = Level.INFO
        filter { call ->
            val path = call.request.path()
            !path.startsWith("/static") && path != "/healthz"
        }
        format { call ->
            val method = call.request.httpMethod.value
            val path = sanitizeForLog(call.request.path())
            "$method $path -> ${call.response.status()?.value ?: "-"}"
        }
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.application.environment.log.error(
                "Unhandled error processing {}",
                sanitizeForLog(call.request.uri),
                cause,
            )
            call.respondText(
                text = "Ha ocurrido un error inesperado.",
                status = HttpStatusCode.InternalServerError,
            )
        }
        status(HttpStatusCode.NotFound) { call, status ->
            call.respondText(text = "Recurso no encontrado.", status = status)
        }
        status(HttpStatusCode.TooManyRequests) { call, status ->
            call.respondText(text = "Demasiadas peticiones. Intenta de nuevo en un minuto.", status = status)
        }
    }
}
