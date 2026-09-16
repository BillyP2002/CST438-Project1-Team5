package com.example.cst438_project1_team5.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.cst438_project1_team5.database.entities.ChallengeSongEntity

@Dao
interface ChallengeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun recordChallenge(challenge: ChallengeSongEntity): Long

    @Query(
        "SELECT * FROM daily_challenge_history " +
            "WHERE user_id = :userId AND challenge_date = :date " +
            "ORDER BY played_at DESC"
    )
    suspend fun getChallengeHistory(
        userId: Long,
        date: String
    ): List<ChallengeSongEntity>
}
