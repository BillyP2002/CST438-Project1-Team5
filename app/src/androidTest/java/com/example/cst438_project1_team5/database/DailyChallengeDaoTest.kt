package com.example.cst438_project1_team5.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DailyChallengeDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var challengeDao: DailyChallengeDao
    private lateinit var userDao: UserDao

    private val testUserId = 1L

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        challengeDao = database.dailyChallengeDao()
        userDao = database.userDao()

        // Insert a dummy user to satisfy foreign key constraints
        val user = UserEntity(
            id = testUserId,
            playerName = "testplayer",
            email = "test@example.com",
            passwordHash = "hash",
            passwordSalt = "salt",
            createdAt = System.currentTimeMillis()
        )
        userDao.insert(user)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetChallengeForUser() = runBlocking {
        val date = "2026-10-31"
        val challenge = DailyChallengeEntity(
            userId = testUserId,
            challengeDate = date,
            songId = "song123",
            title = "Test Song",
            artist = "Test Artist",
            album = "Test Album",
            playedAt = System.currentTimeMillis()
        )
        challengeDao.insert(challenge)

        val history = challengeDao.getForChallenge(testUserId, date)
        
        assertEquals(1, history.size)
        assertEquals("song123", history[0].songId)
    }

    @Test
    fun getEmptyChallengesForDifferentDate() = runBlocking {
        val challenge = DailyChallengeEntity(
            userId = testUserId,
            challengeDate = "2026-10-31",
            songId = "song123",
            title = "Test Song",
            artist = "Test Artist",
            album = "Test Album",
            playedAt = System.currentTimeMillis()
        )
        challengeDao.insert(challenge)

        val history = challengeDao.getForChallenge(testUserId, "2026-11-01")
        
        assertTrue(history.isEmpty())
    }
}
