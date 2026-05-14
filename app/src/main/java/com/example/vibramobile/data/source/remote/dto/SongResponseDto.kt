package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class SongResponseDto(
    val songs: List<SongResponseDto>? = null,

    val id: Int? = null,
    val name: String? = null,
    val description: String? = null,
    val lyrics: JsonElement? = null,
    val thumbnail: String? = null,
    val total_played: Int? = null,
    val status: Int? = null,
    val price: Int? = null,
    val song_path: String? = null,
    val lyrics_path: String? = null,
    val thumbnail_path: String? = null,
    val list_lyric: List<String>? = null,
    val quantity: Int? = null,
    val author_name: String? = null,

    val author: UserResponseDto? = null,
    val playlist: PlaylistResponseDto? = null,
    val category: CategoryResponseDto? = null
)
