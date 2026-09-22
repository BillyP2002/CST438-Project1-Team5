package com.example.cst438_project1_team5.api.anime_themes

import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AnimeThemeSongApi {
    @GET("audio/")
    suspend fun getRandomAudio(
        @Query("sort") sort: String = "random",
        @Query("include") include: String = "videos",
        @Query("page[size]") pageSize: Int = 1
    ): retrofit2.Response<AnimeThemeSongsResponse>

    @GET("audio/{basename}")
    suspend fun getSong(
        @Path("basename") basename: String
    ): retrofit2.Response<AnimeThemeSongResponse>

    @GET("video")
    suspend fun getVideo(
        @Query("include") include: String = "audio",
        @Query("filter[uncen]") uncen: Boolean = false
    ): retrofit2.Response<AnimeVideosResponse>

    @GET("resource")
    suspend fun getAnimeByMalId(
        @Query("filter[external_id]") malId: Int,
        @Query("filter[site]") site: String = "myanimelist",
        @Query("include") include: String = "anime"
    ): ResourceResponse
}
