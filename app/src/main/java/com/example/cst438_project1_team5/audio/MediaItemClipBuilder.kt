package com.example.cst438_project1_team5.audio

import android.net.Uri
import androidx.media3.common.MediaItem
import com.example.cst438_project1_team5.ui.gameAudio.GameLevels

class MediaItemClipBuilder(private val cachedFileUri: Uri) {

    /**
     * A function that creates a new MediaItem each segment of the game
     * that the player progresses.
     * @param uri Uri
     * @param startMs Long (Default 0- beginning of clip)
     * @param endMs Long (No default)
     * @return MediaItem
     */
    fun createMediaItem(uri: Uri = cachedFileUri, startMs: Long = 0, endMs: Long): MediaItem {
        val mediaItem =
            MediaItem.Builder()
                .setUri(uri)
                .setClippingConfiguration(
                    MediaItem.ClippingConfiguration.Builder()
                        .setStartPositionMs(startMs)
                        .setEndPositionMs(endMs)
                        .build()
                )
                .build()
        return mediaItem
    }

    fun generateMediaItemFromGameLevel(level: GameLevels): MediaItem {
        return createMediaItem(endMs = level.ms)
    }
}