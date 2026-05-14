package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class PlaylistResponseDto(
    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val thumbnail_path: String? = null,
    val type: Int? = null,
    val total_song: Int? = null,
    val price: Int? = null,
    val quantity: Int? = null,
    val author: UserResponseDto? = null,
    val songs: List<SongResponseDto>? = null
)
