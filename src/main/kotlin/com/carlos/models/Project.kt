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
    WEB, API, WEATHER, HABIT, SPLIT, WALLET, DEVOPS, POKEDEX
}

@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val descriptionEn: String = description,
    val url: String,
    val tags: List<String> = emptyList(),
    val featured: Boolean = false,
    val icon: ProjectIcon = ProjectIcon.WEB,
    val imageUrl: String? = null,
)
