package com.example.vibramobile.contracts

import com.example.vibramobile.models.Library
import com.example.vibramobile.models.User

interface IUserRepository {
    suspend fun getPopularArtists(accessToken: String): List<User>

    suspend fun getFollowedArtists(accessToken: String): List<User>

    suspend fun getMyPlaylists(accessToken: String): List<Library>

    suspend fun getProfile(accessToken: String): User
}

