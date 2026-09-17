package com.example.cst438_project1_team5.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface DailyChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(song: DailyChallengeEntity): Long

    @Query(
        "SELECT * FROM daily_challenge_history " +
            "WHERE user_id = :userId AND challenge_date = :challengeDate " +
            "ORDER BY played_at DESC"
    )
    suspend fun getForChallenge(userId: Long, challengeDate: String): List<DailyChallengeEntity>
}
