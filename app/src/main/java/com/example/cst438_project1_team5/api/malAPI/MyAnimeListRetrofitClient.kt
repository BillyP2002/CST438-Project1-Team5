package com.example.cst438_project1_team5.api.malAPI

import android.util.Base64
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.security.SecureRandom
import kotlin.jvm.java

object MyAnimeListRetrofitClient {
    private const val BASE_URL = "https://api.myanimelist.net/v2/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    val api: MalAPIService = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(MalAPIService::class.java)

    //Necessary to obtain an OAuth token, which is necessary as a whole for the MAL API.
    //Code verifier is identical to the code challenge under MAL API.
    fun getCodeVerifier() : String{
        val bytes = ByteArray(32)
        SecureRandom().nextBytes(bytes)

        return Base64.encodeToString(
            bytes,
            Base64.URL_SAFE or Base64.NO_WRAP or Base64.NO_PADDING
        )
    }
}
