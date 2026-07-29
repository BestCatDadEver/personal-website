package com.carlos.repository

import com.carlos.models.Project

interface ProjectRepository {
    fun findAll(): List<Project>
}

class InMemoryProjectRepository : ProjectRepository {

    private val projects = listOf(
        Project(
            id = 1,
            name = "Pokedex",
            description = "App Android tipo Pokédex para explorar Pokémon, ver detalles como altura y peso, " +
                "y guardar favoritos con navegación entre dashboard y detalle.",
            descriptionEn = "Pokédex-style Android app to explore Pokémon, view details like height and weight, " +
                "and save favorites with navigation between dashboard and detail screens.",
            url = "https://github.com/BestCatDadEver/pokedex",
            tags = listOf("Kotlin", "Android", "MVVM", "Compose"),
            icon = "pokedex",
            imageUrl = "/static/images/pokedex.jpg",
        ),
    )

    override fun findAll(): List<Project> = projects
}
