package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.api.MalAPIRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class malAPItest {

    @Test
    fun getList() = runBlocking {
        val repository = MalAPIRepository()
        val result = repository.getList("Marxeru")
        println(result)
        assertTrue(result.isSuccess)
    }
}
