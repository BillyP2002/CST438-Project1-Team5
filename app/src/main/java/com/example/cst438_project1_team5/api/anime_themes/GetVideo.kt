package com.example.cst438_project1_team5.api.anime_themes

object GetVideo {
    suspend fun correlateVideoToAudio(): AnimeVideo? {
        val response = RetrofitClient.animeSongApi.getVideo()

        return if (response.isSuccessful) {
            response.body()?.videos?.firstOrNull()
        } else {
            null
        }
    }
}
