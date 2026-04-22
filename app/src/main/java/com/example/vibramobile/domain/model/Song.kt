package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Song(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var lyrics: String? = null,
    var thumbnail: String? = null,
    var totalPlayed: Int? = 0,
    var status: Int? = 1,
    var price: Int? = 10000,
    var songPath: String? = null,
    var lyricsPath: String? = null,
    var thumbnailPath: String? = null,
    var listLyric: List<String>? = null,
    var quantity: Int? = null,

    var author: User? = null,
)
