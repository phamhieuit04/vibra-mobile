package com.example.vibramobile.states

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.example.vibramobile.models.Category
import com.example.vibramobile.models.Playlist
import com.example.vibramobile.models.Song
import com.example.vibramobile.models.User

object SongState {
    var currentSong = mutableStateOf<Song?>(null)
    var recommendedSongs = mutableStateListOf<Song>()
    var popularSongs = mutableStateListOf<Song>()
    var popularAlbums = mutableStateListOf<Song>()
    var popularArtists = mutableStateListOf<Song>()
    var recentRotationSongs = mutableStateListOf<Song>()
}