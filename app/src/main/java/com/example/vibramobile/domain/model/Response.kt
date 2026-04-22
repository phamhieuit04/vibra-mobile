package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Response<T>(
    var code: Int? = null,
    var data: T,
    var message: String? = null
)

@Serializable
data class RecommendedSongs(
    var songs: List<Song>
)

@Serializable
data class SearchResult(
    var albums: List<Playlist>,
    var artists: List<User>,
    var songs: List<Song>
)