package com.example.cst438_project1_team5.api.anime_themes

object GetAudioAndVideo {
    suspend fun randomAudio(): Pair<AnimeThemeSong, AnimeVideo?>? {
        val response = RetrofitClient.animeSongApi.getRandomAudio()

        if (!response.isSuccessful) return null
        val audio = response.body()?.songs?.firstOrNull() ?: return null
        val video = audio.videos?.firstOrNull { !it.uncen }

        return audio to video
    }
}
