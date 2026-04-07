package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.contracts.ICategoryRepository
import com.example.vibramobile.contracts.IPlaylistRepository
import com.example.vibramobile.contracts.ISongRepository
import com.example.vibramobile.contracts.IUserRepository
import com.example.vibramobile.states.ArtistState
import com.example.vibramobile.states.CategoryState
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(
    private val songRepository: ISongRepository,
    private val categoryRepository: ICategoryRepository,
    private val playlistRepository: IPlaylistRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private fun accessToken(): String = UserState.currentUser.value?.token.orEmpty()

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
                SongState.setRecommendedSongs(songRepository.getRecommendedSongs(accessToken()))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getRecentRotationSongs() {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setRecentRotationSongs(
                    songRepository.getRecentRotationSongs(accessToken = accessToken())
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getCategories() {
        withContext(Dispatchers.IO) {
            runCatching {
                CategoryState.setCategories(categoryRepository.getCategories(accessToken()))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getPopularAlbums() {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setPopularAlbums(playlistRepository.getPopularAlbums(accessToken()))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getPopularSongs() {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setPopularSongs(songRepository.getPopularSongs(accessToken()))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun getPopularArtists() {
        withContext(Dispatchers.IO) {
            runCatching {
                ArtistState.setPopularArtists(userRepository.getPopularArtists(accessToken()))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}