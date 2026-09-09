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
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.tooling.preview.Preview

private const val PLAYER_NAME = "player"

private data class AvatarOption(
    val id: String,
    val emoji: String
)

private data class FrameOption(
    val id: String,
    val label: String,
    val color: Color
)

private data class StickerOption(
    val id: String,
    val emoji: String
)

private data class BackgroundOption(
    val id: String,
    val label: String,
    val colors: List<Color>
)

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

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun ProfileScreen() {
    var username by rememberSaveable { mutableStateOf(PLAYER_NAME) }
    var selectedAvatarId by rememberSaveable { mutableStateOf("cat") }
    var selectedFrameId by rememberSaveable { mutableStateOf("gold") }
    var selectedStickerId by rememberSaveable { mutableStateOf("sparkles") }
    var selectedBackgroundId by rememberSaveable { mutableStateOf("night") }

    val avatar = avatars.first { it.id == selectedAvatarId }
    val frame = frames.first { it.id == selectedFrameId }
    val sticker = stickers.first { it.id == selectedStickerId }
    val background = backgrounds.first { it.id == selectedBackgroundId }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Customize Profile") }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
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

            Button(
                onClick = {
                    // Later, replace this with a ViewModel/DataStore save call.
                    scope.launch {
                        snackbarHostState.showSnackbar("Profile saved!")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text("Save profile")
            }
        }
    }
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
            fontWeight = FontWeight.SemiBold
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
                    label = { Text(label(option)) }
                )
            }
        }
    }
}