package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class SearchResultResponseDto(
    val albums: List<PlaylistResponseDto>? = null,
    val artists: List<UserResponseDto>? = null,
    val songs: List<SongResponseDto>? = null
)

