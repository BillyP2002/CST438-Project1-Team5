package com.example.cst438_project1_team5.api.anime_themes

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://api.animethemes.moe/"
    private const val PATH = "/song"

    val animeSongApi: AnimeThemeSongApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AnimeThemeSongApi::class.java)
    }
}
