package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.presentation.state.SessionStore
import com.example.vibramobile.presentation.state.UserState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LibraryViewModel(
    private val playlistRepository: IPlaylistRepository,
    private val sessionStore: SessionStore
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private fun accessToken() = sessionStore.currentAccessToken()

    init {
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            val tokenSnapshot = accessToken()
            if (tokenSnapshot.isBlank()) {
                return@launch
            }

            _isRefreshing.value = true
            try {
                fetchMyPlaylists(tokenSnapshot)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    suspend fun fetchMyPlaylists(token: String = accessToken()) {
        withContext(Dispatchers.IO) {
            runCatching {
                UserState.setMyPlaylists(playlistRepository.getMyPlaylists(token))
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}