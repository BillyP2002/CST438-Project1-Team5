package com.example.cst438_project1_team5

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performTextInput
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cst438_project1_team5.database.AppDatabase
import com.example.cst438_project1_team5.database.UserRepository
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AuthScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var userRepository: UserRepository
    private lateinit var database: AppDatabase

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        // Use an in-memory database for testing to ensure isolation and speed
        database = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        userRepository = UserRepository(database.userDao())

        composeRule.setContent {
            CST438Project1Team5Theme(dynamicColor = false) {
                AuthScreen(userRepository = userRepository)
            }
        }
    }

    @After
    fun tearDown() {
        database.close()
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
        composeRule.onNodeWithText("Don't have an account?").assertIsDisplayed()
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

        composeRule.onNodeWithText("Sign in")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()

        composeRule.onNodeWithText("Sign in to continue your streak").assertIsDisplayed()
        composeRule.onNodeWithText("Create one").assertIsDisplayed()
    }

    @Test
    fun signUpScreen_displaysAllFieldsAndNavigatesBackToLogin() {
        // navigate to sign up
        composeRule.onNodeWithText("Create one").performClick()

        composeRule.onNodeWithText("Create account").assertIsDisplayed()
        composeRule.onNodeWithText("Start your anime music challenge.").assertIsDisplayed()
        composeRule.onNodeWithText("Full name").assertIsDisplayed()
        composeRule.onNodeWithText("Email").assertIsDisplayed()
        composeRule.onNodeWithText("Password").assertIsDisplayed()
        composeRule.onNodeWithText("Confirm password").assertIsDisplayed()
        composeRule.onNodeWithText("Sign up").assertIsDisplayed()
        composeRule.onNodeWithText("Already have an account?").assertIsDisplayed()
        composeRule.onNodeWithText("Sign in")
            .performScrollTo()
            .assertIsDisplayed()

        composeRule.onNodeWithText("Full name").performTextInput("Sam Lee")
        composeRule.onNodeWithText("Sam Lee").assertIsDisplayed()

        composeRule.onNodeWithText("Email").performTextInput("sam@example.com")
        composeRule.onNodeWithText("sam@example.com").assertIsDisplayed()

        composeRule.onNodeWithText("Password").performTextInput("password123")

        composeRule.onNodeWithText("Confirm password").performTextInput("password123")

        composeRule.onNodeWithText("Sign in")
            .performScrollTo()
            .assertIsDisplayed()
            .performClick()
        composeRule.onNodeWithText("Sign in to continue your streak").assertIsDisplayed()
    }
}
