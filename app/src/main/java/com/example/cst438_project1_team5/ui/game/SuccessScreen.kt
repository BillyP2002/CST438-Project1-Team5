package com.example.cst438_project1_team5.ui.game

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

/**
 * Plays the censored AnimeThemes video and its embedded theme-song audio as a
 * single media item, so the visual and song cannot drift out of sync.
 */
@Composable
fun SuccessScreen(
    result: GameResult,
    modifier: Modifier = Modifier,
    onBack: () -> Unit = {}
) {
    val context = LocalContext.current
    val player = remember(result.videoUrl) { ExoPlayer.Builder(context).build() }

    LaunchedEffect(player, result.videoUrl) {
        player.setMediaItem(MediaItem.fromUri(result.videoUrl))
        player.prepare()
        player.playWhenReady = true
    }
    DisposableEffect(player) {
        onDispose { player.release() }
    }

    Column(
        modifier = modifier.padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Round complete!",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            textAlign = TextAlign.Center
        )
        Text(
            text = result.animeTitle,
            style = MaterialTheme.typography.titleLarge,
            color = Color(0xFFBAE6FD),
            textAlign = TextAlign.Center
        )
        Text(
            text = "Score: ${result.score} points  •  +${result.animeCoins} Anime Coins",
            color = Color(0xFFFDE68A),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
        Text(
            text = "Censored theme video • song playing",
            color = Color(0xFFCBD5E1),
            style = MaterialTheme.typography.bodyMedium
        )

        AndroidView(
            factory = { viewContext ->
                PlayerView(viewContext).apply {
                    this.player = player
                    useController = true
                }
            },
            update = { it.player = player },
            modifier = Modifier
                .fillMaxWidth()
                .height(230.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))
        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
        ) {
            Text("Back to Home", fontWeight = FontWeight.Bold, color = Color.White)
        }
    }
}
