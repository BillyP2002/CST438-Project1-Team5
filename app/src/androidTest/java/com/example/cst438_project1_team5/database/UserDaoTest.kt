package com.example.cst438_project1_team5.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserDaoTest {

    private lateinit var database: AppDatabase
    private lateinit var userDao: UserDao

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        ).build()
        userDao = database.userDao()
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun insertAndGetUser() = runBlocking {
        val user = UserEntity(
            playerName = "testplayer",
            email = "test@example.com",
            passwordHash = "hash",
            passwordSalt = "salt",
            passwordIterations = 1000,
            createdAt = System.currentTimeMillis()
        )
        val id = userDao.insert(user)
        
        val loadedUser = userDao.findById(id)
        assertNotNull(loadedUser)
        assertEquals("testplayer", loadedUser?.playerName)
        assertEquals("test@example.com", loadedUser?.email)
    }

    @Test
    fun findByPlayerNameOrEmail() = runBlocking {
        val user = UserEntity(
            playerName = "TestPlayer",
            email = "email@Test.com",
            passwordHash = "hash",
            passwordSalt = "salt",
            passwordIterations = 1000,
            createdAt = System.currentTimeMillis()
        )
        userDao.insert(user)

        // Test finding by exact playerName
        var foundUser = userDao.findByPlayerNameOrEmail("TestPlayer")
        assertNotNull(foundUser)

        // Test finding by lowercase playerName (should be case-insensitive in SQLite usually, but test logic in repo enforces lowercase)
        foundUser = userDao.findByPlayerNameOrEmail("testplayer")
        assertNotNull(foundUser)

        // Test finding by exact email
        foundUser = userDao.findByPlayerNameOrEmail("email@Test.com")
        assertNotNull(foundUser)

        // Test finding by lowercase email
        foundUser = userDao.findByPlayerNameOrEmail("email@test.com")
        assertNotNull(foundUser)

        // Test finding non-existent user
        foundUser = userDao.findByPlayerNameOrEmail("nonexistent")
        assertNull(foundUser)
    }
}
