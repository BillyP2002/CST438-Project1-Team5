package com.example.cst438_project1_team5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.cst438_project1_team5.database.dao.ChallengeDao
import com.example.cst438_project1_team5.database.dao.SongDao
import com.example.cst438_project1_team5.database.dao.UserDao
import com.example.cst438_project1_team5.database.entities.ChallengeSongEntity
import com.example.cst438_project1_team5.database.entities.SongEntity
import com.example.cst438_project1_team5.database.entities.UserEntity

@Database(
    entities = [
        UserEntity::class,
        SongEntity::class,
        ChallengeSongEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun songDao(): SongDao
    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "music_app_secure.db"
                )
                .fallbackToDestructiveMigration() // Mirroring SQLiteOpenHelper's onUpgrade behavior for now
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
