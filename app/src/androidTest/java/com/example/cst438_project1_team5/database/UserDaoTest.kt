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
            username = "testuser",
            email = "test@example.com",
            passwordHash = "hash",
            passwordSalt = "salt",
            passwordIterations = 1000,
            createdAt = System.currentTimeMillis()
        )
        val id = userDao.insert(user)
        
        val loadedUser = userDao.findById(id)
        assertNotNull(loadedUser)
        assertEquals("testuser", loadedUser?.username)
        assertEquals("test@example.com", loadedUser?.email)
    }

    @Test
    fun findByUsernameOrEmail() = runBlocking {
        val user = UserEntity(
            username = "TestUser",
            email = "email@Test.com",
            passwordHash = "hash",
            passwordSalt = "salt",
            passwordIterations = 1000,
            createdAt = System.currentTimeMillis()
        )
        userDao.insert(user)

        // Test finding by exact username
        var foundUser = userDao.findByUsernameOrEmail("TestUser")
        assertNotNull(foundUser)

        // Test finding by lowercase username (should be case-insensitive in SQLite usually, but test logic in repo enforces lowercase)
        foundUser = userDao.findByUsernameOrEmail("testuser")
        assertNotNull(foundUser)

        // Test finding by exact email
        foundUser = userDao.findByUsernameOrEmail("email@Test.com")
        assertNotNull(foundUser)

        // Test finding by lowercase email
        foundUser = userDao.findByUsernameOrEmail("email@test.com")
        assertNotNull(foundUser)

        // Test finding non-existent user
        foundUser = userDao.findByUsernameOrEmail("nonexistent")
        assertNull(foundUser)
    }
}
