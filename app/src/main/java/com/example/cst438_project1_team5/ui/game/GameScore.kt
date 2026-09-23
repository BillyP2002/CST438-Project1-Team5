package com.example.cst438_project1_team5.ui.game

/**
 * Contract for the score-screen activity that will be supplied later.
 * The receiving activity can read [EXTRA_SCORE] from its launch Intent.
 */
object GameScoreContract {
    const val EXTRA_SCORE = "com.example.cst438_project1_team5.extra.GAME_SCORE"
}

/** Scoring rewards answering with fewer revealed seconds of the theme. */
object GameScore {
    fun pointsFor(levelIndex: Int): Int =
        (GameLevels.entries.size - levelIndex).coerceAtLeast(1)
}
