package com.example.cst438_project1_team5.api.anime_themes

import com.google.gson.annotations.SerializedName

data class AnimeThemeSong(
    val id: Int,
    val basename: String,
    val filename: String,
    val path: String,
    val size: Int, // in bytes
    val mimetype: String, // media type
    @SerializedName("created_at")
    val createdAt: String,
    @SerializedName("updated_at")
    val updatedAt: String,
    @SerializedName("deleted_at")
    val deletedAt: String?,
    val link: String, // link to stream audio
    @SerializedName("views_count")
    val viewsCount: Int, // number of views the resource got (for recs)
)

data class AnimeThemeSongsResponse(
    @SerializedName("audio")
    val songs: List<AnimeThemeSong>
)

data class AnimeThemeSongResponse(
    @SerializedName("audio")
    val song: AnimeThemeSong
)
