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
import kotlinx.html.button
import kotlinx.html.p
import kotlinx.html.script
import kotlinx.html.section
import kotlinx.html.span
import kotlinx.html.style
import kotlinx.html.title
import kotlinx.html.ul
import kotlinx.html.unsafe

private const val ABOUT_TEXT = "Con una trayectoria consolidada en proyectos de alta criticidad para el sector financiero y consultoría global, me especializo en construir soluciones robustas, escalables y orientadas a resultados de negocio. " +
        "¿Qué aporto a un equipo?\n" +
        "\n" +
        "- Liderazgo y Gestión: Experiencia dirigiendo equipos bajo metodologías Scrum, realizando Code Reviews y definiendo arquitecturas que aseguran la mantenibilidad a largo plazo.\n" +
        "- Expertiz Multilenguaje: Dominio de Kotlin para ecosistemas Android, junto con un stack Backend en .NET (C#) y Kotlin."

private const val ABOUT_TEXT_EN = "With a solid track record in high-criticality projects for the financial sector and global consulting, I specialize in building robust, scalable solutions oriented toward business outcomes. " +
        "What do I bring to a team?\n" +
        "\n" +
        "- Leadership and Management: Experience leading teams under Scrum methodologies, performing Code Reviews and defining architectures that ensure long-term maintainability.\n" +
        "- Multi-language Expertise: Strong command of Kotlin for Android ecosystems, along with a Backend stack in .NET (C#) and Kotlin."


private val ACCENTS = listOf("#38bdf8", "#a78bfa", "#f472b6", "#34d399", "#fbbf24", "#fb7185")

fun HTML.landingPage(
    projects: List<Project>,
    profileSummary: String,
    profileSummaryEn: String,
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
                    a(href = "#experiencia") { attributes["data-en"] = "Experience"; +"Experiencia" }
                    a(href = "#proyectos") { attributes["data-en"] = "Projects"; +"Proyectos" }
                    a(href = "#acerca-de") { attributes["data-en"] = "About"; +"Acerca de" }
                }
                div(classes = "lang-switch") {
                    attributes["role"] = "group"
                    attributes["aria-label"] = "Selector de idioma"
                    button(classes = "lang-btn active") {
                        attributes["type"] = "button"
                        attributes["data-lang-btn"] = "es"
                        attributes["aria-pressed"] = "true"
                        attributes["aria-label"] = "Español"
                        +"🇪🇸"
                    }
                    button(classes = "lang-btn") {
                        attributes["type"] = "button"
                        attributes["data-lang-btn"] = "en"
                        attributes["aria-pressed"] = "false"
                        attributes["aria-label"] = "English"
                        +"🇬🇧"
                    }
                }
            }
        }
        main {
            section(classes = "section") {
                id = "experiencia"
                h2 { attributes["data-en"] = "Experience"; +"Experiencia" }
                p(classes = "profile-summary") {
                    attributes["data-en"] = profileSummaryEn
                    +profileSummary
                }
                div(classes = "experience-list") {
                    experiences.forEachIndexed { index, experience ->
                        val accent = ACCENTS[index % ACCENTS.size]
                        div(classes = "card experience-card") {
                            style = "--card-accent: $accent"
                            div(classes = "experience-header") {
                                div {
                                    h3 {
                                        attributes["data-en"] = experience.roleEn
                                        +experience.role
                                    }
                                    p(classes = "experience-company") {
                                        val companyEn = experience.company +
                                            (experience.projectEn?.let { " · $it" } ?: "")
                                        attributes["data-en"] = companyEn
                                        +experience.company
                                        experience.project?.let { project -> +" · $project" }
                                    }
                                }
                                span(classes = "experience-period") { +experience.period }
                            }
                            if (experience.highlights.isNotEmpty()) {
                                ul(classes = "experience-highlights") {
                                    experience.highlights.forEachIndexed { highlightIndex, highlight ->
                                        li {
                                            experience.highlightsEn.getOrNull(highlightIndex)?.let {
                                                attributes["data-en"] = it
                                            }
                                            +highlight
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            section(classes = "section") {
                id = "proyectos"
                h2 { attributes["data-en"] = "Projects"; +"Proyectos" }
                if (projects.isEmpty()) {
                    p { attributes["data-en"] = "No projects published yet."; +"Todavía no hay proyectos publicados." }
                } else {
                    div(classes = "project-grid") {
                        projects.forEachIndexed { index, project ->
                            val accent = ACCENTS[index % ACCENTS.size]
                            val cardClasses = if (project.featured) "project-card featured" else "project-card"
                            div(classes = cardClasses) {
                                style = "--card-accent: $accent"
                                div(classes = "project-thumb") {
                                    if (project.imageUrl != null) {
                                        img(src = project.imageUrl, alt = project.name, classes = "project-thumb-img")
                                    } else {
                                        unsafe { raw(projectIconMarkup(project.icon)) }
                                    }
                                    if (project.featured) {
                                        span(classes = "featured-badge") {
                                            attributes["data-en"] = "★ Featured"
                                            +"★ Destacado"
                                        }
                                    }
                                }
                                h3 { +project.name }
                                p {
                                    attributes["data-en"] = project.descriptionEn
                                    +project.description
                                }
                                if (project.tags.isNotEmpty()) {
                                    ul(classes = "project-tags") {
                                        project.tags.forEach { tag ->
                                            li { span { +tag } }
                                        }
                                    }
                                }
                                a(href = project.url, target = "_blank") {
                                    attributes["data-en"] = "View project ↗"
                                    +"Ver proyecto ↗"
                                }
                            }
                        }
                    }
                }
            }
            section(classes = "section") {
                id = "acerca-de"
                h2 { attributes["data-en"] = "About"; +"Acerca de" }
                div(classes = "card") {
                    style = "--card-accent: ${ACCENTS[2]}"
                    p {
                        attributes["data-en"] = ABOUT_TEXT_EN
                        +ABOUT_TEXT
                    }
                }
            }
        }
        if (technologies.isNotEmpty()) {
            div(classes = "tech-carousel") {
                p(classes = "tech-carousel-heading") {
                    attributes["data-en"] = "Technologies I use"
                    +"Tecnologías que utilizo"
                }
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
        script(src = "/static/js/i18n.js") {}
    }
}
