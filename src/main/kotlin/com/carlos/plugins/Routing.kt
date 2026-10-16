package com.carlos.plugins

import com.carlos.repository.ExperienceRepository
import com.carlos.repository.InMemoryExperienceRepository
import com.carlos.repository.InMemoryProjectRepository
import com.carlos.repository.InMemoryTechnologyRepository
import com.carlos.repository.ProjectRepository
import com.carlos.repository.TechnologyRepository
import com.carlos.templates.landingPage
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting(
    projectRepository: ProjectRepository = InMemoryProjectRepository(),
    experienceRepository: ExperienceRepository = InMemoryExperienceRepository(),
    technologyRepository: TechnologyRepository = InMemoryTechnologyRepository(),
) {
    routing {
        get("/") {
            call.respondHtml {
                landingPage(
                    projects = projectRepository.findAll(),
                    profileSummary = experienceRepository.profileSummary(),
                    profileSummaryEn = experienceRepository.profileSummaryEn(),
                    experiences = experienceRepository.findAll(),
                    technologies = technologyRepository.findAll(),
                )
            }
        }
        get("/api/projects") {
            call.respond(projectRepository.findAll())
        }
        // Sonda de salud de la plataforma (Render). La comprobacion llega por
        // HTTP interno, sin TLS, asi que esta excluida de la redireccion a
        // HTTPS en configureHTTP().
        get("/healthz") {
            call.respondText("ok")
        }
        staticResources("/static", "static")
    }
}
