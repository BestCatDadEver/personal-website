package com.carlos.models

import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    val period: String,
    val role: String,
    val roleEn: String = role,
    val company: String,
    val project: String? = null,
    val projectEn: String? = project,
    val highlights: List<String> = emptyList(),
    val highlightsEn: List<String> = highlights,
)
