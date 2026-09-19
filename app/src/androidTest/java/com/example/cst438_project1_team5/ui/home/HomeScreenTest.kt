package com.example.cst438_project1_team5.ui.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.v2.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.cst438_project1_team5.database.AppDatabase
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

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
                HomeScreen(
                    repository = repository,
                    userId = testUserId,
                    onSignOut = { isSignOutClicked = true }
                )
            }
        }
    }

    @Test
    fun homeScreen_displaysBottomNavigationItems() {
        composeRule.onNodeWithText("Home").assertIsDisplayed()
        composeRule.onNodeWithText("Shop").assertIsDisplayed()
        composeRule.onNodeWithText("Profile").assertIsDisplayed()
    }

    @Test
    fun homeScreen_navigatesToShopTab() {
        // click on Shop tab
        composeRule.onNode(hasText("Shop") and hasClickAction(), useUnmergedTree = true).performClick()
        
        // Assert Shop screen is displayed by looking for Shop title
        composeRule.onNode(hasText("Anime Coin:")).assertExists()
    }

    @Test
    fun homeScreen_navigatesToProfileTab() {
        // click on Profile tab
        composeRule.onNode(hasText("Profile") and hasClickAction(), useUnmergedTree = true).performClick()
        
        // Assert Profile screen is displayed by looking for its title
        composeRule.onNodeWithText("Customize Profile").assertIsDisplayed()
    }
}
