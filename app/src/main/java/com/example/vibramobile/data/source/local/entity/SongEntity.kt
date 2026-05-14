package com.example.vibramobile.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "songs")
data class SongEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int? = null,

    @ColumnInfo(name = "name")
    val name: String? = null,

    @ColumnInfo(name = "price")
    val price: Double? = null,

    @ColumnInfo(name = "lyrics")
    val lyrics: String? = null,

    @ColumnInfo(name = "song_path")
    val songPath: String? = null,

    @ColumnInfo(name = "thumbnail_path")
    val thumbnailPath: String? = null,

    @ColumnInfo(name = "author_name")
    val authorName: String? = null,

    @ColumnInfo(name = "author_avatar_path")
    val authorAvatarPath: String? = null
)