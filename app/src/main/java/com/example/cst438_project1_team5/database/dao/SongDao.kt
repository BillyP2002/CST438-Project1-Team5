package com.example.cst438_project1_team5.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cst438_project1_team5.database.entities.SongEntity

@Dao
interface SongDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertSong(song: SongEntity): Long

    @Query(
        "UPDATE user_song_list SET is_favorite = 1 " +
            "WHERE user_id = :userId AND song_id = :songId"
    )
    suspend fun markFavorite(userId: Long, songId: String): Int

    @Query(
        "SELECT * FROM user_song_list " +
            "WHERE user_id = :userId ORDER BY created_at DESC"
    )
    suspend fun getUserSongs(userId: Long): List<SongEntity>

    @Query(
        "DELETE FROM user_song_list " +
            "WHERE user_id = :userId AND song_id = :songId"
    )
    suspend fun removeSong(userId: Long, songId: String): Int
}
