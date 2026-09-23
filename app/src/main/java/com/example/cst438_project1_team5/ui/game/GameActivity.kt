package com.example.cst438_project1_team5.ui.game

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.text.input.ImeAction
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
import com.example.cst438_project1_team5.database.AppDatabase
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.util.Locale

private val GameText = Color(0xFFF8FAFC)
private val GameMutedText = Color(0xFFBAE6FD)
private val GamePrimary = Color(0xFF06B6D4)
private val GameSecondary = Color(0xFF7C3AED)
private val GameActiveLevel = Color(0xFF22D3EE)
private val GameInactiveLevel = Color(0xFF334155)
private val GameError = Color(0xFFFCA5A5)

/** Data retained for the success screen after the player completes every hint level. */
data class GameResult(
    val score: Int,
    val videoUrl: String,
    val animeTitle: String
) {
    val animeCoins: Int get() = score * 10
}

/**
 * A round starts from a random AnimeThemes video. Its related audio supplies
 * [GameRound.sourceUrl] and its related anime supplies [GameRound.correctAnswer].
 * No answer or URL is hard-coded into the UI.
 */
@Composable
@Suppress("CyclomaticComplexMethod", "LongMethod", "TooGenericExceptionCaught")
fun GameScreen(
    userId: Long,
    modifier: Modifier = Modifier,
    /** Called after the player has revealed every hint level. */
    onFinish: (GameResult) -> Unit = {}
) {
    val context = LocalContext.current
    val database = remember(context) {
        AppDatabase.getInstance(context)
    }

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
    var totalScore by remember { mutableIntStateOf(0) }
    var isRoundSolved by remember { mutableStateOf(false) }
    var malMode by remember { mutableStateOf(false) }

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
            isRoundSolved = false

            try {
                round = if (malMode) {
                    GetVideo.randomMalRound(
                        database = database,
                        userId = userId
                    )
                } else {
                    GetVideo.randomRound()
                }
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

    fun finishGame() {
        audioClipPlayer.pause()
        feedback = "Final score: $totalScore"
        round?.let { completedRound ->
            onFinish(
                GameResult(
                    score = totalScore,
                    videoUrl = completedRound.videoUrl,
                    animeTitle = completedRound.correctAnswer
                )
            )
        }
    }

    LaunchedEffect(Unit) { startNewRound() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 80.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "MAL Mode",
                        color = GameText,
                        fontSize = 18.sp
                    )

                    Text(
                        text = if (malMode)
                            "Only anime from your list"
                        else
                            "Any anime",
                        color = GameMutedText,
                        fontSize = 13.sp
                    )
                }

                Switch(
                    checked = malMode,
                    onCheckedChange = { enabled ->
                        malMode = enabled
                        startNewRound()
                    }
                )
            }
            when {
            isLoadingRound -> Text("Finding a theme…", color = GameText)

            roundError != null -> {
                Text(roundError!!, color = GameError)
                GameButton(onClick = ::startNewRound) { Text("Try Again") }
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
                                    if (!isRoundSolved) {
                                        val earnedPoints = GameScore.pointsFor(levelIndex)
                                        totalScore += earnedPoints
                                        isRoundSolved = true
                                        "Correct! ${round!!.correctAnswer}: +$earnedPoints points."
                                    } else {
                                        "Correct! The anime was ${round!!.correctAnswer}."
                                    }
                                } else {
                                    "Not quite—try another guess or reveal the next hint."
                                }
                            } catch (error: IOException) {
                                Log.w("GameScreen", "Unable to search AnimeThemes", error)
                                val isLocallyCorrect = normalizeTitle(submittedGuess) ==
                                    normalizeTitle(round!!.correctAnswer)
                                feedback = if (isLocallyCorrect) {
                                    if (!isRoundSolved) {
                                        val earnedPoints = GameScore.pointsFor(levelIndex)
                                        totalScore += earnedPoints
                                        isRoundSolved = true
                                        "Correct! ${round!!.correctAnswer}: +$earnedPoints points."
                                    } else {
                                        "Correct! The anime was ${round!!.correctAnswer}."
                                    }
                                } else {
                                    "Couldn't search AnimeThemes. Check your connection and try again."
                                }
                            }
                        }
                    }
                },
                onNewRound = ::startNewRound,
            )
        }
        }

        if (round != null && !isLoadingRound && roundError == null &&
            levelIndex == GameLevels.entries.lastIndex
        ) {
            GameButton(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(0.75f)
                    .padding(bottom = 16.dp)
                    .height(48.dp),
                onClick = ::finishGame,
                colors = ButtonDefaults.buttonColors(
                    containerColor = GameSecondary,
                    contentColor = GameText
                )
            ) { Text("Finish") }
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
    onNewRound: () -> Unit,
) {
    val (cachedFile, loadError) = rememberCachedAudioState(round.sourceUrl, cacheAudio)

    when {
        loadError != null -> {
            Text(loadError, color = GameError)
            GameButton(onClick = onNewRound) { Text("Skip Theme") }
        }

            cachedFile == null -> Text("Downloading the round audio…", color = GameText)

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

            Box(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Difficulty: $currentLevelName", color = GameText, fontSize = 25.sp)
                    Text("Clip length: ${currentLevel.ms / 1_000.0}s", color = GameMutedText)
                    Text("Reveal every level to unlock Finish", color = Color(0xFFFDE68A))

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
                            .background(
                                if (index <= levelIndex) GameActiveLevel else GameInactiveLevel
                            )
                    )
                }
            }

            OutlinedTextField(
                value = guessText,
                onValueChange = onGuessChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
                    .padding(top = 10.dp),
                label = { Text("Your anime guess") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSubmit() }),
                trailingIcon = {
                    TextButton(
                        onClick = onSubmit,
                        enabled = guessText.isNotBlank()
                    ) { Text("Submit") }
                },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = GameText,
                    unfocusedTextColor = GameText,
                    focusedLabelColor = GamePrimary,
                    unfocusedLabelColor = GameMutedText,
                    focusedIndicatorColor = GamePrimary,
                    unfocusedIndicatorColor = GameMutedText,
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    cursorColor = GamePrimary
                )
            )

            feedback?.let {
                Text(it, color = GameText, modifier = Modifier.padding(top = 10.dp))
            }

            GameActions(
                isPlaying = isPlaying,
                isLastHint = levelIndex == GameLevels.entries.lastIndex,
                onPlayPause = {
                    if (isPlaying) {
                        audioClipPlayer.pause()
                    } else {
                        if (player.playbackState == Player.STATE_ENDED) player.seekTo(0)
                        audioClipPlayer.resume()
                    }
                },
                onNextHint = onNextHint,
                onNewRound = onNewRound
            )
        }
    }
}

    }
}

