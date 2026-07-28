package com.carlos.repository

import com.carlos.models.Experience

interface ExperienceRepository {
    fun profileSummary(): String
    fun findAll(): List<Experience>
}

class InMemoryExperienceRepository : ExperienceRepository {

    private val summary = "Ingeniero Senior Android y desarrollador Backend .NET / Kotlin, especializado en " +
        "arquitecturas móviles escalables (Clean Architecture, MVVM) y en construir soluciones robustas " +
        "alineadas a objetivos de negocio bajo metodologías ágiles."

    private val experiences = listOf(
        Experience(
            period = "2022 - 2026",
            role = "Android Engineer",
            company = "Perficient Latin América",
            project = "Fintech — Aplicación móvil",
            highlights = listOf(
                "Desarrollo nativo en Kotlin: implementación de funcionalidades clave e integración de SDKs de proveedores.",
                "Consumo de APIs RESTful para gestión de transacciones, saldos, préstamos y plazos fijos.",
                "Interfaces con XML y Jetpack Compose, publicación en Play Store y pipelines de CI/CD.",
                "Seguridad de aplicaciones: escaneos SAST/MAST y resolución de vulnerabilidades detectadas.",
            ),
        ),
        Experience(
            period = "2018 - 2022",
            role = ".NET Engineer / Android Engineer / Tech Lead",
            company = "Tata Consultancy Services (TCS)",
            project = "Healthcare — Empresa de EE. UU.",
            highlights = listOf(
                "Desarrollo y mantenimiento de microservicios en .NET junto con apps nativas Android (Kotlin, Java, C#).",
                "Trabajo bajo metodología Scrum, con Azure DevOps para gestión y pipelines de CI/CD.",
                "Mentoría técnica: entrevistas a candidatos y onboarding de nuevos miembros del equipo.",
                "Pair programming y coordinación con equipos distribuidos en distintas zonas horarias.",
            ),
        ),
    )

    override fun profileSummary(): String = summary

    override fun findAll(): List<Experience> = experiences
}
