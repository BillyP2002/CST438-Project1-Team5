package com.example.cst438_project1_team5.api.anime_themes

import com.example.cst438_project1_team5.database.AppDatabase
import retrofit2.HttpException
import retrofit2.Response

/** A playable game round assembled entirely from AnimeThemes API values. */
data class GameRound(
    val sourceUrl: String,
    val correctAnswer: String,
    /** The filtered AnimeThemes video includes the matching theme-song audio. */
    val videoUrl: String = ""
)

object GetVideo {
    /**
     * Gets a random, censored video and follows its relationships to obtain
     * both the audio URL and the anime title that is the round's answer.
     */
    suspend fun randomRound(): GameRound? {
        val response = RetrofitClient.animeSongApi.getRandomVideo()
        val body = response.successfulBodyOrThrow()

        // Keep this check even though the API request filters `uncen=false` so a
        // malformed response can never reach the success playback screen.
        val video = body?.videos?.firstOrNull { !it.uncen } ?: return null
        val audio = video.audio ?: return null
        val anime = video.animethemeentries.firstNotNullOfOrNull {
            it.animetheme?.anime
        } ?: return null

        return GameRound(
            sourceUrl = audio.link,
            correctAnswer = anime.name,
            videoUrl = video.link
        )
    }

    suspend fun malRound(anime: Anime): GameRound? {
        val response = RetrofitClient.animeSongApi.getVideosByAnimeId(anime.id)
        val body = response.successfulBodyOrThrow()

        val videoList = body?.videos
        val video = videoList?.randomOrNull() ?: return null
        val audio = video.audio ?: return null

        val extractedAnime = video.animethemeentries.firstNotNullOfOrNull {
            it.animetheme?.anime
        } ?: return null

        return GameRound(
            sourceUrl = audio.link,
            correctAnswer = extractedAnime.name
        )
    }

    // Creates a random round from the signed-in user's MAL watchlist.
    suspend fun randomMalRound(
        database: AppDatabase,
        userId: Long
    ): GameRound? {
        val shows = database.malWatchlistDao().getForUser(userId)

        val show = shows.randomOrNull() ?: return null

        val anime = GetAnimeThemeWithMal
            .getAnimeWithMal(show.malAnimeId)
            ?: return null

        return malRound(anime)
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

    private fun <T> Response<T>.successfulBodyOrThrow(): T? {
        if (!isSuccessful) throw HttpException(this)
        return body()
    }
}
