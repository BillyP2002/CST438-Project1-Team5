package com.example.cst438_project1_team5.ui.soundTest

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import android.net.Uri
import kotlinx.serialization.Serializable

@Serializable
data class AnimeIndexResponse(val anime: List<ApiAnime> = emptyList())

@Serializable
data class ApiAnime(
    val id: Int,
    val name: String,
    val animeThemes: List<ApiTheme> = emptyList()
)

@Serializable
data class ApiTheme(
    val type: String,
    val sequence: Int,
    val song: ApiSong? = null,
    val animethemeentries: List<ApiEntry> = emptyList()
)

@Serializable
data class ApiSong(val title: String? = null)

@Serializable
data class ApiEntry(
    val spoiler: Boolean = false,
    val nsfw: Boolean = false,
    val videos: List<ApiVideo> = emptyList()
)

@Serializable
data class ApiVideo(
    val link: String? = null,
    val filename: String? = null
)

data class GameRound(
    val animeTitle: String,
    val songTitle: String?,
    val themeType: String,
    val videoUrl: String
)

@Composable
fun SoundTestScreen(round: GameRound) {
    val context = LocalContext.current
    val player = remember(context) { ExoPlayer.Builder(context).build() }
    var isPlaying by remember { mutableStateOf(false) }

    LaunchedEffect(round.videoUrl) {
        player.setMediaItem(MediaItem.fromUri(round.videoUrl))
        player.prepare()
        player.play()
        isPlaying = true
    }

    DisposableEffect(player) {
        onDispose {
            player.release()
        }
    }

    Button(onClick = {
        if (player.isPlaying) {
            player.pause()
            isPlaying = false
        } else {
            player.play()
            isPlaying = true
        }
    }) {
        Text(if (isPlaying) "Pause" else "Play")
    }
}
