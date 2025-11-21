package com.example.vibramobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Category(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail_path: String? = null
)
