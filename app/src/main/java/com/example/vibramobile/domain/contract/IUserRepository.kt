package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.User

interface IUserRepository {
    suspend fun getPopularArtists(accessToken: String): List<User>

    suspend fun getFollowedArtists(accessToken: String): List<User>


    suspend fun getProfile(accessToken: String): User
}

