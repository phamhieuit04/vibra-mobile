package com.example.vibramobile.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.example.vibramobile.models.Response
import com.example.vibramobile.models.Song
import com.example.vibramobile.states.SongState
import com.example.vibramobile.states.UiState
import com.example.vibramobile.states.UserState
import com.example.vibramobile.ui.MediaPlayer
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val client: HttpClient
) : ViewModel() {
    private val accessToken: String = UserState.currentUser?.token.toString()

    init {
        viewModelScope.launch {
            getRecommendedSongs()
            getRecentRotationSongs()
        }
    }

    fun playSong(song: Song? = null) {
        if (song == null) return

        UiState.displayMediaPlayer.value = true
        if (SongState.currentSong.value?.id != song.id) {
            SongState.currentSong.value = song

            val url = song.song_path?.encodeURLPath()
            if (url.isNullOrBlank()) return

            MediaPlayer.replaceMediaItem(mediaItem = MediaItem.fromUri(url))
            MediaPlayer.play()
        } else {
            MediaPlayer.playOrPause()
        }
    }

    suspend fun getRecommendedSongs() {
        runCatching {
            val response: Response<List<Song>> = client.get("home/get-recommended-songs") {
                bearerAuth(accessToken)
            }.body()
            SongState.recommendedSongs.clear()
            SongState.recommendedSongs.addAll(response.data)
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
    }

    suspend fun getRecentRotationSongs() {
        runCatching {
            val response: Response<List<Song>> = client.get("home/recent-rotation") {
                bearerAuth(accessToken)
            }.body()
            SongState.recentRotationSongs.clear()
            SongState.recentRotationSongs.addAll(response.data)
        }.onFailure { exception ->
            Log.e("MyApp", exception.toString())
        }
    }
}