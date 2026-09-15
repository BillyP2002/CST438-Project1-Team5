package com.example.cst438_project1_team5.api.anime_themes

import com.google.gson.annotations.SerializedName
import java.util.Date

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
    val viewsCount: Int // number of views the resource got (for recs)
)

data class AnimeThemeSongsResponse(
    @SerializedName("audio")
    val songs: List<AnimeThemeSong>
)

data class AnimeThemeSongResponse(
    @SerializedName("audio")
    val song: AnimeThemeSong
)

// for video data
// video format
enum class Source {
    WEB, RAW, BD, DVD, VHS, LD
}

// The degree to which the sequence and episode
// content overlap.
enum class Overlap {
    None, Transition, Over
}

data class AnimeVideo(
    val id: Int,
    val basename: String,
    val filename: String,
    val path: String,
    val size: Int,
    val mimetype: String,
    val resolution: Int?,
    @SerializedName("no_credit")
    val nc: Boolean,
    val subbed: Boolean,
    val lyrics: Boolean,
    @SerializedName("uncensored")
    val uncen: Boolean, // necessary to have this be false (default is true)
    val source: Source,
    val overlap: Overlap,
    val tags: String,
    val link: String,
    val view_count: Int,
    val created_at: Date,
    val updated_at: Date,
    val deleted_at: Date,
)
