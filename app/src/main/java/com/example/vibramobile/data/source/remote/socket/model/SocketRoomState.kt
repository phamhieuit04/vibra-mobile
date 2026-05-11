package com.example.vibramobile.data.source.remote.socket.model

data class SocketRoomState(
    val isPlaying: Boolean,
    val currentPosition: Long,
    val queueSongIds: List<Int>,
    val currentIndex: Int,
    val timestamp: Long
)

