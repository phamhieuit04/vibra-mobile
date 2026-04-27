package com.example.vibramobile.presentation.state

data class MediaPlayerState(
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val currentPosition: Long = 0L,
    val duration: Long = 0L,
    val isMiniVisible: Boolean = false,
    val isFullscreenVisible: Boolean = false
)