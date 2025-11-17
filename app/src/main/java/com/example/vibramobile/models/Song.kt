package com.example.vibramobile.models

data class Song(
    var id: Int,
    var name: String,
    var description: String,
    var lyric: String,
    var thumbnail: String?,
    var total_played: Int = 0,
    var status: Int = 1,
    var price: Int = 10000,

    var author: User,
)
