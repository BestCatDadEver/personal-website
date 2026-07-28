package com.carlos.models

import kotlinx.serialization.Serializable

@Serializable
data class Experience(
    val period: String,
    val role: String,
    val company: String,
    val project: String? = null,
    val highlights: List<String> = emptyList(),
)
