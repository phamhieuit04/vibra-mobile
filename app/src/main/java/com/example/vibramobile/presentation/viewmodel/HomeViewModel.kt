package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.ICategoryRepository
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.CategoryState
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.state.UserState
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

    init {
        viewModelScope.launch { fetchAll(forceRemote = false) }
    }

    fun fetchAll(forceRemote: Boolean = false) {
        viewModelScope.launch {
            val tokenSnapshot = userRepository.getAccessToken()
            if (tokenSnapshot.isBlank()) {
                return@launch
            }

            _isRefreshing.value = true
            try {
                fetchProfile(tokenSnapshot, forceRemote)
                getCategories(tokenSnapshot)
                getRecommendedSongs(tokenSnapshot)
                getRecentRotationSongs(tokenSnapshot)
                getPopularAlbums(tokenSnapshot)
                getPopularSongs(tokenSnapshot)
                getPopularArtists(tokenSnapshot)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private suspend fun fetchProfile(token: String, forceRemote: Boolean) {
        withContext(Dispatchers.IO) {
            runCatching {
                val user = if (forceRemote) {
                    userRepository.refreshProfile(token)
                } else {
                    userRepository.getProfileCached(token)
                }

                user?.let {
                    UserState.setCurrentUser(it.copy(token = null))
                }
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    private suspend fun getRecommendedSongs(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setRecommendedSongs(songRepository.getRecommendedSongs(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    private suspend fun getRecentRotationSongs(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setRecentRotationSongs(
                    songRepository.getRecentRotationSongs(accessToken = token)
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    private suspend fun getCategories(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                CategoryState.setCategories(categoryRepository.getCategories(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    private suspend fun getPopularAlbums(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setPopularAlbums(playlistRepository.getPopularAlbums(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    private suspend fun getPopularSongs(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setPopularSongs(songRepository.getPopularSongs(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    private suspend fun getPopularArtists(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                ArtistState.setPopularArtists(userRepository.getPopularArtists(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}