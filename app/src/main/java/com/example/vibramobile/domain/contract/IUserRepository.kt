package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.User
import kotlinx.coroutines.flow.Flow

interface IUserRepository {
    suspend fun getPopularArtists(accessToken: String): List<User>

    suspend fun getFollowedArtists(accessToken: String): List<User>

    suspend fun getProfileRemote(accessToken: String): User

    suspend fun getProfileCached(accessToken: String): User?

    suspend fun refreshProfile(accessToken: String): User

    fun observeUser(): Flow<User?>

    suspend fun getUser(): User?

    suspend fun getAccessToken(): String

    suspend fun upsertUser(user: User)

    suspend fun clearUser()
}

