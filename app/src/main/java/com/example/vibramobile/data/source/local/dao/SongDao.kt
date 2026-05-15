package com.example.vibramobile.data.source.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.vibramobile.data.source.local.entity.SongEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SongDao {
    @Query("SELECT * FROM songs")
    fun getAllSongs(): Flow<List<SongEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(songs: List<SongEntity>)

    @Query("SELECT * FROM songs")
    suspend fun getAllSongsOnce(): List<SongEntity>

    @Query("UPDATE songs SET song_path = :songPath WHERE id = :songId")
    suspend fun updateSongPath(songId: Int, songPath: String)

    @Query("UPDATE songs SET song_path = :songPath, thumbnail_path = :thumbnailPath, author_avatar_path = :authorAvatarPath WHERE id = :id")
    suspend fun updatePaths(
        id: Int,
        songPath: String?,
        thumbnailPath: String?,
        authorAvatarPath: String?
    )

    @Query("DELETE FROM songs")
    suspend fun deleteAll()
}
