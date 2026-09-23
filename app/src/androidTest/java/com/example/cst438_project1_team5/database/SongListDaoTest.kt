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
class SongListDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var songDao: SongListDao
    private lateinit var userDao: UserDao

    private val testUserId = 1L

    @Before
    fun createDb() = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        songDao = database.songListDao()
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
    fun insertAndGetSongsForUser() = runBlocking {
        val song1 = SongListEntity(
            userId = testUserId,
            songId = "song_1",
            title = "Title 1",
            artist = "Artist 1",
            album = "Album 1",
            createdAt = System.currentTimeMillis()
        )
        val song2 = SongListEntity(
            userId = testUserId,
            songId = "song_2",
            title = "Title 2",
            artist = "Artist 2",
            album = "Album 2",
            createdAt = System.currentTimeMillis()
        )

        songDao.insert(song1)
        songDao.insert(song2)

        val userSongs = songDao.getSongsForUser(testUserId)
        
        assertEquals(2, userSongs.size)
        // Check order (created_at DESC means the newer ones might come first depending on timestamps, but since they are processed immediately let's just check contents)
        assertTrue(userSongs.any { it.songId == "song_1" })
        assertTrue(userSongs.any { it.songId == "song_2" })
    }

    @Test
    fun addToFavorites() = runBlocking {
        val song = SongListEntity(
            userId = testUserId,
            songId = "song_1",
            title = "Title 1",
            artist = "Artist 1",
            album = "Album 1",
            createdAt = System.currentTimeMillis()
        )
        songDao.insert(song)

        var userSongs = songDao.getSongsForUser(testUserId)
        assertEquals(1, userSongs.size)
        assertEquals(false, userSongs[0].isFavorite)

        songDao.addToFavorites(testUserId, "song_1")

        userSongs = songDao.getSongsForUser(testUserId)
        assertTrue(userSongs[0].isFavorite)
    }
}
