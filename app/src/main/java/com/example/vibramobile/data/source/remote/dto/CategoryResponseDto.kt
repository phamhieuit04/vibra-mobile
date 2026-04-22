package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CategoryResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val thumbnail_path: String? = null
)

