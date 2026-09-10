package com.example.cst438_project1_team5

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        composeRule.setContent {
            CST438Project1Team5Theme {
                AuthScreen()
            }
        }
    }

    @Test
    fun signInScreen_displaysAllLoginFieldsAndActions() {
        composeRule.onNodeWithText("Anime Song Guess").assertIsDisplayed()
        composeRule.onNodeWithText("Ready to guess the next anime tune?").assertIsDisplayed()
        composeRule.onNodeWithText("Sign in to continue your streak").assertIsDisplayed()
        composeRule.onNodeWithText("Email").assertIsDisplayed()
        composeRule.onNodeWithText("Password").assertIsDisplayed()
        composeRule.onNodeWithText("Remember me").assertIsDisplayed()
        composeRule.onNodeWithText("Forgot?").assertIsDisplayed()
        composeRule.onNodeWithText("Sign in").assertIsDisplayed()
        composeRule.onNodeWithText("Don’t have an account?").assertIsDisplayed()
        composeRule.onNodeWithText("Create one").assertIsDisplayed()
    }

    @Test
    fun signInScreen_acceptsUserInput() {
        composeRule.onNodeWithText("Email").performTextInput("student@example.com")
        composeRule.onNodeWithText("student@example.com").assertIsDisplayed()

        composeRule.onNodeWithText("Password").performTextInput("secret123")
        composeRule.onNodeWithText("secret123").assertIsDisplayed()
    }

    @Test
    fun authScreen_navigatesBetweenSignInAndSignUp() {
        composeRule.onNodeWithText("Create one").performClick()

        composeRule.onNodeWithText("Create account").assertIsDisplayed()
        composeRule.onNodeWithText("Join now and track your score").assertIsDisplayed()

        composeRule.onNodeWithText("Sign in").performClick()

        composeRule.onNodeWithText("Sign in to continue your streak").assertIsDisplayed()
        composeRule.onNodeWithText("Create one").assertIsDisplayed()
    }

    @Test
    fun signUpScreen_displaysAllFieldsAndNavigatesBackToLogin() {
        composeRule.setContent {
            CST438Project1Team5Theme {
                SignUpScreen()
            }
        }

        composeRule.onNodeWithText("Create account").assertIsDisplayed()
        composeRule.onNodeWithText("Start your anime music challenge.").assertIsDisplayed()
        composeRule.onNodeWithText("Full name").assertIsDisplayed()
        composeRule.onNodeWithText("Email").assertIsDisplayed()
        composeRule.onNodeWithText("Password").assertIsDisplayed()
        composeRule.onNodeWithText("Confirm password").assertIsDisplayed()
        composeRule.onNodeWithText("Sign up").assertIsDisplayed()
        composeRule.onNodeWithText("Already have an account?").assertIsDisplayed()
        composeRule.onNodeWithText("Sign in").assertIsDisplayed()

        composeRule.onNodeWithText("Full name").performTextInput("Sam Lee")
        composeRule.onNodeWithText("Sam Lee").assertIsDisplayed()

        composeRule.onNodeWithText("Email").performTextInput("sam@example.com")
        composeRule.onNodeWithText("sam@example.com").assertIsDisplayed()

        composeRule.onNodeWithText("Password").performTextInput("password123")
        composeRule.onNodeWithText("password123").assertIsDisplayed()

        composeRule.onNodeWithText("Confirm password").performTextInput("password123")
        composeRule.onNodeWithText("password123").assertIsDisplayed()

        composeRule.onNodeWithText("Sign in").performClick()
        composeRule.onNodeWithText("Sign in to continue your streak").assertIsDisplayed()
    }
}
