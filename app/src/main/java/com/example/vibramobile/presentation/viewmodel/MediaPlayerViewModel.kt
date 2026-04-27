package com.example.vibramobile.presentation.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.presentation.state.MediaPlayerState
import com.example.vibramobile.presentation.state.SongState
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MediaPlayerViewModel(
    context: Context
) : ViewModel() {

    private val player = ExoPlayer.Builder(context).build()

    val currentSong: StateFlow<Song?> = SongState.currentSong
    private var currentSongValue: Song?
        get() = SongState.currentSong.value
        set(value) = SongState.setCurrentSong(value)

    private val _uiState = MutableStateFlow(MediaPlayerState())
    val uiState = _uiState.asStateFlow()

    private var progressJob: Job? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _uiState.update { it.copy(isPlaying = isPlaying) }
                if (isPlaying) startProgressUpdater() else stopProgressUpdater()
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    _uiState.update { it.copy(progress = 1f) }
                    stopProgressUpdater()
                }
            }
        })
    }

    fun playSong(song: Song?) {
        if (song == null) return

        _uiState.update { it.copy(isMiniVisible = true) }

        if (currentSongValue?.id != song.id) {
            currentSongValue = song

            val url = song.songPath?.encodeURLPath()
            if (url.isNullOrBlank()) return

            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            player.play()

            _uiState.update { it.copy(progress = 0f) }
        } else {
            toggle()
        }
    }

    fun toggle() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun toggleFullscreen(value: Boolean) {
        _uiState.update { it.copy(isFullscreenVisible = value) }
    }

    private fun startProgressUpdater() {
        if (progressJob != null) return

        progressJob = viewModelScope.launch {
            while (isActive) {
                val duration = player.duration
                if (duration > 0) {
                    _uiState.update { it.copy(progress = player.currentPosition / duration.toFloat()) }
                }
                delay(500)
            }
        }
    }

    private fun stopProgressUpdater() {
        progressJob?.cancel()
        progressJob = null
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}