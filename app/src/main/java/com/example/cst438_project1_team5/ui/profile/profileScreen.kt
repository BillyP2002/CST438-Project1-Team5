package com.example.cst438_project1_team5.ui.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TextFieldDefaults
import com.example.cst438_project1_team5.ui.components.ScreenBackground
import com.example.cst438_project1_team5.database.AppDatabase
import com.example.cst438_project1_team5.database.MusicRepository
import com.example.cst438_project1_team5.database.SongListEntry
import kotlinx.coroutines.launch

private const val PLAYER_NAME = "player"

private data class AvatarOption(val id: String, val emoji: String)

private data class FrameOption(val id: String, val label: String, val color: Color)

private data class StickerOption(val id: String, val emoji: String)

private data class BackgroundOption(val id: String, val label: String, val colors: List<Color>)

private val avatars = listOf(
    AvatarOption("cat", "🐱"),
    AvatarOption("fox", "🦊"),
    AvatarOption("ghost", "👻"),
    AvatarOption("star", "⭐")
)

private val frames = listOf(
    FrameOption("none", "None", Color.Transparent),
    FrameOption("gold", "Gold", Color(0xFFFFC107)),
    FrameOption("purple", "Purple", Color(0xFF9C6ADE)),
    FrameOption("aqua", "Aqua", Color(0xFF00B8D9))
)

private val stickers = listOf(
    StickerOption("none", ""),
    StickerOption("fire", "🔥"),
    StickerOption("heart", "💖"),
    StickerOption("sparkles", "✨")
)

private val backgrounds = listOf(
    BackgroundOption(
        "night",
        "Night",
        listOf(Color(0xFF141E30), Color(0xFF243B55))
    ),
    BackgroundOption(
        "sunset",
        "Sunset",
        listOf(Color(0xFFFF512F), Color(0xFFDD2476))
    ),
    BackgroundOption(
        "forest",
        "Forest",
        listOf(Color(0xFF134E5E), Color(0xFF71B280))
    )
)

