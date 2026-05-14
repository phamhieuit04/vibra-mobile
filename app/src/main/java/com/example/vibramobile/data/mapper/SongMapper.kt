package com.example.vibramobile.data.mapper

import com.example.vibramobile.data.source.local.entity.SongEntity
import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonPrimitive

fun SongResponseDto.toDomain(): Song {
    val resolvedLyrics = normalizeLyrics(lyrics, list_lyric)
    val resolvedListLyric = list_lyric?.takeIf { it.isNotEmpty() }
        ?: resolvedLyrics?.split("\n")?.filter { it.isNotBlank() }

    return Song(
        id = id,
        name = name,
        description = description,
        lyrics = resolvedLyrics,
        thumbnail = thumbnail,
        totalPlayed = total_played,
        status = status,
        price = price,
        songPath = song_path,
        lyricsPath = lyrics_path,
        thumbnailPath = thumbnail_path,
        listLyric = resolvedListLyric,
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
        songPath = song_path,
        thumbnailPath = thumbnail_path,
        authorName = resolvedAuthor
    )
}

fun SongEntity.toDomain(): Song {
    val resolvedListLyric = lyrics?.split("\n")?.filter { it.isNotBlank() }

    return Song(
        id = id,
        name = name,
        lyrics = lyrics,
        price = price?.toInt(),
        songPath = songPath,
        thumbnailPath = thumbnailPath,
        listLyric = resolvedListLyric,
        author = authorName?.let { User(name = it) }
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
