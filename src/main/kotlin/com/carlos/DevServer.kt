package com.carlos

import com.sun.net.httpserver.HttpExchange
import com.sun.net.httpserver.HttpServer
import java.net.InetSocketAddress
import java.nio.file.Files
import java.nio.file.Path

/**
 * Servidor de desarrollo para ver el sitio generado en local.
 *
 * No tiene nada que ver con el Ktor que se retiro: usa el servidor del JDK, no
 * anade dependencias y no se despliega en ningun sitio. Solo existe para servir
 * build/site igual que lo haria GitHub Pages, porque abrir el index.html con
 * file:// no reproduce el comportamiento real (rutas, 404, tipos MIME).
 */

private val CONTENT_TYPES = mapOf(
    "html" to "text/html; charset=utf-8",
    "css" to "text/css; charset=utf-8",
    "js" to "text/javascript; charset=utf-8",
    "json" to "application/json; charset=utf-8",
    "svg" to "image/svg+xml",
    "jpg" to "image/jpeg",
    "jpeg" to "image/jpeg",
    "png" to "image/png",
    "ico" to "image/x-icon",
)

private fun contentTypeOf(path: Path): String =
    CONTENT_TYPES[path.fileName.toString().substringAfterLast('.', "").lowercase()]
        ?: "application/octet-stream"

/**
 * Resuelve la ruta pedida dentro de la raiz del sitio.
 *
 * Devuelve null si la ruta se sale de la raiz: sin esta comprobacion, un
 * "GET /../../secrets" serviria ficheros de fuera del sitio. Es un servidor local,
 * pero escucha en un puerto y no cuesta nada cerrarlo.
 */
private fun resolveWithin(root: Path, requestPath: String): Path? {
    val relative = requestPath.removePrefix("/").ifEmpty { "index.html" }
    val resolved = root.resolve(relative).normalize()
    if (!resolved.startsWith(root)) return null
    return if (Files.isDirectory(resolved)) resolved.resolve("index.html") else resolved
}

private fun respond(exchange: HttpExchange, status: Int, body: ByteArray, contentType: String) {
    exchange.responseHeaders.add("Content-Type", contentType)
    // Una respuesta a HEAD no lleva cuerpo: -1 es como se le indica al servidor
    // del JDK, y pasarle una longitud provoca un warning en cada peticion.
    if (exchange.requestMethod.equals("HEAD", ignoreCase = true)) {
        exchange.sendResponseHeaders(status, -1)
        return
    }
    exchange.sendResponseHeaders(status, body.size.toLong())
    exchange.responseBody.use { it.write(body) }
}

fun main(args: Array<String>) {
    val root = Path.of(args.getOrNull(0) ?: "build/site").toAbsolutePath().normalize()
    val port = args.getOrNull(1)?.toIntOrNull() ?: 8080

    require(Files.isDirectory(root)) { "No existe $root. Ejecuta antes: gradlew generateSite" }

    val server = HttpServer.create(InetSocketAddress(port), 0)
    server.createContext("/") { exchange ->
        exchange.use {
            val target = resolveWithin(root, it.requestURI.path)
            if (target != null && Files.isRegularFile(target)) {
                respond(it, 200, Files.readAllBytes(target), contentTypeOf(target))
            } else {
                // Igual que Pages: cualquier ruta desconocida devuelve el 404 del sitio.
                val notFound = root.resolve("404.html")
                val body = if (Files.isRegularFile(notFound)) Files.readAllBytes(notFound) else ByteArray(0)
                respond(it, 404, body, "text/html; charset=utf-8")
            }
        }
    }
    server.start()
    println("Sirviendo $root en http://localhost:$port  (Ctrl+C para parar)")
}
