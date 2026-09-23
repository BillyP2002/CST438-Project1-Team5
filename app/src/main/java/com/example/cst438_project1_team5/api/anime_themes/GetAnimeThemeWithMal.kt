package com.example.cst438_project1_team5.api.anime_themes

object GetAnimeThemeWithMal {

    /**
     * Get an AnimeThemes anime; You can use the ID from here to get specific songs for MAL mode
     */
    suspend fun getAnimeWithMal(malId: Int): Anime? {
        val response = RetrofitClient.animeSongApi.getAnimeResourceByMalId(malId)
        return response.resources.getOrNull(0)?.anime?.getOrNull(0)
    }
}