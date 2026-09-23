package com.example.cst438_project1_team5.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "mal_watchlist",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["user_id"]),
        Index(value = ["user_id", "mal_anime_id"], unique = true)
    ]
)
data class MalWatchlistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id") val userId: Long,
    @ColumnInfo(name = "mal_anime_id") val malAnimeId: Long,
    val title: String,
    val status: String?,
    val score: Int?,
    @ColumnInfo(name = "updated_at") val updatedAt: Long
)
