package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail_path: String? = null
)
