package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.runBlocking
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assume.assumeTrue
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import com.example.cst438_project1_team5.api.malapi.MalApiRepository
import com.example.cst438_project1_team5.api.malapi.MalOAuthManager

@RunWith(AndroidJUnit4::class)
class MalApiTest {

    @Test
    fun getList() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val malOAuthManager = MalOAuthManager(context)
        val accessToken = malOAuthManager.getAccessToken()
        assumeTrue(
            "MAL integration test requires a locally stored OAuth token",
            !accessToken.isNullOrBlank()
        )
        val repository = MalApiRepository(requireNotNull(accessToken))
        val result = repository.getList()

        println(result.exceptionOrNull()?.stackTraceToString())
        println(result)
        assertTrue(result.isSuccess)
    }
}