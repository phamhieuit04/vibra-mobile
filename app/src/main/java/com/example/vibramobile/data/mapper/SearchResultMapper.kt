package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.remote.dto.SearchResultResponseDto
import com.example.vibramobile.domain.model.SearchResult
import kotlin.collections.map
import kotlin.collections.orEmpty

fun SearchResultResponseDto.toDomain(): SearchResult {
    return SearchResult(
        albums = albums.orEmpty().map { it.toDomain() },
        artists = artists.orEmpty().map { it.toDomain() },
        songs = songs.orEmpty().map { it.toDomain() }
    )
}