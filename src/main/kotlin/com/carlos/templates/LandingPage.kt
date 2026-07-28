package com.carlos.templates

import com.carlos.models.Experience
import com.carlos.models.Project
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
import kotlinx.html.lang
import kotlinx.html.li
import kotlinx.html.link
import kotlinx.html.main
import kotlinx.html.meta
import kotlinx.html.nav
import kotlinx.html.p
import kotlinx.html.section
import kotlinx.html.span
import kotlinx.html.title
import kotlinx.html.ul

private const val LOREM_IPSUM = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod " +
    "tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation " +
    "ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in " +
    "voluptate velit esse cillum dolore eu fugiat nulla pariatur."

fun HTML.landingPage(
    projects: List<Project>,
    profileSummary: String,
    experiences: List<Experience>,
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
                    experiences.forEach { experience ->
                        div(classes = "card experience-card") {
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
                        projects.forEach { project ->
                            div(classes = "project-card") {
                                h3 { +project.name }
                                p { +project.description }
                                if (project.tags.isNotEmpty()) {
                                    ul(classes = "project-tags") {
                                        project.tags.forEach { tag ->
                                            li { span { +tag } }
                                        }
                                    }
                                }
                                a(href = project.url, target = "_blank") { +"Ver proyecto" }
                            }
                        }
                    }
                }
            }
            section(classes = "section") {
                id = "acerca-de"
                h2 { +"Acerca de" }
                div(classes = "card") {
                    p { +LOREM_IPSUM }
                }
            }
        }
        footer {
            p { +"© ${java.time.Year.now()} Carlos Gelpi" }
        }
    }
}
