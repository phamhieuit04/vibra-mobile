package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.IBillRepository
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.presentation.state.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ProfileViewModel(
    private val userRepository: IUserRepository,
    private val playlistRepository: IPlaylistRepository,
    private val billRepository: IBillRepository
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    init {
        refresh(forceRemote = false)
    }

    fun refresh(forceRemote: Boolean = true) {
        viewModelScope.launch {
            val tokenSnapshot = userRepository.getAccessToken()
            if (tokenSnapshot.isBlank()) {
                return@launch
            }

            _isRefreshing.value = true
            try {
                fetchProfile(tokenSnapshot, forceRemote)
                fetchMyPlaylists(tokenSnapshot)
                fetchFollowedArtists(tokenSnapshot)
                fetchMyAlbums(tokenSnapshot)
                fetchPaymentHistory(tokenSnapshot)
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

    suspend fun fetchFollowedArtists(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                UserState.setFollowedArtists(userRepository.getFollowedArtists(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun fetchMyAlbums(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                UserState.setMyAlbums(playlistRepository.getMyAlbums(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun fetchPaymentHistory(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                UserState.setPaymentHistory(billRepository.getPaymentHistory(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun fetchMyPlaylists(token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                UserState.setMyPlaylists(playlistRepository.getMyPlaylists(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}