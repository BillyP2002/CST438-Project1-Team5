package com.example.cst438_project1_team5.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface MalWatchlistDao {
    @Query("DELETE FROM mal_watchlist WHERE user_id = :userId")
    suspend fun deleteForUser(userId: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(entries: List<MalWatchlistEntity>)

    @Query("SELECT * FROM mal_watchlist WHERE user_id = :userId ORDER BY title COLLATE NOCASE")
    suspend fun getForUser(userId: Long): List<MalWatchlistEntity>
}
