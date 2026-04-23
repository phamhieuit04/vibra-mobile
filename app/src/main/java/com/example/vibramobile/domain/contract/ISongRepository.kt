package com.example.vibramobile.domain.contract

import com.example.vibramobile.domain.model.Library
import com.example.vibramobile.domain.model.Song

interface ISongRepository {
    suspend fun getRecommendedSongs(accessToken: String): List<Song>
    suspend fun getRecentRotationSongs(accessToken: String, limit: Int = 4): List<Song>
    suspend fun getPopularSongs(accessToken: String): List<Song>
    suspend fun getSongsByCategory(categoryId: Int, accessToken: String): List<Song>

    suspend fun getLikedSongs(accessToken: String): List<Song>
}

