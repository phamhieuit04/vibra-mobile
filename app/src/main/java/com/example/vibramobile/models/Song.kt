package com.example.vibramobile.models

import kotlinx.serialization.Serializable

@Serializable
data class Song(
    var id: Int? = null,
    var name: String? = null,
    var description: String? = null,
    var lyric: String? = null,
    var thumbnail: String? = null,
    var total_played: Int? = 0,
    var status: Int? = 1,
    var price: Int? = 10000,
    var song_path: String? = null,
    var lyrics_path: String? = null,
    var thumbnail_path: String? = null,
//    var list_lyric: List<String>? = null,

    var author: User? = null,
)
