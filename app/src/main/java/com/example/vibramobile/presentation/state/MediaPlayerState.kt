package com.example.vibramobile.presentation.state

import com.example.vibramobile.domain.model.Song

enum class RepeatMode {
    OFF,
    ALL,
    ONE
}

data class MediaPlayerState(
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isMiniVisible: Boolean = false,
    val isFullscreenVisible: Boolean = false,
    val isLyricsVisible: Boolean = false,
    val isQueueVisible: Boolean = false,
    val queue: List<Song> = emptyList(),
    val currentIndex: Int = -1,
    val isShuffleEnabled: Boolean = false,
    val repeatMode: RepeatMode = RepeatMode.OFF
)