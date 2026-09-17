package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.api.malAPI.MalApiRepository
import com.example.cst438_project1_team5.api.malAPI.MalOAuthManager
import kotlinx.coroutines.runBlocking
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class malAPITest {

    @Test
    fun getList() = runBlocking {
        val context = InstrumentationRegistry
            .getInstrumentation()
            .targetContext
        val malOAuthManager = MalOAuthManager(context)
        val accessToken = malOAuthManager.getAccessToken()
            ?: error("No MAL access token saved")
        val repository = MalApiRepository(accessToken)
        val result = repository.getList("Marxeru")
        println(result.exceptionOrNull()?.stackTraceToString())
        println(result)
        assertTrue(result.isSuccess)
    }
}