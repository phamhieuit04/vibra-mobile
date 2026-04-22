package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnailPath: String? = null,
    var type: Int? = null,
    var totalSong: Int? = null,
    var price: Int? = null,
    var quantity: Int? = null,

    var author: User? = null,
)
