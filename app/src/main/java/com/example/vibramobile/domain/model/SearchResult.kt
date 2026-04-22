package com.example.vibramobile.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class SearchResult(
    val albums: List<Playlist> = emptyList(),
    val artists: List<User> = emptyList(),
    val songs: List<Song> = emptyList()
)

