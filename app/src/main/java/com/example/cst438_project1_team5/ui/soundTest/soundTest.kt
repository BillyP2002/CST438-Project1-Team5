package com.example.cst438_project1_team5.ui.soundTest
import android.media.browse.MediaBrowser
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.serialization.Serializable
class soundTest {

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
        val type: String,                 // "OP" or "ED"
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
    fun SoundTestScreen(round: GameRound, fromUri: Unit.(String) -> MediaItem) {
        val context = LocalContext.current

        val player = remember {
            ExoPlayer.Builder(context).build()
        }
        LaunchedEffect(round.videoUrl) {
            player.setMediaItem(MediaBrowser.MediaItem.fromUri(round.videoUrl))
            player.prepare()
            player.play()
        }

        DisposableEffect(Unit) {
            onDispose {
                player.release()
            }
        }

        Button(onClick = {
            if (player.isPlaying) player.pause() else player.play()
        }) {
            Text(if (player.isPlaying) "Pause" else "Play")
        }
    }

}