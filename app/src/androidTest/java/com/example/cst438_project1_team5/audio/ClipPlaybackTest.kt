package com.example.cst438_project1_team5.audio

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ClipPlaybackTest {
    @Test
    fun isPlaying_reflectsPlayerListenerUpdates() {
        val recordingPlayer = RecordingExoPlayer()
        val clipPlayback = ClipPlayback(recordingPlayer.player)

        assertFalse(clipPlayback.isPlaying.value)
        assertNotNull(recordingPlayer.listener)

        recordingPlayer.listener!!.onIsPlayingChanged(true)
        assertTrue(clipPlayback.isPlaying.value)

        recordingPlayer.listener!!.onIsPlayingChanged(false)
        assertFalse(clipPlayback.isPlaying.value)
    }
}
