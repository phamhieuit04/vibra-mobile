package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.Library
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.Song

interface IPlaylistRepository {
    suspend fun getPopularAlbums(accessToken: String): List<Playlist>

    suspend fun getMyPlaylists(accessToken: String): List<Playlist>

    suspend fun getMyAlbums(accessToken: String): List<Playlist>

    suspend fun getAlbumsByArtist(artistId: Int, accessToken: String): List<Playlist>

    suspend fun getSongsByAlbum(albumId: Int, accessToken: String): List<Song>
}

