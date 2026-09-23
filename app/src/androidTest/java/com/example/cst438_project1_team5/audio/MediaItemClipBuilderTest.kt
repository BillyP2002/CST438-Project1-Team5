package com.example.cst438_project1_team5.audio

import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.cst438_project1_team5.ui.game.GameLevels
import org.junit.Assert.assertEquals
import org.junit.Assert.assertSame
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MediaItemClipBuilderTest {
    private val uri = Uri.parse("file:///cache/theme.mp3")
    private val builder = MediaItemClipBuilder(uri)

    @Test
    fun createMediaItem_usesGivenUriAndClipBounds() {
        val mediaItem = builder.createMediaItem(startMs = 1_000, endMs = 5_000)

        assertSame(uri, mediaItem.localConfiguration?.uri)
        assertEquals(1_000, mediaItem.clippingConfiguration.startPositionMs)
        assertEquals(5_000, mediaItem.clippingConfiguration.endPositionMs)
    }

    @Test
    fun createMediaItem_usesZeroAsTheDefaultStartPosition() {
        val mediaItem = builder.createMediaItem(endMs = 2_000)

        assertEquals(0, mediaItem.clippingConfiguration.startPositionMs)
        assertEquals(2_000, mediaItem.clippingConfiguration.endPositionMs)
    }

    @Test
    fun generateMediaItemFromGameLevel_usesTheLevelsClipEnd() {
        val level = GameLevels.entries.first()

        val mediaItem = builder.generateMediaItemFromGameLevel(level)

        assertEquals(level.ms, mediaItem.clippingConfiguration.endPositionMs)
    }
}
