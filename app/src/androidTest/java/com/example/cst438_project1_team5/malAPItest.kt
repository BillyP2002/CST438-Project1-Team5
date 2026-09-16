package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.api.malAPI.malAPIRepository
import kotlinx.coroutines.runBlocking

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class malAPITest {
    @Test
    fun getList() = runBlocking {
        val accessToken = "YOUR_MAL_ACCESS_TOKEN"
        val repository = malAPIRepository(accessToken)
        val result = repository.getList("Marxeru")
        println(result.exceptionOrNull()?.stackTraceToString())
        println(result)
        assertTrue(result.isSuccess)
    }
}