package com.example.cst438_project1_team5.database

import android.util.Base64
import androidx.room.withTransaction
import com.example.cst438_project1_team5.api.malapi.MalApiRepository
import java.util.Locale

data class UserAccount(
    val id: Long,
    val playerName: String,
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

data class MalWatchlistEntry(
    val id: Long,
    val userId: Long,
    val malAnimeId: Int,
    val title: String,
    val status: String?,
    val score: Int?
)

@Suppress("TooManyFunctions")
class MusicRepository(private val database: AppDatabase) {
    private val userDao = database.userDao()
    private val songListDao = database.songListDao()
    private val dailyChallengeDao = database.dailyChallengeDao()
    private val malWatchlistDao = database.malWatchlistDao()

    suspend fun syncMalWatchlist(userId: Long, accessToken: String): Result<Int> {
        val result = MalApiRepository(accessToken).getList()
        result.getOrNull()?.let { malUser ->
            val now = System.currentTimeMillis()
            val entries = malUser.showsWatched.map { show ->
                MalWatchlistEntity(
                    userId = userId,
                    malAnimeId = show.id,
                    title = show.title,
                    status = show.completedStatus,
                    score = show.score,
                    updatedAt = now
                )
            }
            database.withTransaction {
                malWatchlistDao.deleteForUser(userId)
                malWatchlistDao.insertAll(entries)
            }
            return Result.success(entries.size)
        }
        return Result.failure(result.exceptionOrNull() ?: IllegalStateException("MAL list was empty"))
    }

    suspend fun getMalWatchlist(userId: Long): List<MalWatchlistEntry> =
        malWatchlistDao.getForUser(userId).map {
            MalWatchlistEntry(
                id = it.id,
                userId = it.userId,
                malAnimeId = it.malAnimeId,
                title = it.title,
                status = it.status,
                score = it.score
            )
        }

    suspend fun registerUser(playerName: String, email: String, password: String): Long {
        val cleanPlayerName = playerName.trim()
        val cleanEmail = email.trim()

        require(cleanPlayerName.isNotEmpty()) { "Player name cannot be empty." }
        require(cleanEmail.isNotEmpty()) { "Email cannot be empty." }
        require(isValidEmail(cleanEmail)) { "Email format is invalid." }
        require(password.length >= 12) { "Password must be at least 12 characters long." }

        val normalizedUser = cleanPlayerName.lowercase(Locale.US)
        val normalizedEmail = cleanEmail.lowercase(Locale.US)

        if (userDao.findByEmail(normalizedEmail) != null) {
            throw IllegalArgumentException("An account with that email already exists.")
        }
        if (userDao.findByPlayerName(normalizedUser) != null) {
            throw IllegalArgumentException("That player name is already taken.")
        }

        val salt = PasswordSecurity.generateSalt()
        val hash = PasswordSecurity.hashPassword(password, salt)

        val userEntity = UserEntity(
            playerName = normalizedUser,
            email = normalizedEmail,
            passwordHash = hash,
            passwordSalt = Base64.encodeToString(salt, Base64.NO_WRAP),
            passwordIterations = PasswordSecurity.DEFAULT_ITERATIONS,
            createdAt = System.currentTimeMillis()
        )

        return userDao.insert(userEntity)
    }

    suspend fun authenticateUser(identifier: String, password: String): UserAccount? {
        val normalizedIdentifier = identifier.trim().lowercase(Locale.US)
        val user = userDao.findByPlayerNameOrEmail(normalizedIdentifier) ?: return null

        if (System.currentTimeMillis() < user.lockedUntil) {
            return null
        }

        val expectedHash = PasswordSecurity.hashPassword(
            password,
            Base64.decode(user.passwordSalt, Base64.NO_WRAP),
            user.passwordIterations
        )

        return if (constantTimeEquals(expectedHash, user.passwordHash)) {
            val updatedUser = user.copy(
                lastLoginAt = System.currentTimeMillis(),
                failedAttempts = 0,
                lockedUntil = 0
            )
            userDao.update(updatedUser)
            mapUserEntityToAccount(updatedUser)
        } else {
            val failedAttempts = user.failedAttempts + 1
            val lockDuration = if (failedAttempts >= 5) {
            LOCK_DURATION_MS
        } else {
            0L
        }

            val updatedUser = user.copy(
                failedAttempts = failedAttempts,
                lockedUntil = if (lockDuration > 0L) {
                System.currentTimeMillis() + lockDuration
            } else {
                0L
            }
            )
            userDao.update(updatedUser)
            null
        }
    }

    suspend fun addSongToUserList(
        userId: Long,
        songId: String,
        title: String,
        artist: String,
        album: String? = null
    ): Long {
        val songEntity = SongListEntity(
            userId = userId,
            songId = songId,
            title = title,
            artist = artist,
            album = album,
            createdAt = System.currentTimeMillis()
        )
        return songListDao.insert(songEntity)
    }

    suspend fun addSongToFavorites(userId: Long, songId: String): Int =
        songListDao.addToFavorites(userId, songId)

    suspend fun getUserSongList(userId: Long): List<SongListEntry> =
        songListDao.getSongsForUser(userId).map {
            SongListEntry(
                id = it.id,
                userId = it.userId,
                songId = it.songId,
                title = it.title,
                artist = it.artist,
                album = it.album,
                isFavorite = it.isFavorite,
                createdAt = it.createdAt
            )
        }

    suspend fun recordDailyChallengeSong(
        userId: Long,
        challengeDate: String,
        songId: String,
        title: String,
        artist: String,
        album: String? = null
    ): Long {
        val challengeEntity = DailyChallengeEntity(
            userId = userId,
            challengeDate = challengeDate,
            songId = songId,
            title = title,
            artist = artist,
            album = album,
            playedAt = System.currentTimeMillis()
        )
        return dailyChallengeDao.insert(challengeEntity)
    }

    suspend fun getPastPlayedSongsForChallenge(
        userId: Long,
        challengeDate: String
    ): List<PastChallengeSong> = dailyChallengeDao.getForChallenge(userId, challengeDate).map {
        PastChallengeSong(
            id = it.id,
            userId = it.userId,
            challengeDate = it.challengeDate,
            songId = it.songId,
            title = it.title,
            artist = it.artist,
            album = it.album,
            playedAt = it.playedAt
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

    private fun mapUserEntityToAccount(entity: UserEntity): UserAccount = UserAccount(
        id = entity.id,
        playerName = entity.playerName,
        email = entity.email,
        passwordHash = entity.passwordHash,
        passwordSalt = entity.passwordSalt,
        passwordIterations = entity.passwordIterations,
        createdAt = entity.createdAt,
        lastLoginAt = entity.lastLoginAt,
        failedAttempts = entity.failedAttempts,
        lockedUntil = entity.lockedUntil
    )

    companion object {
        private const val LOCK_DURATION_MS = 15L * 60L * 1000L
    }
}
