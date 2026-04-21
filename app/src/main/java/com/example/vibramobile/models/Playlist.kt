package com.example.vibramobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Playlist(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var thumbnail_path: String? = null,
    var type: Int? = null,
    var total_song: Int? = null,
    var price: Int? = null,
    var quantity: Int? = null,

    var author: User? = null,
)
