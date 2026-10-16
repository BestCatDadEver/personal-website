package com.carlos.repository

import com.carlos.models.Technology

interface TechnologyRepository {
    fun findAll(): List<Technology>
}

class InMemoryTechnologyRepository : TechnologyRepository {

    // Iconos auto-hospedados. Servirlos desde un CDN publico significaba que un
    // compromiso del paquete llegaba a los visitantes sin revision, y que la IP de
    // cada visitante se filtraba a un tercero.
    private val iconBaseUrl = "/static/images/tech"

    private val technologies = listOf(
        Technology("Kotlin", "$iconBaseUrl/kotlin.svg"),
        Technology(".NET", "$iconBaseUrl/dotnet.svg"),
        Technology("C#", "$iconBaseUrl/csharp.svg"),
        Technology("Jetpack Compose", "$iconBaseUrl/jetpackcompose.svg"),
        Technology("Java", "$iconBaseUrl/java.svg"),
        Technology("Android", "$iconBaseUrl/android.svg"),
        Technology("Git", "$iconBaseUrl/git.svg"),
        Technology("Azure DevOps", "$iconBaseUrl/azuredevops.svg"),
    )

    override fun findAll(): List<Technology> = technologies
}
