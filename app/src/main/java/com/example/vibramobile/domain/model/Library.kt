package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Library(
    var id: Int? = null,
    var userId: Int? = null,
    var playlistId: Int? = null,
    var artistId: Int? = null,
    var songId: Int? = null
) {
}