package com.carlos.models

import kotlinx.serialization.Serializable

/**
 * Iconos disponibles para una tarjeta de proyecto.
 *
 * Es un enum y no un String porque el markup del icono se inyecta como HTML crudo:
 * restringir el tipo aqui hace imposible que un valor arbitrario llegue a ese punto.
 */
@Serializable
enum class ProjectIcon {
    WEB, API, WEATHER, HABIT, SPLIT, WALLET, DEVOPS, POKEDEX, SERVICES
}

/** Una captura dentro de la ficha de un proyecto. */
@Serializable
data class Screenshot(
    val src: String,
    val alt: String,
    val altEn: String = alt,
)

/** Un enlace externo de la ficha: repositorio, tienda, demo... */
@Serializable
data class ProjectLink(
    val label: String,
    val url: String,
    val labelEn: String = label,
)

@Serializable
data class Project(
    val id: Int,
    /** Identificador estable para enlazar la tarjeta con su ficha en el HTML. */
    val slug: String,
    val name: String,
    val description: String,
    val descriptionEn: String = description,
    val tags: List<String> = emptyList(),
    val featured: Boolean = false,
    val icon: ProjectIcon = ProjectIcon.WEB,
    val imageUrl: String? = null,
    /** Texto largo de la ficha. Por defecto, el mismo resumen de la tarjeta. */
    val detail: String = description,
    val detailEn: String = descriptionEn,
    val screenshots: List<Screenshot> = emptyList(),
    /**
     * Enlaces de la ficha. Es una lista y no un unico `url` porque un proyecto puede
     * no tener ninguno todavia, o tener varios (repositorio y tienda, por ejemplo).
     */
    val links: List<ProjectLink> = emptyList(),
)
