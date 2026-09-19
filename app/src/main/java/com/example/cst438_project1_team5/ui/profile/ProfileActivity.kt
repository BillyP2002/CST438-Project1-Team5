package com.example.cst438_project1_team5.ui.profile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.room.Room
import com.example.cst438_project1_team5.database.AppDatabase
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.ui.theme.CST438Project1Team5Theme

class ProfileActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "music_database"
        ).build()
        val repository = MusicRepository(database)

        setContent {
            CST438Project1Team5Theme {
                ProfileScreen(repository = repository)
            }
        }
    }
}