package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.LibraryResponseDto
import com.example.vibramobile.domain.model.Library

fun LibraryResponseDto.toDomain(): Library {
    return Library(
        id = id,
        userId = user_id,
        playlistId = playlist_id,
        artistId = artist_id,
        songId = song_id
    )
}