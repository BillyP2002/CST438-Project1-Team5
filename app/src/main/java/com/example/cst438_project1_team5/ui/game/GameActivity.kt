package com.example.cst438_project1_team5.ui.game

import android.icu.lang.UCharacter.toLowerCase
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.cst438_project1_team5.audio.cache.CacheAudio
import okio.IOException
import java.io.File

class GameActivity : ComponentActivity() {

    /**
     * Safely returns the cached audio file.
     * @param sourceUrl String
     * @param cacheAudio CacheAudio
     * @return Pair<File?, String?>
     */
    @Composable
    fun getCachedAudio(sourceUrl: String, cacheAudio: CacheAudio): Pair<File?, String?> {
        var cachedFile by remember { mutableStateOf<File?>(null) }
        var loadError by remember { mutableStateOf<String?>(null) }

        LaunchedEffect(sourceUrl) {
            try {
                cachedFile = cacheAudio.getOrFetch(sourceUrl)
            } catch (e: IOException) {
                loadError = "Error: Couldn't load clip ${e.message}"
            }
        }

        return cachedFile to loadError
    }

    @Preview(showBackground = true)
    @Composable
    fun GameScreen() {
        var levelIndex by remember { mutableIntStateOf(0) }
        var guessText by remember { mutableStateOf("") }
        var feedback by remember { mutableStateOf<String?>(null) }
        var isPlaying by remember { mutableStateOf(false) }

        val currentLevel = GameLevels.entries[levelIndex]

        val currentLevelName = toLowerCase(currentLevel.name).replaceFirstChar { it.uppercase() }

        Column {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(5.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

            ) {
                Text(
                    text = "Difficulty: $currentLevelName",
                    fontSize = 25.sp,
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (i in GameLevels.entries.indices.reversed()) {
                    Box(
                        modifier = Modifier
                        .size(width = 80.dp, height = (40 + i * 20).dp)
                        .background(if (i < levelIndex) Color.Green else Color.LightGray)
                    )
                }
            }

            OutlinedTextField(
                value = guessText,
                onValueChange = { guessText = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                label = { Text("Your guess") }
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { /* TODO: Check if answer correct */}
                ) {
                    Text("Submit")
                }
                Button(
                    onClick = { /* TODO: Wire up to pause & play music */ }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                Button(
                    onClick = { levelIndex++ },
                    enabled = levelIndex < GameLevels.entries.lastIndex
                ) {
                    Text("Next Hint")
                }
            }
        }

        feedback?.let { Text(it) }
    }
}