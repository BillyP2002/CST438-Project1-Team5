package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.api.malAPI.MalApiRepository
import com.example.cst438_project1_team5.api.malAPI.MalOAuthManager
import kotlinx.coroutines.runBlocking
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assume.assumeTrue
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import android.util.Log

@RunWith(AndroidJUnit4::class)
class malAPITest {

    @Test
    fun getList() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val malOAuthManager = MalOAuthManager(context)
        val accessToken = malOAuthManager.getAccessToken()

        assumeTrue(
            "Test skipped: No locally stored OAuth token found. Ensure you logged in via the app UI first!",
            !accessToken.isNullOrBlank()
        )

        val repository = MalApiRepository(requireNotNull(accessToken))
        val result = repository.getList("Marxeru")

        println(result.exceptionOrNull()?.stackTraceToString())
        println(result)
        assertTrue(result.isSuccess)
    }
}