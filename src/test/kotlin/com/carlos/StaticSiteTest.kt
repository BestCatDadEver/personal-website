package com.carlos

import com.carlos.repository.InMemoryProjectRepository
import com.carlos.templates.safeHref
import com.carlos.templates.safeImageSrc
import com.carlos.templates.safeMailto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import java.io.File
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

/**
 * El sitio ya no se sirve, se genera. Estos tests corren sobre el HTML renderizado.
 *
 * Los tests que comprobaban cabeceras HTTP, la redireccion a HTTPS, el rate limit y
 * el path traversal de los recursos estaticos desaparecieron con el servidor: en
 * GitHub Pages no hay nada de eso que probar.
 */
class StaticSiteTest {

    private val html = renderLandingPage()
    private val staticRoot = File("src/main/resources/static")

    private fun attributeValues(attribute: String): List<String> =
        Regex("""\b$attribute="([^"]*)"""").findAll(html).map { it.groupValues[1] }.toList()

    @Test
    fun `landing page renders`() {
        assertTrue(html.startsWith("<!DOCTYPE html>"), "Falta el doctype")
        assertTrue(html.contains("Proyectos"))
        assertTrue(html.contains("Experiencia"))
        assertTrue(html.contains("accent-"), "Se perdieron las clases de acento")
    }

    /**
     * La regresion que romperia el sitio en Pages: se publica bajo /<repo>/, asi que
     * una ruta absoluta como "/static/css/styles.css" apuntaria fuera del sitio.
     */
    @Test
    fun `internal asset references are relative`() {
        val absolute = (attributeValues("src") + attributeValues("href"))
            .filter { it.startsWith("/") && !it.startsWith("//") }
        assertTrue(absolute.isEmpty(), "Rutas absolutas que romperian en Pages: $absolute")
    }

    @Test
    fun `every referenced asset exists on disk`() {
        val assets = (attributeValues("src") + attributeValues("href"))
            .filter { it.startsWith("static/") }
            .distinct()
        assertTrue(assets.isNotEmpty(), "No se referencia ningun asset local")
        assets.forEach { path ->
            val file = File(staticRoot, path.removePrefix("static/"))
            assertTrue(file.isFile, "Asset referenciado que no existe: $path")
        }
    }

    @Test
    fun `page declares the content security policy`() {
        val csp = Regex("""http-equiv="Content-Security-Policy"\s+content="([^"]*)"""")
            .find(html)?.groupValues?.get(1)
        assertTrue(csp != null, "Falta el meta de CSP")
        assertTrue(csp.contains("default-src 'self'"), "CSP inesperada: $csp")
        assertTrue(csp.contains("object-src 'none'"))
        assertTrue(!csp.contains("unsafe-inline"), "La CSP permite inline: $csp")
        // frame-ancestors solo funciona como cabecera, no en un meta: no debe estar.
        assertTrue(!csp.contains("frame-ancestors"), "frame-ancestors no aplica en un meta")
    }

    @Test
    fun `page has no inline styles or scripts blocked by the CSP`() {
        assertTrue(!html.contains("style=\""), "Hay atributos style inline; la CSP los bloquearia")
        assertTrue(!html.contains("<style"), "Hay bloques <style>; la CSP los bloquearia")
        assertTrue(
            !Regex("<script(?![^>]*\\bsrc=)").containsMatchIn(html),
            "Hay <script> inline; la CSP lo bloquearia",
        )
    }

    @Test
    fun `page loads no third party resources`() {
        val external = Regex("""<(?:img|script)\b[^>]*\bsrc="(https?://[^"]+)"|<link\b[^>]*\bhref="(https?://[^"]+)"""")
            .findAll(html)
            .map { it.groupValues[1].ifEmpty { it.groupValues[2] } }
            .toList()
        assertTrue(external.isEmpty(), "La pagina carga recursos de terceros: $external")
    }

    @Test
    fun `landing page has no target blank without noopener`() {
        val leaky = Regex("<a\\b[^>]*>").findAll(html)
            .map { it.value }
            .filter { it.contains("_blank") && !it.contains("noopener") }
            .toList()
        assertTrue(leaky.isEmpty(), "Enlaces _blank sin noopener: $leaky")
    }

    @Test
    fun `every technology icon is served locally`() {
        val icons = Regex("""<img\b[^>]*\bsrc="(static/images/tech/[^"]+)"""")
            .findAll(html)
            .map { it.groupValues[1] }
            .distinct()
            .toList()
        assertEquals(8, icons.size, "Se esperaban 8 iconos locales, se encontraron: $icons")
    }

    @Test
    fun `not found page uses root absolute assets`() {
        val notFound = renderNotFoundPage("/personal-website/")
        // Pages devuelve este fichero para rutas de cualquier profundidad, asi que
        // una ruta relativa se resolveria contra la URL pedida y no contra el sitio.
        assertTrue(
            notFound.contains("""href="/personal-website/static/css/styles.css""""),
            "El 404 no apunta al CSS con ruta absoluta",
        )
    }

    @Test
    fun `projects json is valid and not empty`() {
        val projects = InMemoryProjectRepository().findAll()
        val parsed = Json.parseToJsonElement(renderProjectsJson(projects)).jsonArray
        assertEquals(projects.size, parsed.size)
    }

    @Test
    fun `safeHref neutralises dangerous schemes`() {
        assertEquals("#", safeHref("javascript:alert(1)"))
        assertEquals("#", safeHref("data:text/html,<script>alert(1)</script>"))
        assertEquals("#", safeHref("http://insecure.example"))
        assertEquals("#", safeHref("//evil.example/path"))
        // Los navegadores descartan los saltos de linea antes de leer el esquema.
        assertEquals("#", safeHref("java\nscript:alert(1)"))
        assertEquals("#", safeHref("\\evil.example/path"))
        assertEquals("static/css/styles.css", safeHref("static/css/styles.css"))
        assertEquals("/static/css/styles.css", safeHref("/static/css/styles.css"))
        assertEquals("#contacto", safeHref("#contacto"))
        assertEquals("https://github.com/BestCatDadEver", safeHref("https://github.com/BestCatDadEver"))
    }

    @Test
    fun `safeImageSrc only allows local paths`() {
        assertNull(safeImageSrc("javascript:alert(1)"))
        assertNull(safeImageSrc("https://evil.example/logo.svg"))
        assertNull(safeImageSrc("//evil.example/logo.svg"))
        assertNull(safeImageSrc("https://cdn.jsdelivr.net/npm/simple-icons/icons/kotlin.svg"))
        assertEquals("static/images/pokedex.jpg", safeImageSrc("static/images/pokedex.jpg"))
        assertEquals("static/images/tech/kotlin.svg", safeImageSrc("static/images/tech/kotlin.svg"))
    }

    @Test
    fun `safeMailto rejects header injection`() {
        assertEquals("#", safeMailto("a@b.com%0ABcc:victim@example.com"))
        assertEquals("#", safeMailto("a@b.com?subject=hi"))
        assertEquals("mailto:cgelpi1439@gmail.com", safeMailto("cgelpi1439@gmail.com"))
    }
}
