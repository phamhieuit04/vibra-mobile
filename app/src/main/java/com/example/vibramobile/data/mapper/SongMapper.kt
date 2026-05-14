package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.local.entity.SongEntity
import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.domain.model.Song
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

fun SongResponseDto.toDomain(): Song {
    return Song(
        id = id,
        name = name,
        description = description,
        lyrics = normalizeLyrics(lyrics, list_lyric),
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

fun SongResponseDto.toEntity(): SongEntity? {
    val resolvedId = id ?: return null
    val resolvedLyrics = normalizeLyrics(lyrics, list_lyric)
    val resolvedAuthor = author?.name ?: author_name

    return SongEntity(
        id = resolvedId,
        name = name,
        price = price?.toDouble(),
        lyrics = resolvedLyrics,
        thumbnailPath = thumbnail_path,
        authorName = resolvedAuthor
    )
}

private fun normalizeLyrics(lyrics: JsonElement?, listLyric: List<String>?): String? {
    if (!listLyric.isNullOrEmpty()) {
        return listLyric.joinToString("\n")
    }

    return when (lyrics) {
        is JsonArray -> lyrics.joinToString("\n") { element ->
            (element as? JsonPrimitive)?.content.orEmpty()
        }.ifBlank { null }

        is JsonPrimitive -> lyrics.content
        else -> null
    }
}
