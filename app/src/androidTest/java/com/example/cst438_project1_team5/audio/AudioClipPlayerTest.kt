package com.example.cst438_project1_team5.audio

import androidx.media3.common.MediaItem
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AudioClipPlayerTest {
    @Test
    fun play_setsPreparesAndStartsTheMediaItem() {
        val recordingPlayer = RecordingExoPlayer()
        val audioClipPlayer = AudioClipPlayer(recordingPlayer.player)

        audioClipPlayer.play(MediaItem.Builder().setMediaId("clip").build())

        assertEquals(listOf("setMediaItem", "prepare", "play"), recordingPlayer.calls)
    }

    @Test
    fun stopAndRelease_forwardToThePlayer() {
        val recordingPlayer = RecordingExoPlayer()
        val audioClipPlayer = AudioClipPlayer(recordingPlayer.player)

        audioClipPlayer.stop()
        audioClipPlayer.release()

        assertEquals(listOf("stop", "release"), recordingPlayer.calls)
    }
}
