package com.example.cst438_project1_team5.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Base64
import java.security.SecureRandom
import java.util.Locale
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

private const val DATABASE_NAME = "music_app_secure.db"
private const val DATABASE_VERSION = 1

private const val USER_TABLE = "users"
private const val SONG_LIST_TABLE = "user_song_list"
private const val DAILY_CHALLENGE_TABLE = "daily_challenge_history"

data class UserAccount(
    val id: Long,
    val username: String,
    val email: String,
    val passwordHash: String,
    val passwordSalt: String,
    val passwordIterations: Int,
    val createdAt: Long,
    val lastLoginAt: Long?,
    val failedAttempts: Int,
    val lockedUntil: Long
)

data class SongListEntry(
    val id: Long,
    val userId: Long,
    val songId: String,
    val title: String,
    val artist: String,
    val album: String?,
    val isFavorite: Boolean,
    val createdAt: Long
)

data class PastChallengeSong(
    val id: Long,
    val userId: Long,
    val challengeDate: String,
    val songId: String,
    val title: String,
    val artist: String,
    val album: String?,
    val playedAt: Long
)

class MusicDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON;")

        db.execSQL(
            """
            CREATE TABLE $USER_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                email TEXT NOT NULL UNIQUE,
                password_hash TEXT NOT NULL,
                password_salt TEXT NOT NULL,
                password_iterations INTEGER NOT NULL DEFAULT 120000,
                created_at INTEGER NOT NULL,
                last_login_at INTEGER,
                failed_attempts INTEGER NOT NULL DEFAULT 0,
                locked_until INTEGER NOT NULL DEFAULT 0
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $SONG_LIST_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                song_id TEXT NOT NULL,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                album TEXT,
                is_favorite INTEGER NOT NULL DEFAULT 0,
                created_at INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES $USER_TABLE(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $DAILY_CHALLENGE_TABLE (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id INTEGER NOT NULL,
                challenge_date TEXT NOT NULL,
                song_id TEXT NOT NULL,
                title TEXT NOT NULL,
                artist TEXT NOT NULL,
                album TEXT,
                played_at INTEGER NOT NULL,
                FOREIGN KEY (user_id) REFERENCES $USER_TABLE(id) ON DELETE CASCADE
            )
            """.trimIndent()
        )

        db.execSQL("CREATE UNIQUE INDEX idx_user_song_list_unique ON $SONG_LIST_TABLE(user_id, song_id)")
        db.execSQL("CREATE INDEX idx_song_list_user_id ON $SONG_LIST_TABLE(user_id)")
        db.execSQL("CREATE INDEX idx_daily_challenge_user_date ON $DAILY_CHALLENGE_TABLE(user_id, challenge_date)")
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $DAILY_CHALLENGE_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $SONG_LIST_TABLE")
        db.execSQL("DROP TABLE IF EXISTS $USER_TABLE")
        onCreate(db)
    }

    fun registerUser(username: String, email: String, password: String): Long {
        val cleanUsername = username.trim()
        val cleanEmail = email.trim()

        require(cleanUsername.isNotEmpty()) { "Username cannot be empty." }
        require(cleanEmail.isNotEmpty()) { "Email cannot be empty." }
        require(isValidEmail(cleanEmail)) { "Email format is invalid." }
        require(password.length >= 12) { "Password must be at least 12 characters long." }

        val normalizedUser = cleanUsername.lowercase(Locale.US)
        val normalizedEmail = cleanEmail.lowercase(Locale.US)

        if (findUserByEmail(normalizedEmail) != null) {
            throw IllegalArgumentException("An account with that email already exists.")
        }
        if (findUserByUsername(normalizedUser) != null) {
            throw IllegalArgumentException("That username is already taken.")
        }

        val salt = PasswordSecurity.generateSalt()
        val hash = PasswordSecurity.hashPassword(password, salt)
        val values = ContentValues().apply {
            put("username", normalizedUser)
            put("email", normalizedEmail)
            put("password_hash", hash)
            put("password_salt", Base64.encodeToString(salt, Base64.NO_WRAP))
            put("password_iterations", PasswordSecurity.DEFAULT_ITERATIONS)
            put("created_at", System.currentTimeMillis())
            put("failed_attempts", 0)
            put("locked_until", 0)
        }

        return writableDatabase.insertOrThrow(USER_TABLE, null, values)
    }

    fun authenticateUser(identifier: String, password: String): UserAccount? {
        val normalizedIdentifier = identifier.trim().lowercase(Locale.US)
        val user = findUserByUsername(normalizedIdentifier) ?: findUserByEmail(normalizedIdentifier)
            ?: return null

        if (System.currentTimeMillis() < user.lockedUntil) {
            return null
        }

        val expectedHash = PasswordSecurity.hashPassword(
            password,
            Base64.decode(user.passwordSalt, Base64.NO_WRAP),
            user.passwordIterations
        )

        return if (constantTimeEquals(expectedHash, user.passwordHash)) {
            val updated = ContentValues().apply {
                put("last_login_at", System.currentTimeMillis())
                put("failed_attempts", 0)
                put("locked_until", 0)
            }
            writableDatabase.update(
                USER_TABLE,
                updated,
                "id = ?",
                arrayOf(user.id.toString())
            )
            user.copy(
                lastLoginAt = System.currentTimeMillis(),
                failedAttempts = 0,
                lockedUntil = 0
            )
        } else {
            val failedAttempts = user.failedAttempts + 1
            val lockDuration = if (failedAttempts >= 5) {
                15L * 60L * 1000L
            } else {
                0L
            }

            val updated = ContentValues().apply {
                put("failed_attempts", failedAttempts)
                put("locked_until", if (lockDuration > 0L) System.currentTimeMillis() + lockDuration else 0L)
            }
            writableDatabase.update(USER_TABLE, updated, "id = ?", arrayOf(user.id.toString()))
            null
        }
    }

    fun addSongToUserList(userId: Long, songId: String, title: String, artist: String, album: String? = null): Long {
        val values = ContentValues().apply {
            put("user_id", userId)
            put("song_id", songId)
            put("title", title)
            put("artist", artist)
            put("album", album)
            put("is_favorite", 0)
            put("created_at", System.currentTimeMillis())
        }

        return writableDatabase.insertWithOnConflict(
            SONG_LIST_TABLE,
            null,
            values,
            SQLiteDatabase.CONFLICT_IGNORE
        )
    }

    fun addSongToFavorites(userId: Long, songId: String): Int {
        val values = ContentValues().apply {
            put("is_favorite", 1)
        }
        return writableDatabase.update(
            SONG_LIST_TABLE,
            values,
            "user_id = ? AND song_id = ?",
            arrayOf(userId.toString(), songId)
        )
    }

    fun getUserSongList(userId: Long): List<SongListEntry> {
        val cursor = readableDatabase.query(
            SONG_LIST_TABLE,
            arrayOf("id", "user_id", "song_id", "title", "artist", "album", "is_favorite", "created_at"),
            "user_id = ?",
            arrayOf(userId.toString()),
            null,
            null,
            "created_at DESC"
        )

        val list = mutableListOf<SongListEntry>()
        with(cursor) {
            while (moveToNext()) {
                list.add(
                    SongListEntry(
                        id = getLong(getColumnIndexOrThrow("id")),
                        userId = getLong(getColumnIndexOrThrow("user_id")),
                        songId = getString(getColumnIndexOrThrow("song_id")),
                        title = getString(getColumnIndexOrThrow("title")),
                        artist = getString(getColumnIndexOrThrow("artist")),
                        album = getString(getColumnIndexOrThrow("album")),
                        isFavorite = getInt(getColumnIndexOrThrow("is_favorite")) == 1,
                        createdAt = getLong(getColumnIndexOrThrow("created_at"))
                    )
                )
            }
            close()
        }
        return list
    }

    fun recordDailyChallengeSong(
        userId: Long,
        challengeDate: String,
        songId: String,
        title: String,
        artist: String,
        album: String? = null
    ): Long {
        val values = ContentValues().apply {
            put("user_id", userId)
            put("challenge_date", challengeDate)
            put("song_id", songId)
            put("title", title)
            put("artist", artist)
            put("album", album)
            put("played_at", System.currentTimeMillis())
        }

        return writableDatabase.insertWithOnConflict(
            DAILY_CHALLENGE_TABLE,
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
    }

    fun getPastPlayedSongsForChallenge(userId: Long, challengeDate: String): List<PastChallengeSong> {
        val cursor = readableDatabase.query(
            DAILY_CHALLENGE_TABLE,
            arrayOf("id", "user_id", "challenge_date", "song_id", "title", "artist", "album", "played_at"),
            "user_id = ? AND challenge_date = ?",
            arrayOf(userId.toString(), challengeDate),
            null,
            null,
            "played_at DESC"
        )

        val list = mutableListOf<PastChallengeSong>()
        with(cursor) {
            while (moveToNext()) {
                list.add(
                    PastChallengeSong(
                        id = getLong(getColumnIndexOrThrow("id")),
                        userId = getLong(getColumnIndexOrThrow("user_id")),
                        challengeDate = getString(getColumnIndexOrThrow("challenge_date")),
                        songId = getString(getColumnIndexOrThrow("song_id")),
                        title = getString(getColumnIndexOrThrow("title")),
                        artist = getString(getColumnIndexOrThrow("artist")),
                        album = getString(getColumnIndexOrThrow("album")),
                        playedAt = getLong(getColumnIndexOrThrow("played_at"))
                    )
                )
            }
            close()
        }
        return list
    }

    private fun findUserByUsername(username: String): UserAccount? {
        val cursor = readableDatabase.query(
            USER_TABLE,
            USER_COLUMNS,
            "LOWER(username) = ?",
            arrayOf(username.lowercase(Locale.US)),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) mapUserRow(it) else null
        }
    }

    private fun findUserByEmail(email: String): UserAccount? {
        val cursor = readableDatabase.query(
            USER_TABLE,
            USER_COLUMNS,
            "LOWER(email) = ?",
            arrayOf(email.lowercase(Locale.US)),
            null,
            null,
            null
        )
        return cursor.use {
            if (it.moveToFirst()) mapUserRow(it) else null
        }
    }

    private fun mapUserRow(cursor: Cursor): UserAccount {
        val lastLoginIndex = cursor.getColumnIndexOrThrow("last_login_at")
        return UserAccount(
            id = cursor.getLong(cursor.getColumnIndexOrThrow("id")),
            username = cursor.getString(cursor.getColumnIndexOrThrow("username")),
            email = cursor.getString(cursor.getColumnIndexOrThrow("email")),
            passwordHash = cursor.getString(cursor.getColumnIndexOrThrow("password_hash")),
            passwordSalt = cursor.getString(cursor.getColumnIndexOrThrow("password_salt")),
            passwordIterations = cursor.getInt(cursor.getColumnIndexOrThrow("password_iterations")),
            createdAt = cursor.getLong(cursor.getColumnIndexOrThrow("created_at")),
            lastLoginAt = if (cursor.isNull(lastLoginIndex)) null else cursor.getLong(lastLoginIndex),
            failedAttempts = cursor.getInt(cursor.getColumnIndexOrThrow("failed_attempts")),
            lockedUntil = cursor.getLong(cursor.getColumnIndexOrThrow("locked_until"))
        )
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return email.matches(emailRegex)
    }

    private fun constantTimeEquals(expected: String, actual: String): Boolean {
        if (expected.length != actual.length) {
            return false
        }
        var result = 0
        for (i in expected.indices) {
            result = result or expected[i].code.xor(actual[i].code)
        }
        return result == 0
    }

    companion object {
        private val USER_COLUMNS = arrayOf(
            "id",
            "username",
            "email",
            "password_hash",
            "password_salt",
            "password_iterations",
            "created_at",
            "last_login_at",
            "failed_attempts",
            "locked_until"
        )
    }
}

object PasswordSecurity {
    const val DEFAULT_ITERATIONS = 120_000

    fun generateSalt(): ByteArray {
        val random = SecureRandom()
        val salt = ByteArray(16)
        random.nextBytes(salt)
        return salt
    }

    fun hashPassword(password: String, salt: ByteArray, iterations: Int = DEFAULT_ITERATIONS): String {
        val keySpec = PBEKeySpec(password.toCharArray(), salt, iterations, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val hashed = factory.generateSecret(keySpec).encoded
        return Base64.encodeToString(hashed, Base64.NO_WRAP)
    }
}
