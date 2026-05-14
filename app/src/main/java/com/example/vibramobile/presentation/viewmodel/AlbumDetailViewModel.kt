package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.presentation.state.SongState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AlbumDetailViewModel(
    private val playlistRepository: IPlaylistRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh(albumId: Int) {
        viewModelScope.launch {
            val tokenSnapshot = userRepository.getAccessToken()
            if (tokenSnapshot.isBlank()) {
                return@launch
            }

            _isRefreshing.value = true
            try {
                fetchSongsByAlbum(albumId, tokenSnapshot)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    suspend fun fetchSongsByAlbum(albumId: Int, token: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                SongState.setSongsByAlbum(
                    playlistRepository.getSongsByAlbum(albumId, token)
                )
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}