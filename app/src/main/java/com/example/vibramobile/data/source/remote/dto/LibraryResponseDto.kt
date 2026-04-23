package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class LibraryResponseDto(
    val id: Int? = null,
    val user_id: Int? = null,
    val playlist_id: Int? = null,
    val artist_id: Int? = null,
    val song_id: Int? = null,

    val song: SongResponseDto? = null,
    val playlist: PlaylistResponseDto? = null,
    val artist: UserResponseDto? = null
)

