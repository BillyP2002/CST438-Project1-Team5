package com.example.cst438_project1_team5.ui.game

import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.example.cst438_project1_team5.api.anime_themes.Anime
import com.example.cst438_project1_team5.api.anime_themes.GameRound
import com.example.cst438_project1_team5.api.anime_themes.GetVideo
import com.example.cst438_project1_team5.audio.AudioClipPlayer
import com.example.cst438_project1_team5.audio.ClipPlayback
import com.example.cst438_project1_team5.audio.MediaItemClipBuilder
import com.example.cst438_project1_team5.audio.cache.CacheAudio
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.Locale

/**
 * A round starts from a random AnimeThemes video. Its related audio supplies
 * [GameRound.sourceUrl] and its related anime supplies [GameRound.correctAnswer].
 * No answer or URL is hard-coded into the UI.
 */
@Composable
@Suppress("LongMethod", "TooGenericExceptionCaught")
fun GameScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val cacheAudio = remember(context) { CacheAudio(context.cacheDir) }
    val player = remember(context) { ExoPlayer.Builder(context).build() }
    val audioClipPlayer = remember(player) { AudioClipPlayer(player) }
    val clipPlayback = remember(player) { ClipPlayback(player) }
    val isPlaying by clipPlayback.isPlaying.collectAsState()

    var round by remember { mutableStateOf<GameRound?>(null) }
    var roundError by remember { mutableStateOf<String?>(null) }
    var isLoadingRound by remember { mutableStateOf(true) }
    var levelIndex by remember { mutableIntStateOf(0) }
    var guessText by remember { mutableStateOf("") }
    var feedback by remember { mutableStateOf<String?>(null) }

    DisposableEffect(audioClipPlayer) {
        onDispose { audioClipPlayer.release() }
    }

    fun startNewRound() {
        scope.launch {
            audioClipPlayer.stop()
            isLoadingRound = true
            roundError = null
            feedback = null
            guessText = ""
            levelIndex = 0

            try {
                round = GetVideo.randomRound()
                if (round == null) {
                    roundError = "AnimeThemes could not provide a playable round. Try again."
                }
            } catch (error: IOException) {
                Log.w("GameScreen", "Unable to load an AnimeThemes round", error)
                roundError = "Couldn't reach AnimeThemes. Check your connection and try again."
            } catch (error: RuntimeException) {
                Log.e("GameScreen", "Unexpected game-round response", error)
                roundError = "Couldn't start a round: ${error.message ?: "unknown error"}"
            } finally {
                isLoadingRound = false
            }
        }
    }

    LaunchedEffect(Unit) { startNewRound() }

    Column(modifier = modifier.padding(16.dp)) {
        when {
            isLoadingRound -> Text("Finding a theme…")

            roundError != null -> {
                Text(roundError!!, color = Color.Red)
                Button(onClick = ::startNewRound) { Text("Try Again") }
            }

            round != null -> GameRoundContent(
                round = round!!,
                cacheAudio = cacheAudio,
                audioClipPlayer = audioClipPlayer,
                player = player,
                isPlaying = isPlaying,
                levelIndex = levelIndex,
                onNextHint = { levelIndex += 1 },
                guessText = guessText,
                onGuessChange = {
                    guessText = it
                    feedback = null
                },
                feedback = feedback,
                onSubmit = {
                    val submittedGuess = guessText
                    if (submittedGuess.isBlank()) {
                        feedback = "Enter an anime title first."
                    } else {
                        scope.launch {
                            try {
                                // Search supplies canonical titles, avoiding a fragile raw-string check.
                                val matches = GetVideo.searchAnime(submittedGuess)
                                feedback = if (isCorrectGuess(submittedGuess, round!!, matches)) {
                                    audioClipPlayer.pause()
                                    "Correct! The anime was ${round!!.correctAnswer}."
                                } else {
                                    "Not quite—try another guess or reveal the next hint."
                                }
                            } catch (error: IOException) {
                                Log.w("GameScreen", "Unable to search AnimeThemes", error)
                                val isLocallyCorrect = normalizeTitle(submittedGuess) ==
                                    normalizeTitle(round!!.correctAnswer)
                                feedback = if (isLocallyCorrect) {
                                    "Correct! The anime was ${round!!.correctAnswer}."
                                } else {
                                    "Couldn't search AnimeThemes. Check your connection and try again."
                                }
                            }
                        }
                    }
                },
                onNewRound = ::startNewRound
            )
        }
    }
}

