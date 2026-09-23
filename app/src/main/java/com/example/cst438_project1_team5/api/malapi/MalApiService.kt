package com.example.cst438_project1_team5.api.malapi

import com.google.gson.annotations.SerializedName
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

interface MalAPIService {
    // Fetch MAL user's list by their username.
    @GET("users/@me/animelist")
    suspend fun getUser(
        @Query("fields") fields: String = "list_status",
        @Query("limit") limit: Int = 700,
        @Query("offset") offset: Int = 0,
        @Header("Authorization") authorization : String
    ): MalAnimeListResponse
}

data class MalAnimeListResponse(
    @SerializedName("data") val data: List<MalAnimeListEntry> = emptyList()
)

data class MalAnimeListEntry(
    @SerializedName("node")
    val node: MalAnimeNode,

    @SerializedName("list_status")
    val listStatus: MalListStatus?
)

data class MalAnimeNode(
    @SerializedName("id") val animeId: Int,
    @SerializedName("title") val animeTitle: String,
)

data class MalListStatus(
    @SerializedName("status") val status: String?,
    @SerializedName("score") val score: Int? = null
)
