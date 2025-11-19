package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateOf
import com.example.vibramobile.models.Song

object SongState {
    var currentSong = mutableStateOf<Song?>(null)
}