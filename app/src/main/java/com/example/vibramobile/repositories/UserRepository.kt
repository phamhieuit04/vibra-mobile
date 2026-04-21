package com.example.vibramobile.repositories

import com.example.vibramobile.contracts.IUserRepository
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.User
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json

class UserRepository(
    private val client: HttpClient,
    private val json: Json
) : IUserRepository {
    override suspend fun getPopularArtists(accessToken: String): List<User> {
        val response = client.get("home/list-artist") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<User>>>(response).data
    }

    override suspend fun getFollowedArtists(accessToken: String): List<User> {
        val response = client.get("library/list-artist") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<User>>>(response).data
    }


    override suspend fun getProfile(accessToken: String): User {
        val response = client.get("profile/show") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<User>>(response).data
    }
}

