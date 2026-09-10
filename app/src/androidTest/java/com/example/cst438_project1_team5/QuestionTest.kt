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
        assertEquals(1, question.getGuesses())
        assertEquals(3, question.getGuesses())
        question.guess("Connect")
        question.guess("Don't judge my dummy answer names.")
        question.guess("E")
        assertEquals(16, question.getDuration())
        assertEquals(true, question.guess("Dummy"))
    }
}