package com.carlos.models

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val url: String,
    val tags: List<String> = emptyList(),
    val featured: Boolean = false,
)
