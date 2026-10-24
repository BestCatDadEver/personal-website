package com.carlos.templates

import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.lang
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.title

/**
 * Pagina que GitHub Pages sirve ante cualquier ruta inexistente.
 *
 * Sustituye al StatusPages del servidor, que ya no existe. A diferencia de
 * index.html, aqui los assets *no* pueden ir en rutas relativas: Pages devuelve este
 * mismo fichero para "/algo/muy/profundo", y el navegador resolveria "static/..."
 * contra esa ruta. De ahi que reciba el basePath del sitio ya resuelto.
 */
fun HTML.notFoundPage(basePath: String) {
    lang = "es"
    head {
        meta(charset = "UTF-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        meta {
            attributes["http-equiv"] = "Content-Security-Policy"
            attributes["content"] = CONTENT_SECURITY_POLICY
        }
        title("Página no encontrada | Carlos Gelpi")
        link(rel = "stylesheet", href = "${basePath}static/css/styles.css")
    }
    body {
        header(classes = "site-header") {
            div(classes = "header-inner") {
                h1(classes = "brand") { a(href = basePath) { +"Carlos Gelpi" } }
            }
        }
        main {
            section(classes = "section") {
                h2 { +"Página no encontrada" }
                div(classes = "card accent-0") {
                    p { +"La dirección que has abierto no existe." }
                    p { a(href = basePath) { +"Volver al inicio" } }
                }
            }
        }
    }
}
