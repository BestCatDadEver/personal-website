package com.carlos.repository

import com.carlos.models.Project

interface ProjectRepository {
    fun findAll(): List<Project>
}

class InMemoryProjectRepository : ProjectRepository {

    private val projects = listOf(
        Project(
            id = 1,
            name = "Personal Website",
            description = "Sitio personal construido con Kotlin y Ktor, desplegado como servidor backend.",
            url = "https://github.com/carlos/personal-website",
            tags = listOf("Kotlin", "Ktor"),
        ),
        Project(
            id = 2,
            name = "Task Tracker API",
            description = "API REST para gestión de tareas con autenticación y persistencia en base de datos.",
            url = "https://github.com/carlos/task-tracker-api",
            tags = listOf("Kotlin", "Ktor", "PostgreSQL"),
        ),
        Project(
            id = 3,
            name = "Weather App",
            description = "Aplicación multiplataforma que consume una API de clima usando Kotlin Multiplatform.",
            url = "https://github.com/carlos/weather-app",
            tags = listOf("Kotlin Multiplatform", "Compose"),
        ),
    )

    override fun findAll(): List<Project> = projects
}
