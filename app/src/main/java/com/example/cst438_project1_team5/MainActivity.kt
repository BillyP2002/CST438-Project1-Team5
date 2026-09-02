package com.example.cst438_project1_team5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cst438_project1_team5.ui.shop.ShopScreen
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CST438Project1Team5Theme {
                ShopScreen()
            }
        }
    }
}
