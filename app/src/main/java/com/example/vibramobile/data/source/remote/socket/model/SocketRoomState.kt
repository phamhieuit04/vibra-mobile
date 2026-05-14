package com.example.vibramobile.data.source.remote.socket.model

import com.example.vibramobile.domain.model.Song

data class SocketRoomState(
    val isPlaying: Boolean,
    val currentPosition: Long,
    val queueSongIds: List<Int>,
    val currentIndex: Int,
    val startedAt: Long?,
    val isShuffleEnabled: Boolean,
    val repeatMode: String,
    val queueSongs: List<Song> = emptyList()
)
