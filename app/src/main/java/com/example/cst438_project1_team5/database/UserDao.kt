package com.example.cst438_project1_team5.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Query(
        "SELECT * FROM users WHERE LOWER(player_name) = LOWER(:identifier) " +
            "OR LOWER(email) = LOWER(:identifier) LIMIT 1"
    )
    suspend fun findByPlayerNameOrEmail(identifier: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(player_name) = LOWER(:playerName) LIMIT 1")
    suspend fun findByPlayerName(playerName: String): UserEntity?

    @Query("SELECT * FROM users WHERE LOWER(email) = LOWER(:email) LIMIT 1")
    suspend fun findByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun findById(id: Long): UserEntity?

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Update
    suspend fun update(user: UserEntity)
}
