package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Bill(
    var id: Int? = null,
    var user_id: Int? = null,
    var order_code: Int? = null,
    var playlist_id: Int? = null,
    var status: String? = null,
    var created_at: String? = null,

    var song: Song? = null,
    var playlist: Playlist? = null
)