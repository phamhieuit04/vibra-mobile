package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.data.source.remote.dto.PlaylistResponseDto
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.source.remote.dto.LibraryResponseDto
import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.domain.model.Library
import com.example.vibramobile.domain.model.Song
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class PlaylistRepository(
    private val client: HttpClient,
    private val json: Json
) : IPlaylistRepository {
    override suspend fun getPopularAlbums(accessToken: String): List<Playlist> {
        val response = client.get("home/list-album") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<PlaylistResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getMyPlaylists(accessToken: String): List<Playlist> {
        val response = client.get("library/list-playlist") {
            bearerAuth(accessToken)
            parameter("type", 2)
        }.bodyAsText()

        return json.decodeFromString<Response<List<PlaylistResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getMyAlbums(accessToken: String): List<Playlist> {
        val response = client.get("profile/list-album") {
            bearerAuth(accessToken)
        }.bodyAsText()

        val result = json.decodeFromString<Response<List<PlaylistResponseDto>>>(response)
            .data
            .map { it.toDomain() }
        result.forEach { playlist ->
            playlist.author?.name = null
        }

        return result
    }

    override suspend fun getAlbumsByArtist(artistId: Int, accessToken: String): List<Playlist> {
        val response = client.get("artist/get-artist-albums/$artistId") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<PlaylistResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getSongsByAlbum(
        albumId: Int,
        accessToken: String
    ): List<Song> {
        val response = client.get("playlist/show/$albumId") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<SongResponseDto>>>(response).data.map { it.toDomain() }
    }
}

