package com.example.cst438_project1_team5

import androidx.test.ext.junit.runners.AndroidJUnit4
import data.Question
import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)

class TakeQuizTest {

    @Test
    fun questionTest(){
        var question = Question("dummy");
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