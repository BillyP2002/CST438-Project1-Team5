package com.example.cst438_project1_team5.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CST438Project1Team5Theme {
                ProfileScreen()
            }
        }
    }
}