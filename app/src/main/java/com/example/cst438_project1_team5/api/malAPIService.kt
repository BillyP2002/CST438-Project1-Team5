package com.example.cst438_project1_team5.api

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // Fetch MAL user's list by their username.
    @GET("users/{user_name}/animelist")
    suspend fun getUser(
        @Path("user_name") username: String,
        @Query("limit") limit: Int = 700,
        @Query("offset") offset: Int = 0,
    ): MalAnimeListResponse
}

data class MalAnimeListResponse(
    @SerializedName("data") val data: List<MalAnimeListEntry> = emptyList()
)

data class MalAnimeListEntry(
    @SerializedName("node") val node: MalAnimeNode
)

data class MalAnimeNode(
    @SerializedName("title") val animeTitle: String,
    @SerializedName("status") val status: String,
    @SerializedName("list_score") val listScore: Int? = null
)