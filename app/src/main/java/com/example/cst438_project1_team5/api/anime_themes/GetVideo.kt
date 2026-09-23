package com.example.cst438_project1_team5.api.anime_themes

/** A playable game round assembled entirely from AnimeThemes API values. */
data class GameRound(
    val sourceUrl: String,
    val correctAnswer: String
)

object GetVideo {
    /**
     * Gets a random, censored video and follows its relationships to obtain
     * both the audio URL and the anime title that is the round's answer.
     */
    suspend fun randomRound(): GameRound? {
        val response = RetrofitClient.animeSongApi.getRandomVideo()
        if (!response.isSuccessful) return null

        val video = response.body()?.videos?.firstOrNull() ?: return null
        val audio = video.audio ?: return null
        val anime = video.animethemeentries.firstNotNullOfOrNull {
            it.animetheme?.anime
        } ?: return null

        return GameRound(
            sourceUrl = audio.link,
            correctAnswer = anime.name
        )
    }

    /** Returns canonical AnimeThemes titles matching a player's typed guess. */
    suspend fun searchAnime(query: String): List<Anime> {
        if (query.isBlank()) return emptyList()

        val response = RetrofitClient.animeSongApi.search(query.trim())
        return if (response.isSuccessful) {
            response.body()?.search?.anime.orEmpty()
        } else {
            emptyList()
        }
    }
}
