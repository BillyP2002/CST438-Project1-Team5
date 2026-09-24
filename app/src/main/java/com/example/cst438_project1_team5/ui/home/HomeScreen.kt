package com.example.cst438_project1_team5.ui.home

import android.content.Context
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.SportsEsports
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.edit
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.ui.components.ScreenBackground
import com.example.cst438_project1_team5.ui.game.GameScreen
import com.example.cst438_project1_team5.ui.game.GameResult
import com.example.cst438_project1_team5.ui.game.SuccessScreen
import com.example.cst438_project1_team5.ui.profile.ProfileScreen
import com.example.cst438_project1_team5.ui.shop.ShopScreen
import com.example.cst438_project1_team5.ui.shop.placeholderShopItems

enum class HomeTab {
    Game,
    Success,
    Shop,
    Profile
}

private const val ECONOMY_PREFS_PREFIX = "anime_economy_"
private const val PREF_ANIME_COIN_BALANCE = "anime_coin_balance"
private const val PREF_OWNED_ITEM_IDS = "owned_item_ids"

@Suppress("LongMethod")
@Composable
fun HomeScreen(
    repository: MusicRepository,
    userId: Long,
    onSignOut: () -> Unit
) {
    val context = LocalContext.current
    val economyPrefs = remember(userId) {
        context.getSharedPreferences("$ECONOMY_PREFS_PREFIX$userId", Context.MODE_PRIVATE)
    }
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.Game) }
    var animeCoinBalance by remember(userId) {
        mutableIntStateOf(economyPrefs.getInt(PREF_ANIME_COIN_BALANCE, 0))
    }
    var ownedItemIds by remember(userId) {
        mutableStateOf(
            economyPrefs.getStringSet(PREF_OWNED_ITEM_IDS, emptySet())
                .orEmpty()
                .mapNotNull(String::toIntOrNull)
        )
    }
    var successResult by remember { mutableStateOf<GameResult?>(null) }
    val ownedProfileEffects = placeholderShopItems
        .filter { it.id in ownedItemIds }
        .map { it.profileEffect }
        .toSet()

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF111827).copy(alpha = 0.9f)
            ) {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Game,
                    onClick = { selectedTab = HomeTab.Game },
                    icon = {
                        Icon(
                            Icons.Default.SportsEsports,
                            contentDescription = "Game"
                        )
                    },
                    label = { Text("Game") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF111827),
                        unselectedIconColor = Color(0xFFCBD5E1),
                        selectedTextColor = Color(0xFF7DD3FC),
                        unselectedTextColor = Color(0xFFCBD5E1),
                        indicatorColor = Color(0xFF7DD3FC)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Shop,
                    onClick = { selectedTab = HomeTab.Shop },
                    icon = { Icon(Icons.Default.ShoppingCart, contentDescription = "Shop") },
                    label = { Text("Shop") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF111827),
                        unselectedIconColor = Color(0xFFCBD5E1),
                        selectedTextColor = Color(0xFF7DD3FC),
                        unselectedTextColor = Color(0xFFCBD5E1),
                        indicatorColor = Color(0xFF7DD3FC)
                    )
                )
                NavigationBarItem(
                    selected = selectedTab == HomeTab.Profile,
                    onClick = { selectedTab = HomeTab.Profile },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF111827),
                        unselectedIconColor = Color(0xFFCBD5E1),
                        selectedTextColor = Color(0xFF7DD3FC),
                        unselectedTextColor = Color(0xFFCBD5E1),
                        indicatorColor = Color(0xFF7DD3FC)
                    )
                )
            }
        }
    ) { innerPadding ->
        ScreenBackground {
            Box(modifier = Modifier.padding(innerPadding)) {
                when (selectedTab) {
                    HomeTab.Game -> GameScreen(
                        userId = userId,
                        onFinish = { result ->
                            animeCoinBalance += result.animeCoins
                            economyPrefs.edit {
                                putInt(PREF_ANIME_COIN_BALANCE, animeCoinBalance)
                            }
                            successResult = result
                            selectedTab = HomeTab.Success
                        }
                    )
                    HomeTab.Success -> successResult?.let { result ->
                        SuccessScreen(
                            result = result,
                            onBack = { selectedTab = HomeTab.Game }
                        )
                    }
                    HomeTab.Shop -> ShopScreen(
                        animeCoinBalance = animeCoinBalance,
                        ownedItemIds = ownedItemIds.toSet(),
                        onPurchase = { item ->
                            if (item.id !in ownedItemIds && animeCoinBalance >= item.animeCoinPrice) {
                                animeCoinBalance -= item.animeCoinPrice
                                ownedItemIds = ownedItemIds + item.id
                                economyPrefs.edit {
                                    putInt(PREF_ANIME_COIN_BALANCE, animeCoinBalance)
                                    putStringSet(
                                        PREF_OWNED_ITEM_IDS,
                                        ownedItemIds.map(Int::toString).toSet()
                                    )
                                }
                                true
                            } else {
                                false
                            }
                        }
                    )
                    HomeTab.Profile -> {
                        ProfileScreen(
                            repository = repository, 
                            userId = userId,
                            purchasedEffects = ownedProfileEffects,
                            onSignOut = onSignOut
                        )
                    }
                }
            }
        }
    }
}
