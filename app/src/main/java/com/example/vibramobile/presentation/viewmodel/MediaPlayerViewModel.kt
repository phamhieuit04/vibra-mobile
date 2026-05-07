package com.example.vibramobile.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibramobile.domain.contract.IPlaylistRepository
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.presentation.state.ArtistState
import com.example.vibramobile.presentation.state.MediaPlayerState
import com.example.vibramobile.presentation.state.RepeatMode
import com.example.vibramobile.presentation.state.SessionStore
import com.example.vibramobile.presentation.state.SongState
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MediaPlayerViewModel(
    context: Context,
    private val songRepository: ISongRepository,
    private val sessionStore: SessionStore
) : ViewModel() {
    private fun accessToken() = sessionStore.currentAccessToken()

    private val player = ExoPlayer.Builder(context).build()

    private val _uiState = MutableStateFlow(MediaPlayerState())
    val uiState = _uiState.asStateFlow()

    var currentSongValue = _uiState.value.currentSong

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

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val mediaId = mediaItem?.mediaId
                val queueSnapshot = _uiState.value.queue
                val index = queueSnapshot.indexOfFirst { song ->
                    mediaIdForSong(song) == mediaId
                }
                if (index >= 0) {
                    currentSongValue = queueSnapshot[index]
                    val duration = player.duration
                    _uiState.update {
                        it.copy(
                            currentIndex = index,
                            progress = 0f,
                            currentPosition = 0L,
                            duration = if (duration > 0) duration else it.duration
                        )
                    }
                }
            }
        })
    }

    fun playSong(song: Song?) {
        if (song == null) return
        if (song.songPath.isNullOrBlank()) return

        _uiState.update {
            it.copy(
                isMiniVisible = true, currentSong = song
            )
        }

        val isSameSong = currentSongValue?.id == song.id &&
                currentSongValue?.songPath == song.songPath
        if (isSameSong) {
            toggle()
            return
        }

        val queueSnapshot = _uiState.value.queue
        val existingIndex = indexOfSong(queueSnapshot, song)
        val updatedQueue = if (existingIndex >= 0) queueSnapshot else queueSnapshot + song
        val playIndex = if (existingIndex >= 0) existingIndex else updatedQueue.lastIndex

        playFromQueue(updatedQueue, playIndex)

        val tokenSnapshot = accessToken()
        if (tokenSnapshot.isBlank()) {
            return
        }

        val artistId = currentSongValue?.author?.id ?: return
        viewModelScope.launch {
            fetchSongsByArtist(artistId, tokenSnapshot)
        }
    }

    fun playAll(
        songs: List<Song>,
        startIndex: Int = 0,
        prioritize: Boolean = true,
        enableShuffle: Boolean? = null
    ) {
        val normalizedNew = normalizeQueue(songs)
        if (normalizedNew.isEmpty()) return

        val queueSnapshot = _uiState.value.queue
        val filteredExisting = queueSnapshot.filterNot { existing ->
            normalizedNew.any { candidate -> isSameSong(candidate, existing) }
        }

        val mergedQueue = if (prioritize) {
            normalizedNew + filteredExisting
        } else {
            filteredExisting + normalizedNew
        }

        enableShuffle?.let { setShuffleEnabled(it) }

        val safeIndex = startIndex.coerceIn(0, normalizedNew.lastIndex)
        playFromQueue(mergedQueue, safeIndex)
    }

    fun enqueueSong(song: Song?) {
        if (song == null) return
        enqueueSongs(listOf(song))
    }

    fun enqueueSongs(songs: List<Song>) {
        val normalized = normalizeQueue(songs)
        if (normalized.isEmpty()) return

        val queueSnapshot = _uiState.value.queue
        val additions = normalized.filterNot { candidate ->
            queueSnapshot.any { existing -> isSameSong(existing, candidate) }
        }
        if (additions.isEmpty()) return

        val updatedQueue = queueSnapshot + additions
        _uiState.update { it.copy(queue = updatedQueue) }

        if (player.mediaItemCount > 0) {
            val mediaItems = additions.mapNotNull { buildMediaItem(it) }
            if (mediaItems.isNotEmpty()) {
                player.addMediaItems(mediaItems)
            }
        }
    }

    fun toggleShuffle() {
        setShuffleEnabled(!_uiState.value.isShuffleEnabled)
    }

    fun toggleRepeat() {
        val nextMode = when (_uiState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        setRepeatMode(nextMode)
    }

    fun skipToNext() {
        if (player.hasNextMediaItem() || player.repeatMode != Player.REPEAT_MODE_OFF) {
            player.seekToNextMediaItem()
        }
    }

    fun skipToPrevious() {
        if (player.currentPosition > 3000L) {
            player.seekTo(0)
        } else {
            player.seekToPreviousMediaItem()
        }
    }

    fun toggle() {
        if (player.isPlaying) player.pause() else player.play()
    }

    fun toggleFullscreen(value: Boolean) {
        _uiState.update { it.copy(isFullscreenVisible = value) }
    }

    fun toggleLyrics(value: Boolean) {
        _uiState.update { it.copy(isLyricsVisible = value) }
    }

    fun toggleQueue(value: Boolean) {
        _uiState.update { it.copy(isQueueVisible = value) }
    }

    fun seekTo(positionMs: Long) {
        if (positionMs < 0L) return
        player.seekTo(positionMs)

        val duration = player.duration
        if (duration > 0) {
            _uiState.update {
                it.copy(
                    progress = positionMs / duration.toFloat(),
                    currentPosition = positionMs,
                    duration = duration
                )
            }
        }
    }

    private fun startProgressUpdater() {
        if (progressJob != null) return

        progressJob = viewModelScope.launch {
            while (isActive) {
                val duration = player.duration
                val position = player.currentPosition

                if (duration > 0) {
                    _uiState.update {
                        it.copy(
                            progress = position / duration.toFloat(),
                            currentPosition = position,
                            duration = duration
                        )
                    }
                }

                delay(500)
            }
        }
    }

    private fun stopProgressUpdater() {
        progressJob?.cancel()
        progressJob = null
    }

    private fun playFromQueue(queue: List<Song>, startIndex: Int) {
        val playableQueue = normalizeQueue(queue)
        if (playableQueue.isEmpty()) return

        val mediaItems = playableQueue.mapNotNull { buildMediaItem(it) }
        if (mediaItems.isEmpty()) return

        val safeIndex = startIndex.coerceIn(0, playableQueue.lastIndex)
        currentSongValue = playableQueue[safeIndex]

        player.setMediaItems(mediaItems, safeIndex, 0L)
        player.prepare()
        player.play()

        _uiState.update {
            it.copy(
                isMiniVisible = true,
                queue = playableQueue,
                currentIndex = safeIndex,
                progress = 0f,
                currentPosition = 0L
            )
        }
    }

    private fun normalizeQueue(songs: List<Song>): List<Song> {
        return songs.filter { !it.songPath.isNullOrBlank() }
    }

    private fun buildMediaItem(song: Song): MediaItem? {
        val url = song.songPath?.encodeURLPath() ?: return null
        val mediaId = mediaIdForSong(song) ?: return null
        return MediaItem.Builder()
            .setUri(url)
            .setMediaId(mediaId)
            .build()
    }

    private fun mediaIdForSong(song: Song): String? {
        return song.id?.toString() ?: song.songPath?.encodeURLPath()
    }

    private fun indexOfSong(queue: List<Song>, song: Song): Int {
        song.id?.let { id ->
            val index = queue.indexOfFirst { it.id == id }
            if (index >= 0) return index
        }
        val path = song.songPath
        if (!path.isNullOrBlank()) {
            val index = queue.indexOfFirst { it.songPath == path }
            if (index >= 0) return index
        }
        return -1
    }

    private fun isSameSong(left: Song, right: Song): Boolean {
        if (left.id != null && right.id != null) {
            return left.id == right.id
        }
        val leftPath = left.songPath
        val rightPath = right.songPath
        return !leftPath.isNullOrBlank() && leftPath == rightPath
    }

    private fun setShuffleEnabled(enabled: Boolean) {
        player.shuffleModeEnabled = enabled
        _uiState.update { it.copy(isShuffleEnabled = enabled) }
    }

    private fun setRepeatMode(mode: RepeatMode) {
        player.repeatMode = when (mode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
        }
        _uiState.update { it.copy(repeatMode = mode) }
    }

    override fun onCleared() {
        player.release()
        super.onCleared()
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
}