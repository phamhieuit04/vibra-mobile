package com.example.vibramobile.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibramobile.data.source.remote.socket.ISocketClient
import com.example.vibramobile.data.source.remote.socket.model.SocketRoomState
import com.example.vibramobile.domain.contract.ISongRepository
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.presentation.state.MediaPlayerState
import com.example.vibramobile.presentation.state.RepeatMode
import com.example.vibramobile.presentation.state.SessionStore
import com.example.vibramobile.presentation.state.SongState
import com.example.vibramobile.presentation.state.UserState
import io.ktor.http.encodeURLPath
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

class MediaPlayerViewModel(
    context: Context,
    private val songRepository: ISongRepository,
    private val sessionStore: SessionStore,
    private val socket: ISocketClient
) : ViewModel() {
    private val player = ExoPlayer.Builder(context).build()

    private val _uiState = MutableStateFlow(MediaPlayerState())
    val uiState = _uiState.asStateFlow()

    var currentSongValue = _uiState.value.currentSong

    private var progressJob: Job? = null
    private var lastRemoteQueueIds: List<Int> = emptyList()
    private var lastRemoteIndex: Int = -1

    init {
        socket.observeState { state ->
            viewModelScope.launch(Dispatchers.Main) {
                applyRemoteState(state)
            }
        }

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
        val userId = currentUserId() ?: return
        val songId = song.id ?: return

        _uiState.update {
            it.copy(
                isMiniVisible = true, currentSong = song
            )
        }

        socket.play(userId, songId)
    }

    fun playAll(
        songs: List<Song>,
        startIndex: Int = 0,
        prioritize: Boolean = true,
        enableShuffle: Boolean? = null
    ) {
        val userId = currentUserId() ?: return
        val normalizedNew = normalizeQueue(songs)
        if (normalizedNew.isEmpty()) return

        val songIds = normalizedNew.mapNotNull { it.id }
        if (songIds.isEmpty()) return

        socket.queueAdd(userId, songIds)

        val safeIndex = startIndex.coerceIn(0, songIds.lastIndex)
        socket.play(userId, songIds[safeIndex])
    }

    fun enqueueSong(song: Song?) {
        if (song == null) return
        enqueueSongs(listOf(song))
    }

    fun enqueueSongs(songs: List<Song>) {
        val userId = currentUserId() ?: return
        val normalized = normalizeQueue(songs)
        if (normalized.isEmpty()) return

        val songIds = normalized.mapNotNull { it.id }
        if (songIds.isEmpty()) return

        socket.queueAdd(userId, songIds)
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
        val userId = currentUserId() ?: return
        if (_uiState.value.isPlaying) {
            socket.pause(userId)
        } else {
            socket.play(userId, null)
        }
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
        val userId = currentUserId() ?: return
        socket.seek(userId, positionMs)
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

    private fun applyRemoteState(state: SocketRoomState) {
        val now = System.currentTimeMillis()
        val basePosition = state.currentPosition.coerceAtLeast(0L)
        val actualPosition = if (state.isPlaying) {
            basePosition + (now - state.timestamp).coerceAtLeast(0L)
        } else {
            basePosition
        }
        val queueSongs = resolveQueueSongs(state.queueSongIds)
        val currentIndex = state.currentIndex
        val currentSong = queueSongs.getOrNull(currentIndex)

        _uiState.update {
            it.copy(
                isPlaying = state.isPlaying,
                currentPosition = actualPosition,
                queue = queueSongs,
                currentIndex = currentIndex,
                currentSong = currentSong,
                isMiniVisible = queueSongs.isNotEmpty()
            )
        }

        syncPlayerWithRemote(
            queueSongs = queueSongs,
            queueSongIds = state.queueSongIds,
            currentIndex = currentIndex,
            positionMs = actualPosition,
            isPlaying = state.isPlaying
        )
    }

    private fun syncPlayerWithRemote(
        queueSongs: List<Song>,
        queueSongIds: List<Int>,
        currentIndex: Int,
        positionMs: Long,
        isPlaying: Boolean
    ) {
        val queueChanged = queueSongIds != lastRemoteQueueIds
        val indexChanged = currentIndex != lastRemoteIndex

        if (queueChanged || indexChanged) {
            val playableQueue = queueSongs.filter { !it.songPath.isNullOrBlank() }
            val playableIndex = queueSongs
                .take(currentIndex + 1)
                .count { !it.songPath.isNullOrBlank() }
                .minus(1)

            if (playableQueue.isEmpty() || playableIndex < 0) {
                lastRemoteQueueIds = queueSongIds
                lastRemoteIndex = currentIndex
                return
            }

            val mediaItems = playableQueue.mapNotNull { buildMediaItem(it) }
            if (mediaItems.isEmpty()) {
                lastRemoteQueueIds = queueSongIds
                lastRemoteIndex = currentIndex
                return
            }

            player.setMediaItems(mediaItems, playableIndex, positionMs.coerceAtLeast(0L))
            player.prepare()
            if (isPlaying) player.play() else player.pause()

            lastRemoteQueueIds = queueSongIds
            lastRemoteIndex = currentIndex
        } else {
            val shouldSeek = abs(player.currentPosition - positionMs) > 1500
            if (shouldSeek && positionMs >= 0L) {
                player.seekTo(positionMs)
            }

            if (isPlaying && !player.isPlaying) player.play()
            if (!isPlaying && player.isPlaying) player.pause()
        }
    }

    private fun resolveQueueSongs(songIds: List<Int>): List<Song> {
        if (songIds.isEmpty()) return emptyList()

        val sources = listOf(
            _uiState.value.queue,
            SongState.recommendedSongs.value,
            SongState.popularSongs.value,
            SongState.recentRotationSongs.value,
            SongState.songsByCategory.value,
            SongState.songsByArtist.value,
            SongState.songsByAlbum.value,
            UserState.likedSongs.value
        )

        val lookup = LinkedHashMap<Int, Song>()
        sources.asSequence()
            .flatten()
            .forEach { song ->
                val id = song.id
                if (id != null && !lookup.containsKey(id)) {
                    lookup[id] = song
                }
            }

        return songIds.mapNotNull { lookup[it] }
    }

    private fun currentUserId(): Int? = UserState.currentUser.value?.id

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