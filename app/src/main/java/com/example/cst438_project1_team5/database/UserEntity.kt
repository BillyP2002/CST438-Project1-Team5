package com.example.cst438_project1_team5.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "users",
    indices = [
        Index(value = ["player_name"], unique = true),
        Index(value = ["email"], unique = true)
    ]
)
data class UserEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo(name = "player_name")
    val playerName: String,
    val email: String,

    @ColumnInfo(name = "password_hash")
    val passwordHash: String,

    @ColumnInfo(name = "password_salt")
    val passwordSalt: String,

    @ColumnInfo(name = "password_iterations")
    val passwordIterations: Int = 120_000,

    @ColumnInfo(name = "created_at")
    val createdAt: Long,

    @ColumnInfo(name = "last_login_at")
    val lastLoginAt: Long? = null,

    @ColumnInfo(name = "failed_attempts")
    val failedAttempts: Int = 0,

    @ColumnInfo(name = "locked_until")
    val lockedUntil: Long = 0
)
