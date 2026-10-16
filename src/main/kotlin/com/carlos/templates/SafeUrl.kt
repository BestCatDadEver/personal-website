package com.carlos.templates

/**
 * Saneado de URLs para atributos href/src.
 *
 * Los datos del portfolio hoy son constantes, pero estas funciones son la barrera que
 * impide que una futura fuente dinamica (CMS, JSON, API) introduzca "javascript:",
 * "data:text/html" o un origen no previsto directamente en el HTML renderizado.
 */

/**
 * Ningun host externo esta permitido: todas las imagenes del sitio son propias.
 * Anadir uno aqui obliga a anadirlo tambien a `img-src` en la CSP.
 */
private val ALLOWED_IMAGE_HOSTS = emptySet<String>()

/** Acepta solo rutas internas absolutas y URLs https. Cualquier otra cosa queda inerte. */
fun safeHref(url: String): String = when {
    url.startsWith("/") && !url.startsWith("//") -> url
    url.startsWith("#") -> url
    url.startsWith("https://") -> url
    else -> "#"
}

/** Acepta rutas internas o https desde un host explicitamente permitido. */
fun safeImageSrc(url: String): String? = when {
    url.startsWith("/") && !url.startsWith("//") -> url
    url.startsWith("https://") -> {
        val host = url.removePrefix("https://").substringBefore('/').substringBefore(':')
        if (host in ALLOWED_IMAGE_HOSTS) url else null
    }
    else -> null
}

/** Acepta un correo simple para mailto:, evitando inyeccion de cabeceras via CRLF o "?". */
fun safeMailto(email: String): String =
    if (email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) "mailto:$email" else "#"
