package com.example.vibramobile.data.repository

import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.data.source.remote.dto.Response
import com.example.vibramobile.data.source.remote.dto.UserResponseDto
import com.example.vibramobile.data.mapper.toDomain
import com.example.vibramobile.data.mapper.toEntity
import com.example.vibramobile.data.source.local.dao.UserDao
import com.example.vibramobile.data.source.remote.dto.LibraryResponseDto
import com.example.vibramobile.domain.model.User
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

class UserRepository(
    private val client: HttpClient,
    private val json: Json,
    private val userDao: UserDao
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

        val followedArtistsResponse =
            json.decodeFromString<Response<List<LibraryResponseDto>>>(response)
        val followedArtists = mutableListOf<User>()
        followedArtistsResponse.data.forEach { item ->
            item.artist?.toDomain()?.let { followedArtists.add(it) }
        }

        return followedArtists
    }


    override suspend fun getProfileRemote(accessToken: String): User {
        val response = client.get("profile/show") {
            bearerAuth(accessToken)
        }.bodyAsText()

        return json.decodeFromString<Response<UserResponseDto>>(response).data.toDomain()
    }

    override suspend fun getProfileCached(accessToken: String): User? {
        val cached = getUser()
        if (cached != null) {
            if (cached.token.isNullOrBlank() && accessToken.isNotBlank()) {
                val updated = cached.copy(token = accessToken)
                upsertUser(updated)
                return updated
            }

            return cached
        }

        return refreshProfile(accessToken)
    }

    override suspend fun refreshProfile(accessToken: String): User {
        val user = getProfileRemote(accessToken)
        val updated = user.copy(token = accessToken)
        upsertUser(updated)
        return updated
    }

    override fun observeUser(): Flow<User?> {
        return userDao.getUser().map { it?.toDomain() }
    }

    override suspend fun getUser(): User? {
        return withContext(Dispatchers.IO) {
            userDao.getUserOnce()?.toDomain()
        }
    }

    override suspend fun getAccessToken(): String {
        return withContext(Dispatchers.IO) {
            userDao.getUserOnce()?.token.orEmpty()
        }
    }

    override suspend fun upsertUser(user: User) {
        withContext(Dispatchers.IO) {
            userDao.insert(user.toEntity())
        }
    }

    override suspend fun clearUser() {
        withContext(Dispatchers.IO) {
            userDao.clear()
        }
    }
}

