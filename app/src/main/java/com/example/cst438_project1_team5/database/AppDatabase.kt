package com.example.cst438_project1_team5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        UserEntity::class,
        SongListEntity::class,
        DailyChallengeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun songListDao(): SongListDao
    abstract fun dailyChallengeDao(): DailyChallengeDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "music_app_room.db"
            )
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
            instance = db
            db
        }
    }
}
