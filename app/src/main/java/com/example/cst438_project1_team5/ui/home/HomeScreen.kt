package com.example.cst438_project1_team5.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.ui.components.ScreenBackground
import com.example.cst438_project1_team5.ui.profile.ProfileScreen
import com.example.cst438_project1_team5.ui.shop.ShopScreen
import com.example.cst438_project1_team5.ui.soundTest.GameRound
import com.example.cst438_project1_team5.ui.soundTest.SoundTestScreen

enum class HomeTab {
    SoundTest,
    Shop,
    Profile
}

@Suppress("LongMethod")
@Composable
fun HomeScreen(
    repository: MusicRepository,
    userId: Long,
    onSignOut: () -> Unit
) {
    var selectedTab by rememberSaveable { mutableStateOf(HomeTab.SoundTest) }

    Scaffold(
        containerColor = Color.Transparent,
        bottomBar = {
            NavigationBar(
                containerColor = Color(0xFF111827).copy(alpha = 0.9f)
            ) {
                NavigationBarItem(
                    selected = selectedTab == HomeTab.SoundTest,
                    onClick = { selectedTab = HomeTab.SoundTest },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
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
                    HomeTab.SoundTest -> {
                        // Using a dummy GameRound for now, this can be hooked up properly later
                        val dummyRound = GameRound(
                            animeTitle = "Naruto",
                            songTitle = "Blue Bird",
                            themeType = "OP",
                            videoUrl = "https://example.com/video.mp4"
                        )
                        SoundTestScreen(
                            round = dummyRound,
                            repository = repository,
                            userId = userId
                        )
                    }
                    HomeTab.Shop -> ShopScreen()
                    HomeTab.Profile -> {
                        ProfileScreen(
                            repository = repository, 
                            userId = userId,
                            onSignOut = onSignOut
                        )
                    }
                }
            }
        }
    }
}
