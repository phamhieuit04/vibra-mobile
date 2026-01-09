package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateOf

object UiState {
    private var displayNavigationBar = mutableStateOf(false)
    fun getDisplayNavigationBar(): Boolean {
        return displayNavigationBar.value
    }

    fun setDisplayNavigationBar(value: Boolean) {
        displayNavigationBar.value = value
    }

    private var displayMediaPlayer = mutableStateOf(false)
    fun getDisplayMediaPlayer(): Boolean {
        return displayMediaPlayer.value
    }

    fun setDisplayMediaPlayer(value: Boolean) {
        displayMediaPlayer.value = value
    }

    private var displaySongDetail = mutableStateOf(false)
    fun getDisplaySongDetail(): Boolean {
        return displaySongDetail.value
    }

    fun setDisplaySongDetail(value: Boolean) {
        displaySongDetail.value = value
    }

    private var displayQueuePlaylist = mutableStateOf(false)
    fun getDisplayQueuePlaylist(): Boolean {
        return displayQueuePlaylist.value
    }

    fun setDisplayQueuePlaylist(value: Boolean) {
        displayQueuePlaylist.value = value
    }
}