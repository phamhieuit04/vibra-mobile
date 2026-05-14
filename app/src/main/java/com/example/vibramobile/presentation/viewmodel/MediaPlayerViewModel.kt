package com.example.vibramobile.presentation.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.example.vibramobile.data.source.remote.socket.ISocketClient
import com.example.vibramobile.data.source.remote.socket.model.SocketRoomState
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.presentation.state.MediaPlayerState
import com.example.vibramobile.presentation.state.RepeatMode
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
import kotlinx.coroutines.flow.merge
import kotlin.math.abs

@UnstableApi
class MediaPlayerViewModel(
    context: Context,
    private val socket: ISocketClient
) : ViewModel() {
    private val player = ExoPlayer.Builder(context)
        .setLoadControl(
            androidx.media3.exoplayer.DefaultLoadControl.Builder()
                .setBufferDurationsMs(
                    60_000,
                    180_000,
                    1_500,
                    5_000
                )
                .build()
        )
        .build()

    private val _uiState = MutableStateFlow(MediaPlayerState())
    val uiState = _uiState.asStateFlow()

    var currentSongValue = _uiState.value.currentSong

    private var progressJob: Job? = null

    // TODO: move all last* var to Room database
    private var lastRemoteQueueIds: List<Int> = emptyList()
    private var lastRemoteIndex: Int = -1
    private var lastServerPositionMs: Long = 0L
    private var lastServerStartedAtMs: Long? = null
    private var lastServerIsPlaying: Boolean = false
    private var lastSocketState: SocketRoomState? = null
    private var lastSeekPositionMs: Long = -1L
    private var lastSeekSentAtMs: Long = 0L
    private var isSeeking: Boolean = false
    private var pendingRemoteState: SocketRoomState? = null

    init {
        socket.connect(UserState.currentUser.value?.id!!)

        socket.observeState { state ->
            viewModelScope.launch(Dispatchers.Main) {
                applyRemoteState(state)
            }
        }

        viewModelScope.launch(Dispatchers.Main) {
            merge(
                SongState.recommendedSongs,
                SongState.popularSongs,
                SongState.recentRotationSongs,
                SongState.songsByCategory,
                SongState.songsByArtist,
                SongState.songsByAlbum,
                UserState.likedSongs
            ).collect { songs ->
                if (songs.isNotEmpty()) {
                    val pending = pendingRemoteState ?: return@collect
                    pendingRemoteState = null
                    applyRemoteState(pending)
                }
            }
        }

        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                if (isPlaying) {
                    isSeeking = false
                }
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    val userId = currentUserId() ?: return
                    socket.trackEnded(userId)
                }
            }

            override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                val mediaId = mediaItem?.mediaId
                val queueSnapshot = _uiState.value.queue
                val index = queueSnapshot.indexOfFirst { song ->
                    mediaIdForSong(song) == mediaId
                }
                if (index >= 0) {
                    val transitionedSong = queueSnapshot[index]
                    currentSongValue = transitionedSong
                    val duration = player.duration
                    val isSameSong = transitionedSong.id != null &&
                            transitionedSong.id == _uiState.value.currentSong?.id
                    _uiState.update {
                        it.copy(
                            currentIndex = index,
                            currentSong = transitionedSong,
                            progress = if (isSameSong) it.progress else 0f,
                            currentPosition = if (isSameSong) it.currentPosition else 0L,
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

        if (song.id == _uiState.value.currentSong?.id) {
            lastRemoteIndex = -1
        }

        _uiState.update {
            it.copy(isMiniVisible = true, currentSong = song)
        }

        socket.play(userId, songId)
    }

    fun playAll(
        songs: List<Song>,
        startIndex: Int = 0
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

    fun retryPendingRemoteState() {
        val pending = pendingRemoteState ?: return
        pendingRemoteState = null
        viewModelScope.launch(Dispatchers.Main) {
            applyRemoteState(pending)
        }
    }

    fun toggleShuffle() {
        val userId = currentUserId() ?: return
        socket.shuffle(userId, !_uiState.value.isShuffleEnabled)
    }

    fun toggleRepeat() {
        val userId = currentUserId() ?: return
        val nextMode = when (_uiState.value.repeatMode) {
            RepeatMode.OFF -> RepeatMode.ALL
            RepeatMode.ALL -> RepeatMode.ONE
            RepeatMode.ONE -> RepeatMode.OFF
        }
        socket.repeat(userId, nextMode.name)
    }

    fun skipToNext() {
        val userId = currentUserId() ?: return
        socket.next(userId)
    }

    fun skipToPrevious() {
        val userId = currentUserId() ?: return
        socket.previous(userId)
    }

    fun toggle() {
        val userId = currentUserId() ?: return
        if (_uiState.value.isPlaying) {
            val positionMs = player.currentPosition.coerceAtLeast(0L)
            socket.pause(userId, positionMs)
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
        val duration = _uiState.value.duration
        val safePosition = positionMs.coerceAtLeast(0L)
        val now = System.currentTimeMillis()
        lastServerPositionMs = safePosition
        lastServerStartedAtMs = if (_uiState.value.isPlaying) now else null
        lastServerIsPlaying = _uiState.value.isPlaying
        lastSeekPositionMs = safePosition
        lastSeekSentAtMs = now
        isSeeking = true

        _uiState.update {
            it.copy(
                currentPosition = safePosition,
                progress = if (duration > 0L) safePosition / duration.toFloat() else it.progress
            )
        }

        player.seekTo(safePosition)
        socket.seek(userId, safePosition)
    }

    private fun startProgressUpdater() {
        if (progressJob != null) return

        progressJob = viewModelScope.launch {
            while (isActive) {
                if (!isSeeking) {
                    val position = player.currentPosition
                    val duration = player.duration

                    if (duration > 0) {
                        _uiState.update {
                            it.copy(
                                progress = position / duration.toFloat(),
                                currentPosition = position,
                                duration = duration
                            )
                        }
                    } else {
                        _uiState.update {
                            it.copy(currentPosition = position)
                        }
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

    private fun applyRemoteState(state: SocketRoomState) {
        lastSocketState = state
        lastServerPositionMs = state.currentPosition.coerceAtLeast(0L)
        lastServerStartedAtMs = state.startedAt
        lastServerIsPlaying = state.isPlaying

        val resolvedSongs = state.queueSongs.ifEmpty {
            resolveQueueSongs(state.queueSongIds)
        }

        if (resolvedSongs.isEmpty() && state.queueSongIds.isNotEmpty()) {
            pendingRemoteState = state
            return
        }

        val queueSongs = resolvedSongs
        val currentIndex = state.currentIndex
        val currentSong = queueSongs.getOrNull(currentIndex)

        val repeatMode = runCatching {
            RepeatMode.valueOf(state.repeatMode)
        }.getOrDefault(RepeatMode.OFF)

        val previousSongId = _uiState.value.currentSong?.id
        val currentSongId = currentSong?.id

        val isSameTrack =
            previousSongId != null &&
                    previousSongId == currentSongId

        val isTrackRestart = isSameTrack &&
                state.currentPosition == 0L &&
                state.isPlaying &&
                state.startedAt != null &&
                (System.currentTimeMillis() - state.startedAt) < 3_000L

        val isRecentSeek = lastSeekPositionMs >= 0L &&
                (System.currentTimeMillis() - lastSeekSentAtMs) < 3_000L &&
                abs(state.currentPosition - lastSeekPositionMs) < 500L

        val actualPosition =
            if (isSameTrack && !isTrackRestart) {
                computeServerPosition(state)
            } else {
                state.currentPosition.coerceAtLeast(0L)
            }

        val safePosition = when {
            isTrackRestart -> actualPosition
            isRecentSeek -> {
                lastSeekPositionMs = -1L
                actualPosition
            }

            isSameTrack -> maxOf(actualPosition, _uiState.value.currentPosition)
            else -> actualPosition
        }

        _uiState.update {
            it.copy(
                isPlaying = state.isPlaying,

                currentPosition = safePosition,

                progress =
                    if (it.duration > 0L) {
                        safePosition / it.duration.toFloat()
                    } else {
                        0f
                    },

                queue = queueSongs,
                currentIndex = currentIndex,
                currentSong = currentSong,

                isMiniVisible = currentSong != null,

                isShuffleEnabled = state.isShuffleEnabled,
                repeatMode = repeatMode
            )
        }

        player.shuffleModeEnabled =
            state.isShuffleEnabled

        player.repeatMode = when (repeatMode) {
            RepeatMode.OFF -> Player.REPEAT_MODE_OFF
            RepeatMode.ALL -> Player.REPEAT_MODE_ALL
            RepeatMode.ONE -> Player.REPEAT_MODE_ONE
        }

        if (state.isPlaying) {
            startProgressUpdater()
        } else {
            stopProgressUpdater()
        }

        syncPlayerWithRemote(
            queueSongs = queueSongs,
            queueSongIds = state.queueSongIds,
            currentIndex = currentIndex,
            positionMs = safePosition,
            isPlaying = state.isPlaying,
            forceSeek = isTrackRestart
        )
    }

    private fun syncPlayerWithRemote(
        queueSongs: List<Song>,
        queueSongIds: List<Int>,
        currentIndex: Int,
        positionMs: Long,
        isPlaying: Boolean,
        forceSeek: Boolean = false
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
                return
            }

            val mediaItems = playableQueue.mapNotNull { buildMediaItem(it) }
            if (mediaItems.isEmpty()) {
                return
            }

            player.setMediaItems(mediaItems, playableIndex, positionMs.coerceAtLeast(0L))
            player.prepare()
            if (isPlaying) player.play() else player.pause()

            lastRemoteQueueIds = queueSongIds
            lastRemoteIndex = currentIndex
        } else {
            val shouldSeek = forceSeek || abs(player.currentPosition - positionMs) > 1500
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

    private fun computeServerPosition(state: SocketRoomState): Long {
        val basePosition = state.currentPosition.coerceAtLeast(0L)
        val startedAt = state.startedAt
        if (state.isPlaying && startedAt != null) {
            return basePosition + (System.currentTimeMillis() - startedAt).coerceAtLeast(0L)
        }
        return basePosition
    }

    private fun currentUserId(): Int? = UserState.currentUser.value?.id

    override fun onCleared() {
        player.release()
        super.onCleared()
    }
}