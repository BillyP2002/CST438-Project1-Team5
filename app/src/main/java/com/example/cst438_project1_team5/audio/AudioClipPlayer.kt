package com.example.cst438_project1_team5.audio

import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer

class AudioClipPlayer(private val player: ExoPlayer) {
    /**
     * A function that sets up the player with a MediaItem,
     * then plays the item.
     * @param mediaItem MediaItem
     */
    fun play(mediaItem: MediaItem) {
        player.setMediaItem(mediaItem)
        player.prepare()
        player.play()
    }

    /**
     * A function to stop the player.
     */
    fun stop() {
        player.stop()
    }

    /**
     * A function to release the player.
     */
    fun release() {
        player.release()
    }
}