package com.example.cst438_project1_team5

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.ui.shop.ShopScreen
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ShopScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Before
    fun setUp() {
        composeRule.setContent {
            CST438Project1Team5Theme(dynamicColor = false) {
                ShopScreen()
            }
        }
    }

    @Test
    fun shopScreen_displaysItemsInTwoColumns() {
        composeRule
            .onNodeWithText("Shop")
            .assertIsDisplayed()

        val firstItemBounds = composeRule
            .onNodeWithContentDescription("Open Item 1 details")
            .fetchSemanticsNode()
            .boundsInRoot

        val secondItemBounds = composeRule
            .onNodeWithContentDescription("Open Item 2 details")
            .fetchSemanticsNode()
            .boundsInRoot

        val thirdItemBounds = composeRule
            .onNodeWithContentDescription("Open Item 3 details")
            .fetchSemanticsNode()
            .boundsInRoot

        assertEquals(firstItemBounds.top, secondItemBounds.top, 1f)
        assertTrue(firstItemBounds.left < secondItemBounds.left)
        assertTrue(thirdItemBounds.top > firstItemBounds.top)
    }

    @Test
    fun clickingItem_opensAndCancelsDialog() {
        composeRule
            .onNodeWithContentDescription("Open Item 1 details")
            .performClick()

        composeRule
            .onNodeWithText("Item 1")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Description for item 1")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Cancel")
            .performClick()

        composeRule
            .onNodeWithText("Description for item 1")
            .assertDoesNotExist()

        composeRule
            .onNodeWithText("Cart (0)")
            .assertIsDisplayed()
    }

    @Test
    fun buyingItem_incrementsCartAndClosesDialog() {
        composeRule
            .onNodeWithContentDescription("Open Item 1 details")
            .performClick()

        composeRule
            .onNodeWithText("Buy")
            .performClick()

        composeRule
            .onNodeWithText("Cart (1)")
            .assertIsDisplayed()

        composeRule
            .onNodeWithText("Description for item 1")
            .assertDoesNotExist()
    }
}
