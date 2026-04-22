package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.dto.UserResponseDto
import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.domain.model.User
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

        return json.decodeFromString<Response<List<UserResponseDto>>>(response).data.map { it.toDomain() }
    }

    override suspend fun getFollowedArtists(accessToken: String): List<User> {
        val response = client.get("library/list-artist") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<List<UserResponseDto>>>(response).data.map { it.toDomain() }
    }


    override suspend fun getProfile(accessToken: String): User {
        val response = client.get("profile/show") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<UserResponseDto>>(response).data.toDomain()
    }
}

