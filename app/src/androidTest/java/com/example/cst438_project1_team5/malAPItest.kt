package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

private val Unit.isSuccess: Boolean
    get() = this is Boolean && this

@RunWith(AndroidJUnit4::class)
class malAPITest {
    @Test
    fun getList(getList: Unit.(String) -> Unit) {
        val repository = malAPIRepository()
        val result = repository.getList("Marxeru")
        println(result)
        assertTrue(result.isSuccess)
    }

    private fun malAPIRepository() {
        TODO("Not yet implemented")
    }

}