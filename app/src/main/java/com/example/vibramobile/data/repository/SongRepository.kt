package com.example.vibramobile.data.repository

import android.util.Log
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.source.remote.dto.LibraryResponseDto
import com.example.vibramobile.domain.model.Song
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class SongRepository(
    private val client: HttpClient,
    private val json: Json
) : ISongRepository {
    override suspend fun getRecommendedSongs(accessToken: String): List<Song> {
        val response = client.get("home/get-recommended-songs") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<SongResponseDto>>(response)
            .data
            .songs
            .orEmpty()
            .map { it.toDomain() }
    }

    override suspend fun getRecentRotationSongs(accessToken: String, limit: Int): List<Song> {
        val response = client.get("home/recent-rotation") {
            bearerAuth(accessToken)
            parameter(key = "limit", value = limit)
        }.bodyAsText()

        return json.decodeFromString<Response<List<SongResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getPopularSongs(accessToken: String): List<Song> {
        val response = client.get("home/list-song") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<SongResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getSongsByCategory(categoryId: Int, accessToken: String): List<Song> {
        val response = client.get("category/show/$categoryId") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<SongResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getLikedSongs(accessToken: String): List<Song> {
        val response = client.get("library/list-song") {
            bearerAuth(accessToken)
        }.bodyAsText()

        val result = json.decodeFromString<Response<List<LibraryResponseDto>>>(response)

        val songs = mutableListOf<Song>()
        result.data.forEach { item ->
            item.song?.toDomain()?.let { songs.add(it) }
        }

        return songs
    }

    override suspend fun getSongsByArtist(artistId: Int, accessToken: String): List<Song> {
        val response = client.get("artist/get-artist-songs/$artistId") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<SongResponseDto>>>(response).data.map { it.toDomain() }
    }
}

