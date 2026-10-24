package com.carlos.templates

/**
 * Saneado de URLs para atributos href/src.
 *
 * Los datos del portfolio hoy son constantes, pero estas funciones son la barrera que
 * impide que una futura fuente dinamica (CMS, JSON, API) introduzca "javascript:",
 * "data:text/html" o un origen no previsto directamente en el HTML renderizado.
 *
 * Las rutas internas son relativas ("static/...") y no absolutas ("/static/..."):
 * GitHub Pages sirve el sitio bajo /<repo>/, asi que una ruta absoluta apuntaria
 * fuera del sitio. Se siguen aceptando absolutas por si el sitio pasa a un dominio
 * propio servido desde la raiz.
 */

/**
 * Ningun host externo esta permitido: todas las imagenes del sitio son propias.
 * Anadir uno aqui obliga a anadirlo tambien a `img-src` en la CSP.
 */
private val ALLOWED_IMAGE_HOSTS = emptySet<String>()

/** Un esquema al principio de la cadena: "javascript:", "data:", "http:", "mailto:"... */
private val SCHEME = Regex("^[A-Za-z][A-Za-z0-9+.\\-]*:")

/**
 * Los navegadores descartan tabuladores y saltos de linea dentro de una URL *antes*
 * de interpretar el esquema, asi que "java\nscript:alert(1)" se ejecutaria igual.
 * Se eliminan antes de validar, y se devuelve siempre la cadena ya normalizada para
 * que lo que se valida sea exactamente lo que acaba en el HTML.
 */
private fun normalize(url: String): String = url.filter { it.code > 0x20 }

/**
 * Ruta dentro del propio sitio: relativa, o absoluta de un solo "/".
 * Se rechaza "//host" (relativa al protocolo) y "\host", que algunos navegadores
 * tratan igual, porque ambas saldrian del sitio.
 */
private fun isInternalPath(url: String): Boolean =
    url.isNotEmpty() &&
        !url.startsWith("//") &&
        !url.startsWith("\\") &&
        !SCHEME.containsMatchIn(url)

/** Acepta solo rutas internas, anclas y URLs https. Cualquier otra cosa queda inerte. */
fun safeHref(url: String): String {
    val value = normalize(url)
    return when {
        value.startsWith("#") -> value
        value.startsWith("https://") -> value
        isInternalPath(value) -> value
        else -> "#"
    }
}

/** Acepta rutas internas o https desde un host explicitamente permitido. */
fun safeImageSrc(url: String): String? {
    val value = normalize(url)
    return when {
        value.startsWith("https://") -> {
            val host = value.removePrefix("https://").substringBefore('/').substringBefore(':')
            if (host in ALLOWED_IMAGE_HOSTS) value else null
        }
        isInternalPath(value) -> value
        else -> null
    }
}

/** Acepta un correo simple para mailto:, evitando inyeccion de cabeceras via CRLF o "?". */
fun safeMailto(email: String): String =
    if (email.matches(Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"))) "mailto:$email" else "#"
