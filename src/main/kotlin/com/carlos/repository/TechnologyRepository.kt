package com.carlos.repository

import com.carlos.models.Technology

interface TechnologyRepository {
    fun findAll(): List<Technology>
}

class InMemoryTechnologyRepository : TechnologyRepository {

    private val iconBaseUrl = "https://cdn.jsdelivr.net/npm/simple-icons@latest/icons"

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
