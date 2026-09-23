package com.example.cst438_project1_team5.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        UserEntity::class,
        SongListEntity::class,
        DailyChallengeEntity::class,
        MalWatchlistEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun songListDao(): SongListDao
    abstract fun dailyChallengeDao(): DailyChallengeDao
    abstract fun malWatchlistDao(): MalWatchlistDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase = instance ?: synchronized(this) {
            val db = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "music_app_room.db"
            )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration(dropAllTables = true)
                .build()
            instance = db
            db
        }

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """CREATE TABLE IF NOT EXISTS mal_watchlist (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        user_id INTEGER NOT NULL,
                        mal_anime_id INTEGER NOT NULL,
                        title TEXT NOT NULL,
                        status TEXT,
                        score INTEGER,
                        updated_at INTEGER NOT NULL,
                        FOREIGN KEY(user_id) REFERENCES users(id) ON DELETE CASCADE
                    )"""
                )
                db.execSQL(
                    "CREATE UNIQUE INDEX IF NOT EXISTS index_mal_watchlist_user_id_mal_anime_id " +
                        "ON mal_watchlist(user_id, mal_anime_id)"
                )
                db.execSQL(
                    "CREATE INDEX IF NOT EXISTS index_mal_watchlist_user_id " +
                        "ON mal_watchlist(user_id)"
                )
            }
        }
    }
}
