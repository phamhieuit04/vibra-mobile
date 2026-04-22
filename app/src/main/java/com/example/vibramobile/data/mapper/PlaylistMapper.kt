package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.PlaylistResponseDto
import com.example.vibramobile.domain.model.Playlist

fun PlaylistResponseDto.toDomain(): Playlist {
    return Playlist(
        id = id,
        name = name,
        description = description,
        thumbnailPath = thumbnail_path,
        type = type,
        totalSong = total_song,
        price = price,
        quantity = quantity,
        author = author?.toDomain()
    )
}