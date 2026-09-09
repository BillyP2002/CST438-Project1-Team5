package com.example.cst438_project1_team5.api.anime_themes

class UserService {
    private val retrofit = RetrofitClient.getClient()
    private val userApi = retrofit.create(AnimeThemeSongApi::class.java)
}