package com.example.vibramobile.contracts

import com.example.vibramobile.models.Playlist

interface IPlaylistRepository {
    suspend fun getPopularAlbums(accessToken: String): List<Playlist>
}

