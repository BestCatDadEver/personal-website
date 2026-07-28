package com.carlos.models

import kotlinx.serialization.Serializable

@Serializable
data class Technology(
    val name: String,
    val iconUrl: String,
)
