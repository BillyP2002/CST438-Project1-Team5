package com.example.cst438_project1_team5.api.malapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object MalOAuthClient {

    private const val BASE_URL = "https://myanimelist.net/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val api: MalOAuthService =
        retrofit.create(MalOAuthService::class.java)
}
