package com.example.vibramobile.repositories

import com.example.vibramobile.contracts.ISongRepository
import com.example.vibramobile.models.RecommendedSongs
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.Song
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

        return json.decodeFromString<Response<RecommendedSongs>>(response).data.songs
    }

    override suspend fun getRecentRotationSongs(accessToken: String, limit: Int): List<Song> {
        val response = client.get("home/recent-rotation") {
            bearerAuth(accessToken)
            parameter(key = "limit", value = limit)
        }.bodyAsText()

        return json.decodeFromString<Response<List<Song>>>(response).data
    }

    override suspend fun getPopularSongs(accessToken: String): List<Song> {
        val response = client.get("home/list-song") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<Song>>>(response).data
    }

    override suspend fun getSongsByCategory(categoryId: Int, accessToken: String): List<Song> {
        val response = client.get("category/show/$categoryId") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<Song>>>(response).data
    }
}

