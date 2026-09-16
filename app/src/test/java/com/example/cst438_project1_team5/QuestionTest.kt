package com.example.cst438_project1_team5

import data.Question
import org.junit.Assert.assertEquals
import org.junit.Test

class TakeQuizTest {

    @Test
    fun questionTest() {
        var question = Question("dummy")
        assertEquals(false, question.guess("Bobibaba"))
        assertEquals(1, question.guesses)
        question.guess("Connect")
        assertEquals(3, question.duration)
        question.guess("Don't judge my dummy answer names.")
        question.guess("E")
        assertEquals(true, question.guess("Dummy"))
        assertEquals(16, question.duration)
    }
}
