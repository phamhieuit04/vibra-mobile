package com.example.vibramobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Library(
    var id: Int? = null,
    var user_id: Int? = null,
    var playlist_id: Int? = null,
    var artist_id: Int? = null,
    var song_id: Int? = null
) {
}