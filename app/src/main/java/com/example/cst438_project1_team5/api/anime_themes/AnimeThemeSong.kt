package com.example.cst438_project1_team5.api.anime_themes

import com.google.gson.annotations.SerializedName

data class AnimeThemeSong(
    val id: Int,
    val basename: String,
    val filename: String,
    val path: String,
    val size: Int, // in bytes
    val mimetype: String, // media type
    val createdAt: String,
    val updatedAt: String,
    val deletedAt: String?,
    val link: String, // link to stream audio
    val viewsCount: Int, // number of views the resource got (for recs)
)

data class AnimeThemeSongsResponse(
    val songs: List<AnimeThemeSong>
)

data class AnimeThemeSongResponse(
    val song: AnimeThemeSong
)
