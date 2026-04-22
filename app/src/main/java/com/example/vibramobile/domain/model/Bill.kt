package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Bill(
    var id: Int? = null,
    var userId: Int? = null,
    var orderCode: Int? = null,
    var playlistId: Int? = null,
    var status: String? = null,
    var createdAt: String? = null,

    var song: Song? = null,
    var playlist: Playlist? = null
)