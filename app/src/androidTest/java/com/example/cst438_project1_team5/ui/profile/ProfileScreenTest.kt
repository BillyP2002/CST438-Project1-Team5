package com.example.cst438_project1_team5.ui.profile

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cst438_project1_team5.database.AppDatabase
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProfileScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private lateinit var repository: MusicRepository
    private val testUserId = 1L
    private var isSignOutClicked = false

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        val database = AppDatabase.getInstance(context)
        repository = MusicRepository(database)

        composeRule.setContent {
            CST438Project1Team5Theme {
                ProfileScreen(
                    repository = repository,
                    userId = testUserId,
                    onSignOut = { isSignOutClicked = true }
                )
            }
        }
    }

    @Test
    fun profileScreen_displaysProfileElements() {
        composeRule.onNodeWithText("Customize Profile").assertIsDisplayed()
        composeRule.onNodeWithText("Player name").assertIsDisplayed()
        composeRule.onNodeWithText("Avatar").assertIsDisplayed()
        composeRule.onNodeWithText("Frame").assertIsDisplayed()
        composeRule.onNodeWithText("Sticker").assertIsDisplayed()
        composeRule.onNodeWithText("Background").assertIsDisplayed()
        composeRule.onNodeWithText("My Song List", ignoreCase = true, useUnmergedTree = true).assertExists()
        composeRule.onNodeWithText("Save profile", ignoreCase = true, useUnmergedTree = true).assertExists()
        composeRule.onNodeWithText("Sign Out").assertIsDisplayed()
    }

    @Test
    fun profileScreen_acceptsPlayerNameInput() {
        composeRule.onNodeWithText("Player name", useUnmergedTree = true).performTextInput("TestUser")
        composeRule.onNodeWithText("TestUser", useUnmergedTree = true).assertExists()
    }

    @Test
    fun profileScreen_handlesSignOutClick() {
        composeRule.onNodeWithText("Sign Out").performClick()
        assertTrue("Sign out callback should have been triggered", isSignOutClicked)
    }
}