@Composable
private fun GameActions(
    isPlaying: Boolean,
    isLastHint: Boolean,
    onPlayPause: () -> Unit,
    onNextHint: () -> Unit,
    onNewRound: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth().padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GameButton(
                modifier = Modifier.weight(1f).height(48.dp),
                onClick = onNextHint,
                enabled = !isLastHint,
                contentPadding = GameActionContentPadding
            ) { Text("Next Hint") }
            PlayPauseButton(
                modifier = Modifier.weight(1f).height(48.dp),
                isPlaying = isPlaying,
                onClick = onPlayPause
            )
            GameButton(
                modifier = Modifier.weight(1f).height(48.dp),
                onClick = onNewRound,
                contentPadding = GameActionContentPadding
            ) { Text("New Round") }
        }
    }
}

private val GameActionContentPadding = PaddingValues(horizontal = 8.dp)

@Composable
private fun PlayPauseButton(modifier: Modifier, isPlaying: Boolean, onClick: () -> Unit) {
    GameButton(
        modifier = modifier,
        onClick = onClick,
        contentPadding = GameActionContentPadding
    ) {
        Icon(
            imageVector = if (isPlaying) Icons.Filled.Pause else Icons.Filled.PlayArrow,
            contentDescription = if (isPlaying) "Pause" else "Play"
        )
    }
}

@Composable
private fun GameButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    enabled: Boolean = true,
    colors: androidx.compose.material3.ButtonColors = ButtonDefaults.buttonColors(
        containerColor = GamePrimary,
        contentColor = Color(0xFF082F49),
        disabledContainerColor = GameInactiveLevel,
        disabledContentColor = GameMutedText
    ),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    Button(
        modifier = modifier,
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        contentPadding = contentPadding,
        content = content
    )
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
