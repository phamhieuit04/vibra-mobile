package com.example.vibramobile.controllers

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class MediaPlayerController(
    context: Context
) {
    private val player = ExoPlayer.Builder(context).build()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying = _isPlaying.asStateFlow()

    private val _progress = MutableStateFlow(0f)
    val progress = _progress.asStateFlow()

    private var progressJob: Job? = null

    init {
        player.addListener(object : Player.Listener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                _isPlaying.value = isPlaying
                if (isPlaying) startProgressUpdater()
                else stopProgressUpdater()
            }

            override fun onPlaybackStateChanged(state: Int) {
                if (state == Player.STATE_ENDED) {
                    _progress.value = 1f
                    stopProgressUpdater()
                }
            }
        })
    }

    fun replaceMediaItem(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
        _progress.value = 0f
    }

    fun play() = player.play()
    fun pause() = player.pause()
    fun playOrPause() {
        if (player.isPlaying) pause() else play()
    }

    private fun startProgressUpdater() {
        if (progressJob != null) return

        progressJob = CoroutineScope(Dispatchers.Main).launch {
            while (isActive) {
                val duration = player.duration
                if (duration > 0) {
                    _progress.value =
                        player.currentPosition / duration.toFloat()
                }
                delay(500)
            }
        }
    }

    private fun stopProgressUpdater() {
        progressJob?.cancel()
        progressJob = null
    }
}