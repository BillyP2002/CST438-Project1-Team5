package com.example.cst438_project1_team5.api.anime_themes

import okhttp3.Response
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

    /** Basic video lookup retained for the existing video/audio API caller. */
    @GET("video")
    suspend fun getVideo(
        @Query("include") include: String = "audio",
        @Query("filter[uncen]") uncen: Boolean = false
    ): retrofit2.Response<AnimeVideosResponse>

    @GET("video")
    suspend fun getRandomVideo(
        @Query("sort") sort: String = "random",
        @Query("include") include: String = "audio,animethemeentries.animetheme.anime",
        @Query("page[size]") pageSize: Int = 1,
        @Query("filter[uncen]") uncen: Boolean = false
    ): retrofit2.Response<AnimeVideosResponse>

    /**
     * Searches AnimeThemes' content catalog. The game uses this to validate a
     * player's typed anime title against the API's canonical title.
     */
    @GET("search/")
    suspend fun search(
        @Query("q") query: String
    ): retrofit2.Response<AnimeSearchResponse>
}
