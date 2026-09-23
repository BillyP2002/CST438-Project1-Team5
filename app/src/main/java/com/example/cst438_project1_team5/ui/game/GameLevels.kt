package com.example.cst438_project1_team5.ui.game

/**
 * Enum class that defines the 5 levels of the game.
 * Level seconds are returned as millisecond values.
 * Impossible- 0.5s
 * Hard- 1s
 * Medium- 3s
 * Chill- 8s
 * Easy- 15s
 * @param ms Long
 */
enum class GameLevels(val ms: Long) {
    IMPOSSIBLE(500),
    HARD(1000),
    MEDIUM(3000),
    CHILL(8000),
    EASY(15000)
}