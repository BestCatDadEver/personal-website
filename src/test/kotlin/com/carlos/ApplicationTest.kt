package com.carlos

import com.carlos.templates.safeHref
import com.carlos.templates.safeImageSrc
import com.carlos.templates.safeMailto
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun `landing page loads successfully`() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertTrue(bodyAsText().contains("Proyectos"))
        }
    }

    @Test
    fun `projects api returns json list`() = testApplication {
        application {
            module()
        }
        client.get("/api/projects").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(ContentType.Application.Json, contentType()?.withoutParameters())
        }
    }

    @Test
    fun `responses carry the security headers`() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals("nosniff", headers["X-Content-Type-Options"])
            assertEquals("DENY", headers["X-Frame-Options"])
            assertEquals("strict-origin-when-cross-origin", headers["Referrer-Policy"])
            assertEquals("same-origin", headers["Cross-Origin-Opener-Policy"])

            val csp = headers["Content-Security-Policy"]
            assertTrue(csp != null && csp.contains("default-src 'self'"), "CSP ausente: $csp")
            assertTrue(csp.contains("frame-ancestors 'none'"))
            assertTrue(csp.contains("object-src 'none'"))
        }
    }

    @Test
    fun `server header does not leak the framework version`() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            val server = headers["Server"].orEmpty()
            assertTrue(!server.contains("Ktor", ignoreCase = true), "Server filtra el framework: $server")
        }
    }

    @Test
    fun `landing page has no target blank without noopener`() = testApplication {
        application {
            module()
        }
        val html = client.get("/").bodyAsText()
        val anchors = Regex("<a\\b[^>]*>").findAll(html).map { it.value }
        val leaky = anchors.filter { it.contains("_blank") && !it.contains("noopener") }.toList()
        assertTrue(leaky.isEmpty(), "Enlaces _blank sin noopener: $leaky")
    }

    @Test
    fun `page renders without inline styles or scripts blocked by the CSP`() = testApplication {
        application {
            module()
        }
        val html = client.get("/").bodyAsText()
        assertTrue(!html.contains("style=\""), "Hay atributos style inline; la CSP style-src 'self' los bloquearia")
        assertTrue(!html.contains("<style"), "Hay bloques <style>; la CSP style-src 'self' los bloquearia")
        assertTrue(!Regex("<script(?![^>]*\\bsrc=)").containsMatchIn(html), "Hay <script> inline; la CSP lo bloquearia")
        // El acento debe seguir aplicandose, ahora via clase.
        assertTrue(html.contains("accent-"), "Se perdieron las clases de acento")
    }

    @Test
    fun `safeHref neutralises dangerous schemes`() {
        assertEquals("#", safeHref("javascript:alert(1)"))
        assertEquals("#", safeHref("data:text/html,<script>alert(1)</script>"))
        assertEquals("#", safeHref("http://insecure.example"))
        assertEquals("#", safeHref("//evil.example/path"))
        assertEquals("/static/css/styles.css", safeHref("/static/css/styles.css"))
        assertEquals("https://github.com/BestCatDadEver", safeHref("https://github.com/BestCatDadEver"))
    }

    @Test
    fun `safeImageSrc only allows local paths`() {
        assertNull(safeImageSrc("javascript:alert(1)"))
        assertNull(safeImageSrc("https://evil.example/logo.svg"))
        assertNull(safeImageSrc("//evil.example/logo.svg"))
        assertNull(safeImageSrc("https://cdn.jsdelivr.net/npm/simple-icons/icons/kotlin.svg"))
        assertEquals("/static/images/pokedex.jpg", safeImageSrc("/static/images/pokedex.jpg"))
        assertEquals("/static/images/tech/kotlin.svg", safeImageSrc("/static/images/tech/kotlin.svg"))
    }

    @Test
    fun `page loads no third party resources`() = testApplication {
        application {
            module()
        }
        val response = client.get("/")
        val html = response.bodyAsText()

        // Solo cuentan los recursos que el navegador *carga*: <img src>, <script src>,
        // <link href>. Los <a href> salientes son enlaces legitimos, no subrecursos.
        val external = Regex("""<(?:img|script)\b[^>]*\bsrc="(https?://[^"]+)"|<link\b[^>]*\bhref="(https?://[^"]+)"""")
            .findAll(html)
            .map { it.groupValues[1].ifEmpty { it.groupValues[2] } }
            .toList()
        assertTrue(external.isEmpty(), "La pagina carga recursos de terceros: $external")

        val csp = response.headers["Content-Security-Policy"].orEmpty()
        assertTrue(csp.contains("img-src 'self' data:;"), "La CSP aun permite origenes externos: $csp")
        assertTrue(!csp.contains("jsdelivr"), "La CSP aun menciona el CDN: $csp")
    }

    @Test
    fun `every technology icon is served locally`() = testApplication {
        application {
            module()
        }
        val html = client.get("/").bodyAsText()
        val icons = Regex("""<img\b[^>]*\bsrc="(/static/images/tech/[^"]+)"""")
            .findAll(html)
            .map { it.groupValues[1] }
            .distinct()
            .toList()
        assertEquals(8, icons.size, "Se esperaban 8 iconos locales, se encontraron: $icons")
        icons.forEach { path ->
            val response = client.get(path)
            assertEquals(HttpStatusCode.OK, response.status, "Icono no servido: $path")
            assertTrue(response.bodyAsText().contains("<svg"), "No es un SVG valido: $path")
        }
    }

    @Test
    fun `safeMailto rejects header injection`() {
        assertEquals("#", safeMailto("a@b.com%0ABcc:victim@example.com"))
        assertEquals("#", safeMailto("a@b.com?subject=hi"))
        assertEquals("mailto:cgelpi1439@gmail.com", safeMailto("cgelpi1439@gmail.com"))
    }

    @Test
    fun `httpsOnly is off by default so local development still works`() = testApplication {
        application {
            module()
        }
        client.get("/").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertNull(headers[HttpHeaders.StrictTransportSecurity])
        }
    }

    @Test
    fun `httpsOnly redirects plain http to https`() = testApplication {
        environment {
            config = MapApplicationConfig("app.security.httpsOnly" to "true")
        }
        application {
            module()
        }
        val client = createClient { followRedirects = false }
        client.get("/").apply {
            assertTrue(
                status == HttpStatusCode.MovedPermanently || status == HttpStatusCode.Found,
                "Se esperaba una redireccion a HTTPS, se obtuvo $status",
            )
            assertTrue(
                headers[HttpHeaders.Location].orEmpty().startsWith("https://"),
                "Location no apunta a https: ${headers[HttpHeaders.Location]}",
            )
        }
    }

    @Test
    fun `error pages do not leak internals`() = testApplication {
        application {
            module()
        }
        client.get("/ruta-inexistente").apply {
            assertEquals(HttpStatusCode.NotFound, status)
            val body = bodyAsText()
            assertEquals("Recurso no encontrado.", body)
            assertTrue(!body.contains("io.ktor"), "La respuesta filtra detalles internos")
        }
    }

    @Test
    fun `static resources cannot escape their root`() = testApplication {
        application {
            module()
        }
        listOf(
            "/static/../application.yaml",
            "/static/%2e%2e/application.yaml",
            "/static/..%2f..%2fapplication.yaml",
        ).forEach { path ->
            val response = client.get(path)
            assertTrue(
                response.status != HttpStatusCode.OK || !response.bodyAsText().contains("ktor:"),
                "Path traversal posible en $path",
            )
        }
    }
}
