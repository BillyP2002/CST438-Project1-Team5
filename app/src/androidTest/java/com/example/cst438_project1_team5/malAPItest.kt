package com.example.cst438_project1_team5

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

@RunWith(AndroidJUnit4::class)
class malAPITest {
    @Test
    fun getList() {
        val repository = malAPIRepository()
        val result = repository.getList("Marxeru")
        println(result)
        assertTrue(result.isSuccess)
    }
}