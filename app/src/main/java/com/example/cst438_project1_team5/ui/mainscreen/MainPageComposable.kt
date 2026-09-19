package com.example.cst438_project1_team5.ui.mainscreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.cst438_project1_team5.SignInScreen
import com.example.cst438_project1_team5.database.AppDatabase
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.ui.profile.ProfileScreen
import com.example.cst438_project1_team5.ui.shop.ShopScreen
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme

class MainPageComposable : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CST438Project1Team5Theme {
                val database = AppDatabase.getInstance(applicationContext)
                val musicRepository = remember {
                    MusicRepository(database)
                }
                AppRoot(repository = musicRepository)
            }
        }
    }
}

@Composable
fun AppRoot(repository: MusicRepository) {
    var currentScreen by rememberSaveable {
        mutableStateOf("main")
    }

    BackHandler(enabled = currentScreen != "main") {
        currentScreen = "main"
    }

    when (currentScreen) {
        "main" -> MainPageScreen(
            onPlay = {
                // currentScreen = "play"
            },
            onProfile = {
                currentScreen = "profile"
            },
            onShop = {
                currentScreen = "shop"
            },
            onConnectToMal = {
                // currentScreen = "mal"
            },
            onLogout = {
                currentScreen = "signIn"
            }
        )

        "profile" -> ProfileScreen(repository = repository)

        "shop" -> ShopScreen()

        "signIn" -> SignInScreen(repository = repository)
    }
}