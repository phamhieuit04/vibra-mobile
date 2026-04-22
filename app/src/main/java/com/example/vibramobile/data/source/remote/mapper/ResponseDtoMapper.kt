package com.example.vibramobile.data.source.remote.mapper

import com.example.vibramobile.data.source.remote.dto.BillResponseDto
import com.example.vibramobile.data.source.remote.dto.CategoryResponseDto
import com.example.vibramobile.data.source.remote.dto.LibraryResponseDto
import com.example.vibramobile.data.source.remote.dto.PlaylistResponseDto
import com.example.vibramobile.data.source.remote.dto.SearchResultResponseDto
import com.example.vibramobile.data.source.remote.dto.SongResponseDto
import com.example.vibramobile.data.source.remote.dto.UserResponseDto
import com.example.vibramobile.domain.model.Bill
import com.example.vibramobile.domain.model.Category
import com.example.vibramobile.domain.model.Library
import com.example.vibramobile.domain.model.Playlist
import com.example.vibramobile.domain.model.SearchResult
import com.example.vibramobile.domain.model.Song
import com.example.vibramobile.domain.model.User

fun UserResponseDto.toDomain(): User {
    return User(
        id = id,
        name = name,
        description = description,
        email = email,
        avatar = avatar,
        followers = followers,
        token = token,
        avatarPath = avatar_path
    )
}

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

fun CategoryResponseDto.toDomain(): Category {
    return Category(
        id = id,
        name = name,
        description = description,
        thumbnailPath = thumbnail_path
    )
}

fun LibraryResponseDto.toDomain(): Library {
    return Library(
        id = id,
        userId = user_id,
        playlistId = playlist_id,
        artistId = artist_id,
        songId = song_id
    )
}

fun BillResponseDto.toDomain(): Bill {
    return Bill(
        id = id,
        userId = user_id,
        orderCode = order_code,
        playlistId = playlist_id,
        status = status,
        createdAt = created_at,
        song = song?.toDomain(),
        playlist = playlist?.toDomain()
    )
}

fun SearchResultResponseDto.toDomain(): SearchResult {
    return SearchResult(
        albums = albums.orEmpty().map { it.toDomain() },
        artists = artists.orEmpty().map { it.toDomain() },
        songs = songs.orEmpty().map { it.toDomain() }
    )
}

