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
class MalWatchlistDaoTest {
    private lateinit var database: AppDatabase
    private lateinit var dao: MalWatchlistDao
    private lateinit var userDao: UserDao

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        dao = database.malWatchlistDao()
        userDao = database.userDao()
        userDao.insert(testUser(1L, "first"))
        userDao.insert(testUser(2L, "second"))
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun watchlistIsStoredAndScopedToUser() = runBlocking {
        dao.insertAll(
            listOf(
                MalWatchlistEntity(
                    userId = 1L,
                    malAnimeId = 101L,
                    title = "First User Show",
                    status = "completed",
                    score = 8,
                    updatedAt = 1L
                ),
                MalWatchlistEntity(
                    userId = 2L,
                    malAnimeId = 202L,
                    title = "Second User Show",
                    status = "watching",
                    score = null,
                    updatedAt = 1L
                )
            )
        )

        val firstUserList = dao.getForUser(1L)
        val secondUserList = dao.getForUser(2L)

        assertEquals(1, firstUserList.size)
        assertEquals("First User Show", firstUserList.single().title)
        assertEquals(1, secondUserList.size)
        assertEquals("Second User Show", secondUserList.single().title)
        assertTrue(firstUserList.none { it.userId == 2L })
    }

    @Test
    fun deleteForUserDoesNotDeleteAnotherUsersList() = runBlocking {
        dao.insertAll(
            listOf(
                MalWatchlistEntity(1L, 1L, 101L, "First", "completed", 8, 1L),
                MalWatchlistEntity(2L, 2L, 202L, "Second", "watching", null, 1L)
            )
        )

        dao.deleteForUser(1L)

        assertTrue(dao.getForUser(1L).isEmpty())
        assertEquals("Second", dao.getForUser(2L).single().title)
    }

    private fun testUser(id: Long, name: String) = UserEntity(
        id = id,
        playerName = name,
        email = "$name@example.com",
        passwordHash = "hash",
        passwordSalt = "salt",
        createdAt = 1L
    )
}
