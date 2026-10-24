package com.carlos

import com.carlos.models.Project
import com.carlos.repository.ExperienceRepository
import com.carlos.repository.InMemoryExperienceRepository
import com.carlos.repository.InMemoryProjectRepository
import com.carlos.repository.InMemoryTechnologyRepository
import com.carlos.repository.ProjectRepository
import com.carlos.repository.TechnologyRepository
import com.carlos.templates.landingPage
import com.carlos.templates.notFoundPage
import kotlinx.html.HTML
import kotlinx.html.html
import kotlinx.html.stream.appendHTML
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Path

/**
 * Genera el sitio estatico que se publica en GitHub Pages.
 *
 * Sustituye al servidor Ktor que habia antes: las plantillas y los repositorios no
 * cambian, solo el momento en que se renderizan. Antes era por peticion, ahora una
 * vez en el build, de modo que el diseño sigue teniendo una sola fuente de verdad.
 */

private val json = Json {
    prettyPrint = true
    encodeDefaults = true
}

/** El HTML se escribe siempre en UTF-8: la pagina lleva acentos y emojis. */
private fun write(path: Path, content: String) {
    Files.createDirectories(path.parent)
    Files.writeString(path, content, StandardCharsets.UTF_8)
}

private fun render(block: HTML.() -> Unit): String = buildString {
    append("<!DOCTYPE html>\n")
    appendHTML().html { block() }
    append('\n')
}

fun renderLandingPage(
    projectRepository: ProjectRepository = InMemoryProjectRepository(),
    experienceRepository: ExperienceRepository = InMemoryExperienceRepository(),
    technologyRepository: TechnologyRepository = InMemoryTechnologyRepository(),
): String = render {
    landingPage(
        projects = projectRepository.findAll(),
        profileSummary = experienceRepository.profileSummary(),
        profileSummaryEn = experienceRepository.profileSummaryEn(),
        experiences = experienceRepository.findAll(),
        technologies = technologyRepository.findAll(),
    )
}

fun renderNotFoundPage(basePath: String): String = render { notFoundPage(basePath) }

fun renderProjectsJson(projects: List<Project>): String = json.encodeToString(projects)

fun main(args: Array<String>) {
    val outputDir = Path.of(args.getOrNull(0) ?: "build/site")
    // Pages sirve un repo de proyecto bajo /<repo>/. Solo lo necesita el 404; ver
    // NotFoundPage. Con un dominio propio servido desde la raiz, pasar "/".
    val basePath = (args.getOrNull(1) ?: "/").let { if (it.endsWith("/")) it else "$it/" }

    write(outputDir.resolve("index.html"), renderLandingPage())
    write(outputDir.resolve("404.html"), renderNotFoundPage(basePath))

    // El endpoint /api/projects del servidor se conserva como fichero estatico, asi
    // que sigue siendo consumible aunque ya no haya nada ejecutandose detras.
    write(
        outputDir.resolve("api/projects.json"),
        renderProjectsJson(InMemoryProjectRepository().findAll()),
    )

    // Publicando via Actions el artefacto se sirve tal cual. El fichero esta por
    // si algun dia se publica desde una rama, modo en el que Pages pasaria el
    // sitio por Jekyll y este ignoraria lo que empiece por "_".
    write(outputDir.resolve(".nojekyll"), "")

    println("Sitio generado en $outputDir (basePath=$basePath)")
}
