package com.carlos.repository

import com.carlos.models.Experience

interface ExperienceRepository {
    fun profileSummary(): String
    fun profileSummaryEn(): String
    fun findAll(): List<Experience>
}

class InMemoryExperienceRepository : ExperienceRepository {

    private val summary = "Ingeniero Senior Android y desarrollador Backend .NET / Kotlin, especializado en " +
        "arquitecturas móviles escalables (Clean Architecture, MVVM) y en construir soluciones robustas " +
        "alineadas a objetivos de negocio bajo metodologías ágiles."

    private val summaryEn = "Senior Android Engineer and .NET / Kotlin Backend developer, specialized in " +
        "scalable mobile architectures (Clean Architecture, MVVM) and in building robust solutions " +
        "aligned with business goals under agile methodologies."

    private val experiences = listOf(
        Experience(
            period = "2022 - 2026",
            role = "Android Engineer",
            roleEn = "Android Engineer",
            company = "Perficient Latin América",
            project = "Fintech — Aplicación móvil",
            projectEn = "Fintech — Mobile Application",
            highlights = listOf(
                "Desarrollo nativo en Kotlin: implementación de funcionalidades clave e integración de SDKs de proveedores.",
                "Consumo de APIs RESTful para gestión de transacciones, saldos, préstamos y plazos fijos.",
                "Interfaces con XML y Jetpack Compose, publicación en Play Store y pipelines de CI/CD.",
                "Seguridad de aplicaciones: escaneos SAST/MAST y resolución de vulnerabilidades detectadas.",
            ),
            highlightsEn = listOf(
                "Native Kotlin development: implementation of key features and integration of provider SDKs.",
                "Consumption of RESTful APIs for managing transactions, balances, loans, and fixed-term deposits.",
                "Interfaces with XML and Jetpack Compose, Play Store publishing and CI/CD pipelines.",
                "Application security: SAST/MAST scans and remediation of detected vulnerabilities.",
            ),
        ),
        Experience(
            period = "2018 - 2022",
            role = ".NET Engineer / Android Engineer / Tech Lead",
            roleEn = ".NET Engineer / Android Engineer / Tech Lead",
            company = "Tata Consultancy Services (TCS)",
            project = "Healthcare — Empresa de EE. UU.",
            projectEn = "Healthcare — U.S. Company",
            highlights = listOf(
                "Desarrollo y mantenimiento de microservicios en .NET junto con apps nativas Android (Kotlin, Java, C#).",
                "Trabajo bajo metodología Scrum, con Azure DevOps para gestión y pipelines de CI/CD.",
                "Mentoría técnica: entrevistas a candidatos y onboarding de nuevos miembros del equipo.",
                "Pair programming y coordinación con equipos distribuidos en distintas zonas horarias.",
            ),
            highlightsEn = listOf(
                "Development and maintenance of .NET microservices alongside native Android apps (Kotlin, Java, C#).",
                "Work under Scrum methodology, using Azure DevOps for management and CI/CD pipelines.",
                "Technical mentoring: candidate interviews and onboarding of new team members.",
                "Pair programming and coordination with teams distributed across different time zones.",
            ),
        ),
    )

    override fun profileSummary(): String = summary

    override fun profileSummaryEn(): String = summaryEn

    override fun findAll(): List<Experience> = experiences
}
