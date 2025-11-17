package com.example.vibramobile.models

data class Playlist(
    var id: Int,
    var name: String,
    var description: String,
    var thumbnail: String,
    var type: Int,
    var total_song: Int,
    var price: Int,

    var author: User,
)
