package com.example.vibramobile.viewmodels

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import com.example.vibramobile.controllers.MediaPlayerController
import com.example.vibramobile.models.Song
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.flow.StateFlow

class MediaPlayerViewModel(
    private val controller: MediaPlayerController
) : ViewModel() {
    val isPlaying = controller.isPlaying
    val progress = controller.progress
    val currentSong: StateFlow<Song?> = SongState.currentSong
    private var currentSongValue: Song?
        get() = SongState.currentSong.value
        set(value) = SongState.setCurrentSong(value)

    fun playSong(song: Song? = null) {
        if (song == null) return

        UiState.setDisplayMediaPlayer(true)
        if (currentSongValue?.id != song.id) {
            currentSongValue = song

            val url = song.song_path?.encodeURLPath()
            if (url.isNullOrBlank()) return

            controller.replaceMediaItem(mediaItem = MediaItem.fromUri(url))
            controller.play()
        } else {
            controller.playOrPause()
        }
    }

    fun toggle() {
        controller.playOrPause()
    }
}