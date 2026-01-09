package com.example.vibramobile.states

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.Song
import com.example.vibramobile.models.User

object SongState {
    var isRecommendedSongsLoading by mutableStateOf(false)
    var isRecentRotationLoading by mutableStateOf(false)

    var currentSong = mutableStateOf<Song?>(null)
    var recommendedSongs = mutableStateListOf<Song>()
    var popularSongs = mutableStateListOf<Song>()
    var popularAlbums = mutableStateListOf<Song>()
    var recentRotationSongs = mutableStateListOf<Song>()
}