package com.carlos.templates

import com.carlos.models.Experience
import com.carlos.models.Project
import com.carlos.models.Technology
import kotlinx.html.HTML
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.div
import kotlinx.html.footer
import kotlinx.html.h1
import kotlinx.html.h2
import kotlinx.html.h3
import kotlinx.html.head
import kotlinx.html.header
import kotlinx.html.id
import kotlinx.html.img
import kotlinx.html.lang
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.ul

private const val ABOUT_TEXT = "Con una trayectoria consolidada en proyectos de alta criticidad para el sector financiero y consultoría global, me especializo en construir soluciones robustas, escalables y orientadas a resultados de negocio. " +
        "¿Qué aporto a un equipo?\n" +
        "\n" +
        "- Liderazgo y Gestión: Experiencia dirigiendo equipos bajo metodologías Scrum, realizando Code Reviews y definiendo arquitecturas que aseguran la mantenibilidad a largo plazo.\n" +
        "- Expertiz Multilenguaje: Dominio de Kotlin para ecosistemas Android, junto con un stack Backend en .NET (C#) y Kotlin."


private val ACCENTS = listOf("#38bdf8", "#a78bfa", "#f472b6", "#34d399", "#fbbf24", "#fb7185")

fun HTML.landingPage(
    projects: List<Project>,
    profileSummary: String,
    experiences: List<Experience>,
    technologies: List<Technology>,
) {
    lang = "es"
    head {
        meta(charset = "UTF-8")
        meta(name = "viewport", content = "width=device-width, initial-scale=1")
        title("Carlos Gelpi | Portfolio")
        link(rel = "stylesheet", href = "/static/css/styles.css")
    }
    body {
        header(classes = "site-header") {
            div(classes = "header-inner") {
                h1(classes = "brand") { a(href = "#") { +"Carlos Gelpi" } }
                nav(classes = "nav-tabs") {
                    a(href = "#experiencia") { +"Experiencia" }
                    a(href = "#proyectos") { +"Proyectos" }
                    a(href = "#acerca-de") { +"Acerca de" }
                }
            }
        }
        main {
            section(classes = "section") {
                id = "experiencia"
                h2 { +"Experiencia" }
                p(classes = "profile-summary") { +profileSummary }
                div(classes = "experience-list") {
                    experiences.forEachIndexed { index, experience ->
                        val accent = ACCENTS[index % ACCENTS.size]
                        div(classes = "card experience-card") {
                            style = "--card-accent: $accent"
                            div(classes = "experience-header") {
                                div {
                                    h3 { +experience.role }
                                    p(classes = "experience-company") {
                                        +experience.company
                                        experience.project?.let { project -> +" · $project" }
                                    }
                                }
                                span(classes = "experience-period") { +experience.period }
                            }
                            if (experience.highlights.isNotEmpty()) {
                                ul(classes = "experience-highlights") {
                                    experience.highlights.forEach { highlight ->
                                        li { +highlight }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            section(classes = "section") {
                id = "proyectos"
                h2 { +"Proyectos" }
                if (projects.isEmpty()) {
                    p { +"Todavía no hay proyectos publicados." }
                } else {
                    div(classes = "project-grid") {
                        projects.forEachIndexed { index, project ->
                            val accent = ACCENTS[index % ACCENTS.size]
                            val cardClasses = if (project.featured) "project-card featured" else "project-card"
                            div(classes = cardClasses) {
                                style = "--card-accent: $accent"
                                if (project.featured) {
                                    span(classes = "featured-badge") { +"★ Destacado" }
                                }
                                h3 { +project.name }
                                p { +project.description }
                                if (project.tags.isNotEmpty()) {
                                    ul(classes = "project-tags") {
                                        project.tags.forEach { tag ->
                                            li { span { +tag } }
                                        }
                                    }
                                }
                                a(href = project.url, target = "_blank") { +"Ver proyecto ↗" }
                            }
                        }
                    }
                }
            }
            section(classes = "section") {
                id = "acerca-de"
                h2 { +"Acerca de" }
                div(classes = "card") {
                    style = "--card-accent: ${ACCENTS[2]}"
                    p { +ABOUT_TEXT }
                }
            }
        }
        if (technologies.isNotEmpty()) {
            div(classes = "tech-carousel") {
                p(classes = "tech-carousel-heading") { +"Tecnologías que utilizo" }
                div(classes = "tech-track") {
                    (technologies + technologies).forEach { technology ->
                        div(classes = "tech-item") {
                            img(src = technology.iconUrl, alt = technology.name)
                            span { +technology.name }
                        }
                    }
                }
            }
        }
        footer {
            p { +"© ${java.time.Year.now()} Carlos Gelpi" }
        }
    }
}
