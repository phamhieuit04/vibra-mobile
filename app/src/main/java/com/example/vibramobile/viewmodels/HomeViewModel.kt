package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.Song
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UserState
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class HomeViewModel(
    private val client: HttpClient,
    private val json: Json
) : ViewModel() {
    private val accessToken: String = UserState.getCurrentUser()?.token.toString()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        viewModelScope.launch { fetchAll() }
    }

    suspend fun fetchAll() = coroutineScope {
        _isRefreshing.value = true
        try {
            awaitAll(
                async { getCategories() },
                async { getRecommendedSongs() },
                async { getRecentRotationSongs() }
            )
        } finally {
            _isRefreshing.value = false
        }
    }

    suspend fun getRecommendedSongs() {
        SongState.isRecommendedSongsLoading = true
        runCatching {
            val response = client.get("home/get-recommended-songs") {
                bearerAuth(accessToken)
            }.bodyAsText()

            val result = json.decodeFromString<Response<List<Song>>>(response)
            SongState.recommendedSongs.apply {
                clear()
                addAll(result.data)
            }
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
        SongState.isRecommendedSongsLoading = false
    }

    suspend fun getRecentRotationSongs() {
        SongState.isRecentRotationLoading = true
        runCatching {
            val response = client.get("home/recent-rotation") {
                bearerAuth(accessToken)
                parameter(key = "limit", value = 4)
            }.bodyAsText()
            val result = json.decodeFromString<Response<List<Song>>>(response)

            SongState.recentRotationSongs.apply {
                clear()
                addAll(result.data)
            }
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
        SongState.isRecentRotationLoading = false
    }

    suspend fun getCategories() {
        CategoryState.isCategoriesLoading = true
        runCatching {
            val response = client.get("home/list-category") {
                bearerAuth(accessToken)
            }.bodyAsText()
            val result = json.decodeFromString<Response<List<Category>>>(response)

            CategoryState.categories.apply {
                clear()
                addAll(result.data)
            }
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
        CategoryState.isCategoriesLoading = false
    }
}