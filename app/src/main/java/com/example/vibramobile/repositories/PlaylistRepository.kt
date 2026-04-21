package com.example.vibramobile.repositories

import com.example.vibramobile.contracts.IPlaylistRepository
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.Response
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

        return json.decodeFromString<Response<List<Playlist>>>(response).data
    }

    override suspend fun getMyPlaylists(accessToken: String): List<Playlist> {
        val response = client.get("library/list-playlist") {
            bearerAuth(accessToken)
            parameter("type", 2)
        }.bodyAsText()

        return json.decodeFromString<Response<List<Playlist>>>(response).data
    }

    override suspend fun getMyAlbums(accessToken: String): List<Playlist> {
        val response = client.get("profile/list-album") {
            bearerAuth(accessToken)
        }.bodyAsText()

        val result = json.decodeFromString<Response<List<Playlist>>>(response).data
        result.forEach { playlist ->
            playlist.author?.name = null
        }

        return result
    }
}

