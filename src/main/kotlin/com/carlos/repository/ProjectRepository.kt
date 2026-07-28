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
            description = "Sitio personal construido con Kotlin y Ktor, desplegado como servidor backend " +
                "con renderizado server-side y buenas prácticas de arquitectura.",
            url = "https://github.com/carlos/personal-website",
            tags = listOf("Kotlin", "Ktor"),
            featured = true,
        ),
        Project(
            id = 2,
            name = "Task Tracker API",
            description = "API REST para gestión de tareas con autenticación, control de permisos y " +
                "persistencia en base de datos relacional.",
            url = "https://github.com/carlos/task-tracker-api",
            tags = listOf("Kotlin", "Ktor", "PostgreSQL"),
            featured = true,
        ),
        Project(
            id = 3,
            name = "Weather App",
            description = "Aplicación multiplataforma que consume una API de clima usando Kotlin Multiplatform.",
            url = "https://github.com/carlos/weather-app",
            tags = listOf("Kotlin Multiplatform", "Compose"),
        ),
        Project(
            id = 4,
            name = "HabitFlow",
            description = "App Android para seguimiento de hábitos diarios con recordatorios y estadísticas.",
            url = "https://github.com/carlos/habitflow",
            tags = listOf("Kotlin", "Jetpack Compose", "Room"),
        ),
        Project(
            id = 5,
            name = "SplitBill",
            description = "App para dividir gastos entre grupos de amigos, con sincronización en tiempo real.",
            url = "https://github.com/carlos/splitbill",
            tags = listOf("Kotlin", "Firebase", "MVVM"),
        ),
        Project(
            id = 6,
            name = "Wallet Prototype",
            description = "Prueba de concepto de billetera digital con Clean Architecture, pensada como " +
                "base para proyectos fintech.",
            url = "https://github.com/carlos/wallet-prototype",
            tags = listOf("Kotlin", "Clean Architecture", "Fintech"),
        ),
        Project(
            id = 7,
            name = "DevOps Pipeline Kit",
            description = "Plantillas reutilizables de CI/CD para apps Android y servicios Kotlin.",
            url = "https://github.com/carlos/devops-pipeline-kit",
            tags = listOf("Azure DevOps", "GitHub Actions", "CI/CD"),
        ),
    )

    override fun findAll(): List<Project> = projects
}
