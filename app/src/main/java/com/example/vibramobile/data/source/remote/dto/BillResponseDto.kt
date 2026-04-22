package com.example.vibramobile.data.source.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class BillResponseDto(
    val id: Int? = null,
    val user_id: Int? = null,
    val order_code: Int? = null,
    val playlist_id: Int? = null,
    val status: String? = null,
    val created_at: String? = null,
    val song: SongResponseDto? = null,
    val playlist: PlaylistResponseDto? = null
)

