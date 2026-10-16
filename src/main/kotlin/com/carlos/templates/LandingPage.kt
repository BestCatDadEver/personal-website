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

private const val CONTACT_EMAIL = "cgelpi1439@gmail.com"

private const val LINKEDIN_URL = "https://www.linkedin.com/in/cgelpicx9/"
private const val GITHUB_URL = "https://github.com/BestCatDadEver"


/**
 * Numero de clases `.accent-N` definidas en styles.css.
 *
 * El color vive en la hoja de estilos y no en un atributo style="" inline: asi la
 * Content-Security-Policy puede mantener `style-src 'self'` sin 'unsafe-inline',
 * que es lo que desactivaria buena parte de su proteccion frente a XSS.
 */
private const val ACCENT_COUNT = 6

private fun accentClass(index: Int): String = "accent-${index.mod(ACCENT_COUNT)}"

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
                    a(href = "#contacto") { attributes["data-en"] = "Contact"; +"Contacto" }
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
                div(classes = "section-heading") {
                    h2 { attributes["data-en"] = "Experience"; +"Experiencia" }
                    div(classes = "social-links") {
                        a(href = safeHref(LINKEDIN_URL), target = "_blank") {
                            attributes["rel"] = "noopener noreferrer"
                            attributes["aria-label"] = "LinkedIn"
                            unsafe { raw(socialIconMarkup(SocialNetwork.LINKEDIN)) }
                        }
                        a(href = safeHref(GITHUB_URL), target = "_blank") {
                            attributes["rel"] = "noopener noreferrer"
                            attributes["aria-label"] = "GitHub"
                            unsafe { raw(socialIconMarkup(SocialNetwork.GITHUB)) }
                        }
                    }
                }
                p(classes = "profile-summary") {
                    attributes["data-en"] = profileSummaryEn
                    +profileSummary
                }
                div(classes = "experience-list") {
                    experiences.forEachIndexed { index, experience ->
                        div(classes = "card experience-card ${accentClass(index)}") {
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
                            val featuredClass = if (project.featured) " featured" else ""
                            div(classes = "project-card$featuredClass ${accentClass(index)}") {
                                div(classes = "project-thumb") {
                                    val thumbSrc = project.imageUrl?.let { safeImageSrc(it) }
                                    if (thumbSrc != null) {
                                        img(src = thumbSrc, alt = project.name, classes = "project-thumb-img")
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
                                a(href = safeHref(project.url), target = "_blank") {
                                    attributes["rel"] = "noopener noreferrer"
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
                div(classes = "card ${accentClass(2)}") {
                    p {
                        attributes["data-en"] = ABOUT_TEXT_EN
                        +ABOUT_TEXT
                    }
                }
            }
            section(classes = "section") {
                id = "contacto"
                h2 { attributes["data-en"] = "Contact"; +"Contacto" }
                div(classes = "card contact-card ${accentClass(3)}") {
                    p {
                        attributes["data-en"] = "Have a project in mind or want to get in touch? " +
                            "Send me an email and I'll get back to you as soon as possible."
                        +("¿Tenés un proyecto en mente o querés ponerte en contacto? " +
                            "Envíame un correo y te responderé a la brevedad.")
                    }
                    a(href = safeMailto(CONTACT_EMAIL), classes = "contact-button") {
                        attributes["data-en"] = "✉ Send an email"
                        +"✉ Enviar un correo"
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
                            safeImageSrc(technology.iconUrl)?.let { iconSrc ->
                                img(src = iconSrc, alt = technology.name)
                            }
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
