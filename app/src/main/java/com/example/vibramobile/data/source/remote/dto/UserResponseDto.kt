package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val email: String? = null,
    val avatar: String? = null,
    val followers: Int? = null,
    val token: String? = null,
    val avatar_path: String? = null,
    val libraries: List<LibraryResponseDto>? = null
)

