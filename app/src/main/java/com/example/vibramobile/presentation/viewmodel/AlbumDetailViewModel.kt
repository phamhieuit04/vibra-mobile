package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.SessionStore
import com.example.vibramobile.presentation.state.SongState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(
    private val playlistRepository: IPlaylistRepository,
    private val sessionStore: SessionStore
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private fun accessToken() = sessionStore.currentAccessToken()

    fun refresh(albumId: Int) {
        viewModelScope.launch {
            val tokenSnapshot = accessToken()
            if (tokenSnapshot.isBlank()) {
                return@launch
            }

            _isRefreshing.value = true
            try {
                fetchSongsByAlbum(albumId)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    suspend fun fetchSongsByAlbum(albumId: Int) {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setSongsByAlbum(
                    playlistRepository.getSongsByAlbum(albumId, accessToken())
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}