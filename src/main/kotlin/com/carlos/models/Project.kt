package com.carlos.models

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: Int,
    val name: String,
    val description: String,
    val descriptionEn: String = description,
    val url: String,
    val tags: List<String> = emptyList(),
    val featured: Boolean = false,
    val icon: String = "web",
    val imageUrl: String? = null,
)
