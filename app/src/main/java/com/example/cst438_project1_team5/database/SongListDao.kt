package com.example.cst438_project1_team5.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SongListDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(song: SongListEntity): Long

    @Query("SELECT * FROM user_song_list WHERE user_id = :userId ORDER BY created_at DESC")
    suspend fun getSongsForUser(userId: Long): List<SongListEntity>

    @Query(
        "UPDATE user_song_list SET is_favorite = 1 " +
            "WHERE user_id = :userId AND song_id = :songId"
    )
    suspend fun addToFavorites(userId: Long, songId: String): Int
}