@Suppress("LongMethod")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    repository: MusicRepository, 
    userId: Long? = null,
    onSignOut: () -> Unit = {}
) {
    var username by rememberSaveable { mutableStateOf(PLAYER_NAME) }
    var selectedAvatarId by rememberSaveable { mutableStateOf("cat") }
    var selectedFrameId by rememberSaveable { mutableStateOf("gold") }
    var selectedStickerId by rememberSaveable { mutableStateOf("sparkles") }
    var selectedBackgroundId by rememberSaveable { mutableStateOf("night") }
    var songTitle by rememberSaveable { mutableStateOf("") }
    var songArtist by rememberSaveable { mutableStateOf("") }
    var songList by remember {
        mutableStateOf<List<SongListEntry>>(emptyList())
    }

    val avatar = avatars.first { it.id == selectedAvatarId }
    val frame = frames.first { it.id == selectedFrameId }
    val sticker = stickers.first { it.id == selectedStickerId }
    val background = backgrounds.first { it.id == selectedBackgroundId }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(userId) {
        if (userId != null) {
            songList = repository.getUserSongList(userId)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customize Profile", color = Color.White) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White
                ),
                actions = {
                    TextButton(onClick = onSignOut) {
                        Text("Sign Out", color = Color(0xFFFCA5A5))
                    }
                }
            )
        },
        containerColor = Color.Transparent,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        ScreenBackground {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ProfilePreview(
                    username = username.ifBlank { "Player" },
                    avatar = avatar,
                    frame = frame,
                    sticker = sticker,
                    background = background
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it.take(20) },
                    label = { Text("Player name") },
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF7DD3FC),
                        unfocusedIndicatorColor = Color(0xFF475569),
                        focusedLabelColor = Color(0xFF7DD3FC),
                        unfocusedLabelColor = Color(0xFFCBD5E1)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                CustomizationRow(
                    title = "Avatar",
                    options = avatars,
                    selectedId = selectedAvatarId,
                    label = { it.emoji },
                    onSelected = { selectedAvatarId = it }
                )

                CustomizationRow(
                    title = "Frame",
                    options = frames,
                    selectedId = selectedFrameId,
                    label = { it.label },
                    onSelected = { selectedFrameId = it }
                )

                CustomizationRow(
                    title = "Sticker",
                    options = stickers,
                    selectedId = selectedStickerId,
                    label = { if (it.emoji.isBlank()) "None" else it.emoji },
                    onSelected = { selectedStickerId = it }
                )

                CustomizationRow(
                    title = "Background",
                    options = backgrounds,
                    selectedId = selectedBackgroundId,
                    label = { it.label },
                    onSelected = { selectedBackgroundId = it }
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF111827).copy(alpha = 0.9f))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "My Song List",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )

                        OutlinedTextField(
                            value = songTitle,
                            onValueChange = { songTitle = it },
                            label = { Text("Song title") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF7DD3FC),
                                unfocusedIndicatorColor = Color(0xFF475569),
                                focusedLabelColor = Color(0xFF7DD3FC),
                                unfocusedLabelColor = Color(0xFFCBD5E1)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = songArtist,
                            onValueChange = { songArtist = it },
                            label = { Text("Artist") },
                            singleLine = true,
                            shape = RoundedCornerShape(16.dp),
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.White,
                                unfocusedTextColor = Color.White,
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color(0xFF7DD3FC),
                                unfocusedIndicatorColor = Color(0xFF475569),
                                focusedLabelColor = Color(0xFF7DD3FC),
                                unfocusedLabelColor = Color(0xFFCBD5E1)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )

                    Button(
                        onClick = {
                            if (userId != null && songTitle.isNotBlank() &&
                                songArtist.isNotBlank()
                            ) {
                                scope.launch {
                                    repository.addSongToUserList(
                                        userId = userId,
                                        songId = "manual_${System.currentTimeMillis()}",
                                        title = songTitle.trim(),
                                        artist = songArtist.trim()
                                    )
                                    songList = repository.getUserSongList(userId)
                                    songTitle = ""
                                    songArtist = ""
                                    snackbarHostState.showSnackbar("Song saved to your list")
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED))
                    ) {
                        Text("Add to my song list")
                    }

                    if (songList.isEmpty()) {
                        Text(
                            text = "No songs saved yet.",
                            color = Color(0xFFCBD5E1)
                        )
                    } else {
                        songList.forEach { song ->
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E293B))
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(12.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            song.title,
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White
                                        )
                                        Text(song.artist, color = Color(0xFFCBD5E1))
                                    }
                                    if (song.isFavorite) {
                                        Text("★", color = Color(0xFFFBBF24))
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Button(
                onClick = {
                    scope.launch {
                        snackbarHostState.showSnackbar("Profile saved!")
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .padding(bottom = 8.dp)
            ) {
                Text(
                    "Save profile",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    val context = LocalContext.current
    val database = AppDatabase.getInstance(context)
    val repository = MusicRepository(database)
    ProfileScreen(repository = repository, userId = 1L)
}

@Composable
private fun ProfilePreview(
    username: String,
    avatar: AvatarOption,
    frame: FrameOption,
    sticker: StickerOption,
    background: BackgroundOption
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(210.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Brush.linearGradient(background.colors)),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(contentAlignment = Alignment.TopEnd) {
                    Text(
                        text = avatar.emoji,
                        fontSize = 54.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.9f))
                            .border(
                                width = if (frame.id == "none") 0.dp else 6.dp,
                                color = frame.color,
                                shape = CircleShape
                            )
                            .padding(18.dp)
                    )

                    if (sticker.emoji.isNotBlank()) {
                        Text(
                            text = sticker.emoji,
                            fontSize = 28.sp
                        )
                    }
                }

                Text(
                    text = username,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    }
}

@Composable
private fun <T> CustomizationRow(
    title: String,
    options: List<T>,
    selectedId: String,
    label: (T) -> String,
    onSelected: (String) -> Unit
) where T : Any {
    // Each option type currently has an `id`; this extracts it safely.
    fun idOf(option: T): String = when (option) {
        is AvatarOption -> option.id
        is FrameOption -> option.id
        is StickerOption -> option.id
        is BackgroundOption -> option.id
        else -> error("Unsupported customization option")
    }

    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            color = Color.White
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(end = 8.dp)
        ) {
            items(options) { option ->
                val optionId = idOf(option)

                FilterChip(
                    selected = selectedId == optionId,
                    onClick = { onSelected(optionId) },
                    label = { Text(label(option), color = if (selectedId == optionId) Color(0xFF111827) else Color.White) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color.Transparent,
                        labelColor = Color.White,
                        selectedContainerColor = Color(0xFF7DD3FC),
                        selectedLabelColor = Color(0xFF111827)
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = Color(0xFF7DD3FC),
                        selectedBorderColor = Color(0xFF7DD3FC),
                        enabled = true,
                        selected = selectedId == optionId
                    )
                )
            }
        }
    }
}
