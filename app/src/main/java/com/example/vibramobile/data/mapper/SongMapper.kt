package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.domain.model.Song

fun SongResponseDto.toDomain(): Song {
    return Song(
        id = id,
        name = name,
        description = description,
        lyrics = lyrics,
        thumbnail = thumbnail,
        totalPlayed = total_played,
        status = status,
        price = price,
        songPath = song_path,
        lyricsPath = lyrics_path,
        thumbnailPath = thumbnail_path,
        listLyric = list_lyric,
        quantity = quantity,
        author = author?.toDomain()
    )
}