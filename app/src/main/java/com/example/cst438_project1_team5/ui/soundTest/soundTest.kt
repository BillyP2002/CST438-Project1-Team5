package com.example.cst438_project1_team5.ui.soundTest

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import com.example.cst438_project1_team5.database.MusicDatabaseHelper
import com.example.cst438_project1_team5.database.PastChallengeSong
import kotlinx.serialization.Serializable
import java.time.LocalDate

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
fun SoundTestScreen(
    round: GameRound,
    databaseHelper: MusicDatabaseHelper = MusicDatabaseHelper(LocalContext.current),
    userId: Long? = null,
    challengeDate: String = LocalDate.now().toString()
) {
    val context = LocalContext.current
    val player = remember(context) { ExoPlayer.Builder(context).build() }
    var isPlaying by remember { mutableStateOf(false) }
    var challengeHistory by remember { mutableStateOf<List<PastChallengeSong>>(emptyList()) }
    var hasRecordedSong by remember { mutableStateOf(false) }

    LaunchedEffect(userId, challengeDate) {
        if (userId != null) {
            challengeHistory = databaseHelper.getPastPlayedSongsForChallenge(userId, challengeDate)
        }
    }

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

    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = {
            if (player.isPlaying) {
                player.pause()
                isPlaying = false
            } else {
                player.play()
                isPlaying = true
            }

            if (userId != null && !hasRecordedSong && round.songTitle != null) {
                databaseHelper.recordDailyChallengeSong(
                    userId = userId,
                    challengeDate = challengeDate,
                    songId = round.songTitle ?: "unknown-song",
                    title = round.songTitle ?: "Unknown song",
                    artist = "Daily challenge",
                    album = null
                )
                hasRecordedSong = true
                challengeHistory = databaseHelper.getPastPlayedSongsForChallenge(userId, challengeDate)
            }
        }) {
            Text(if (isPlaying) "Pause" else "Play")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF111827))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Daily challenge history",
                    style = MaterialTheme.typography.titleMedium,
                    color = Color.White
                )

                if (challengeHistory.isEmpty()) {
                    Text("No songs recorded yet for this challenge.", color = Color(0xFFCBD5E1))
                } else {
                    challengeHistory.forEach { song ->
                        Text(
                            text = "• ${song.title} by ${song.artist}",
                            color = Color(0xFFE2E8F0),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}
