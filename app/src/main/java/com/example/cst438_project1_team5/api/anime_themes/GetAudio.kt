package com.example.cst438_project1_team5.api.anime_themes

object GetAudio {
    suspend fun randomAudio(): AnimeThemeSong? {
        val response = RetrofitClient.animeSongApi.getRandomAudio()

        return if (response.isSuccessful) {
            response.body()?.songs?.firstOrNull()
        } else {
            null
        }
    }
}