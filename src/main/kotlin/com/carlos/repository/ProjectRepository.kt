package com.carlos.repository

import com.carlos.models.Project
import com.carlos.models.ProjectIcon
import com.carlos.models.ProjectLink

interface ProjectRepository {
    fun findAll(): List<Project>
}

class InMemoryProjectRepository : ProjectRepository {

    private val projects = listOf(
        Project(
            id = 1,
            slug = "pokedex",
            name = "Pokedex",
            description = "App Android tipo Pokédex para explorar Pokémon, ver detalles como altura y peso, " +
                "y guardar favoritos con navegación entre dashboard y detalle.",
            descriptionEn = "Pokédex-style Android app to explore Pokémon, view details like height and weight, " +
                "and save favorites with navigation between dashboard and detail screens.",
            tags = listOf("Kotlin", "Android", "MVVM", "Compose"),
            icon = ProjectIcon.POKEDEX,
            imageUrl = "static/images/pokedex.jpg",
            links = listOf(
                ProjectLink(
                    label = "Ver en GitHub",
                    labelEn = "View on GitHub",
                    url = "https://github.com/BestCatDadEver/pokedex",
                ),
            ),
        ),
        Project(
            id = 2,
            slug = "todopro",
            name = "TodoPro",
            description = "Aplicación móvil que conecta a profesionales que ofrecen servicios " +
                "con los clientes que los necesitan.",
            descriptionEn = "Mobile app that connects professionals offering services " +
                "with the clients who need them.",
            icon = ProjectIcon.SERVICES,
            // Las capturas van en static/images/projects/todopro/ y se listan aqui
            // como Screenshot(src, alt, altEn). Sin ellas la ficha omite la galeria.
            screenshots = emptyList(),
        ),
    )

    override fun findAll(): List<Project> = projects
}
