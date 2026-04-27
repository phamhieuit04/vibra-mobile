package com.example.vibramobile.presentation.state

data class MediaPlayerState(
    val isPlaying: Boolean = false,
    val progress: Float = 0f,
    val isMiniVisible: Boolean = false,
    val isFullscreenVisible: Boolean = false,
)