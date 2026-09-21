package com.example.cst438_project1_team5.ui.gameAudio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.cst438_project1_team5.api.anime_themes.AnimeThemeSong
import com.example.cst438_project1_team5.api.anime_themes.AnimeVideo
import com.example.cst438_project1_team5.api.anime_themes.GetAudioAndVideo
import kotlinx.coroutines.runBlocking

class PlayAudioActivity : ComponentActivity() {
    private var player: ExoPlayer? = null
    private lateinit var playerView: PlayerView

    /**
     * Function to create a slice of the anime song that's playing.
     * Starts where the last bit of the song played, and plays the whole thing.
     * Ex. 0-0.5s, then 0-1s, then 0-3s (pulls from different MediaItem every time).
     * returns MediaItem
     */
    private fun createMediaItem(level: GameLevels): MediaItem {
        MediaItem.Builder()
            .setUri(cachedFileUri)
            .setClippingConfiguration(
                MediaItem.ClippingConfiguration.Builder()
                    .setStartPositionMs(0)
                    .setEndPositionMs(level.ms)
                    .build()
            )
            .build()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        var randomLink: String?
        runBlocking { // coroutine builder
            randomLink = getRandomAudio()
        }

        super.onCreate(savedInstanceState)
        setContent {
            defaultGameView()
        }
    }

    /**
     * Enum class that defines the 5 levels of the game.
     * Level seconds are returned as millisecond values.
     * Impossible- 0.5s
     * Hard- 1s
     * Medium- 3s
     * Chill- 8s
     * Easy- 15s
     */
    enum class GameLevels(val ms: Long) {
        IMPOSSIBLE(500),
        HARD(1000),
        MEDIUM(3000),
        CHILL(8000),
        EASY(15000)
    }

    /**
     * Composable function that builds the game. Enables simple guesses
     * and has functionality for game repetition.
     */
    @Composable
    fun defaultGameView(
        song: MediaItem,
    ) {


    }
}