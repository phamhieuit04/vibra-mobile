package com.example.vibramobile.core.util

import com.example.vibramobile.domain.model.Song

object SongHelper {
    fun songMatches(song: Song, currentSongId: Int?, currentSongPath: String?): Boolean {
        if (currentSongId != null && song.id == currentSongId) return true
        if (!currentSongPath.isNullOrBlank() && song.songPath == currentSongPath) return true
        return false
    }
}