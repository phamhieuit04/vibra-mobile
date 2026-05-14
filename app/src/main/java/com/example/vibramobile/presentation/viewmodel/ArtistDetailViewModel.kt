package com.example.vibramobile.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.domain.contract.IUserRepository
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.SongState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ArtistDetailViewModel(
    private val songRepository: ISongRepository,
    private val playlistRepository: IPlaylistRepository,
    private val userRepository: IUserRepository
) : ViewModel() {
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    fun refresh(artistId: Int) {
        viewModelScope.launch {
            val tokenSnapshot = userRepository.getAccessToken()
            if (tokenSnapshot.isBlank()) {
                return@launch
            }

            _isRefreshing.value = true
            try {
                fetchSongsByArtist(artistId, tokenSnapshot)
                fetchAlbumsByArtist(artistId, tokenSnapshot)
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    suspend fun fetchSongsByArtist(artistId: Int, accessToken: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val result = songRepository.getSongsByArtist(artistId, accessToken)
                SongState.setSongsByArtist(result)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }

    suspend fun fetchAlbumsByArtist(artistId: Int, accessToken: String) {
        withContext(Dispatchers.IO) {
            runCatching {
                val result = playlistRepository.getAlbumsByArtist(artistId, accessToken)
                ArtistState.setAlbumsByArtist(result)
            }.onFailure { exception ->
                Log.e("MyApp", exception.toString())
            }
        }
    }
}