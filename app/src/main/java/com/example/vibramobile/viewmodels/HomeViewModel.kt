package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.Song
import com.example.vibramobile.models.User
import com.example.vibramobile.states.ArtistState
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UserState
import io.ktor.client.HttpClient
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher

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

    fun fetchAll() {
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                getCategories()
                getRecommendedSongs()
                getRecentRotationSongs()
                getPopularAlbums()
                getPopularSongs()
                getPopularArtists()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    suspend fun getRecommendedSongs() {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/get-recommended-songs") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<Song>>>(response)

                SongState.setRecommendedSongs(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getRecentRotationSongs() {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/recent-rotation") {
                    bearerAuth(accessToken)
                    parameter(key = "limit", value = 4)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<Song>>>(response)

                SongState.setRecentRotationSongs(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getCategories() {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/list-category") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<Category>>>(response)

                CategoryState.setCategories(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getPopularAlbums() {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/list-album") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<Playlist>>>(response)

                SongState.setPopularAlbums(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getPopularSongs() {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/list-song") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<Song>>>(response)

                SongState.setPopularSongs(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getPopularArtists() {
        withContext(Dispatchers.IO) {
            runCatching {
                val response = client.get("home/list-artist") {
                    bearerAuth(accessToken)
                }.bodyAsText()
                val result = json.decodeFromString<Response<List<User>>>(response)

                ArtistState.setPopularArtists(result.data)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}