@Composable
@Suppress("LongMethod")
private fun GameRoundContent(
    round: GameRound,
    cacheAudio: CacheAudio,
    audioClipPlayer: AudioClipPlayer,
    player: ExoPlayer,
    isPlaying: Boolean,
    levelIndex: Int,
    onNextHint: () -> Unit,
    guessText: String,
    onGuessChange: (String) -> Unit,
    feedback: String?,
    onSubmit: () -> Unit,
    onNewRound: () -> Unit
) {
    val (cachedFile, loadError) = rememberCachedAudioState(round.sourceUrl, cacheAudio)

    when {
        loadError != null -> {
            Text(loadError, color = Color.Red)
            Button(onClick = onNewRound) { Text("Skip Theme") }
        }

        cachedFile == null -> Text("Downloading the round audio…")

        else -> {
            val clipBuilder = remember(cachedFile) {
                MediaItemClipBuilder(Uri.fromFile(cachedFile))
            }
            val currentLevel = GameLevels.entries[levelIndex]
            val currentLevelName = currentLevel.name.lowercase()
                .replaceFirstChar { it.titlecase(Locale.ROOT) }

            // Each hint reuses the cached source file and changes only the clip end.
            LaunchedEffect(cachedFile, levelIndex) {
                audioClipPlayer.play(clipBuilder.generateMediaItemFromGameLevel(currentLevel))
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Difficulty: $currentLevelName", fontSize = 25.sp)
                Text("Clip length: ${currentLevel.ms / 1_000.0}s")
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                for (index in GameLevels.entries.indices.reversed()) {
                    Box(
                        modifier = Modifier
                            .size(width = 80.dp, height = (40 + index * 20).dp)
                            .background(if (index < levelIndex) Color.Green else Color.LightGray)
                    )
                }
            }

            OutlinedTextField(
                value = guessText,
                onValueChange = onGuessChange,
                modifier = Modifier.fillMaxWidth(),
                label = { Text("Your anime guess") },
                singleLine = true
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = onSubmit) { Text("Submit") }
                Button(
                    onClick = {
                        if (isPlaying) {
                            audioClipPlayer.pause()
                        } else {
                            if (player.playbackState == Player.STATE_ENDED) player.seekTo(0)
                            audioClipPlayer.resume()
                        }
                    }
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play"
                    )
                }
                Button(
                    onClick = onNextHint,
                    enabled = levelIndex < GameLevels.entries.lastIndex
                ) { Text("Next Hint") }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(onClick = onNewRound) { Text("New Round") }
            }

            feedback?.let { Text(it, modifier = Modifier.padding(top = 10.dp)) }
        }
    }
}

private fun isCorrectGuess(guess: String, round: GameRound, searchResults: List<Anime>): Boolean {
    val answer = normalizeTitle(round.correctAnswer)
    return normalizeTitle(guess) == answer ||
        searchResults.any { normalizeTitle(it.name) == answer }
}

private fun normalizeTitle(title: String): String =
    title.lowercase(Locale.ROOT).filter(Char::isLetterOrDigit)

/** Safely downloads the current round's source file into the app cache. */
@Composable
fun rememberCachedAudioState(sourceUrl: String, cacheAudio: CacheAudio): Pair<File?, String?> {
    var cachedFile by remember(sourceUrl) { mutableStateOf<File?>(null) }
    var loadError by remember(sourceUrl) { mutableStateOf<String?>(null) }

    LaunchedEffect(sourceUrl, cacheAudio) {
        try {
            cachedFile = cacheAudio.getOrFetch(sourceUrl)
        } catch (error: IOException) {
            loadError = "Couldn't load this clip: ${error.message ?: "network error"}"
        }
    }

    return cachedFile to loadError
}
