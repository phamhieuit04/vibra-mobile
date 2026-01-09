package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.helpers.JsonHelper
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
import kotlinx.coroutines.launch

class HomeViewModel(
    private val client: HttpClient
) : ViewModel() {
    private val accessToken: String = UserState.getCurrentUser()?.token.toString()

    init {
        viewModelScope.launch { fetchAll() }
    }

    suspend fun fetchAll() {
        getCategories()
        getRecommendedSongs()
        getRecentRotationSongs()
    }

    suspend fun getRecommendedSongs() {
        runCatching {
            SongState.recommendedSongs.clear()
            val response = client.get("home/get-recommended-songs") {
                bearerAuth(accessToken)
            }.bodyAsText()

            val result = JsonHelper.json.decodeFromString<Response<List<Song>>>(response)
            SongState.recommendedSongs.addAll(result.data)
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
    }

    suspend fun getRecentRotationSongs() {
        runCatching {
            SongState.recentRotationSongs.clear()
            val response = client.get("home/recent-rotation") {
                bearerAuth(accessToken)
                parameter(key = "limit", value = 4)
            }.bodyAsText()
            val result = JsonHelper.json.decodeFromString<Response<List<Song>>>(response)

            SongState.recentRotationSongs.addAll(result.data)
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
    }

    suspend fun getCategories() {
        runCatching {
            CategoryState.categories.clear()
            val response = client.get("home/list-category") {
                bearerAuth(accessToken)
            }.bodyAsText()
            val result = JsonHelper.json.decodeFromString<Response<List<Category>>>(response)

            CategoryState.categories.addAll(result.data)
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
    }
}