package com.example.cst438_project1_team5.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "daily_challenge_history",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id", "challenge_date"]),
        Index(value = ["user_id", "challenge_date", "song_id"], unique = true)
    ]
)
data class DailyChallengeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "user_id")
    val userId: Long,

    @ColumnInfo(name = "challenge_date")
    val challengeDate: String,

    @ColumnInfo(name = "song_id")
    val songId: String,

    val title: String,
    val artist: String,
    val album: String?,

    @ColumnInfo(name = "played_at")
    val playedAt: Long
)
