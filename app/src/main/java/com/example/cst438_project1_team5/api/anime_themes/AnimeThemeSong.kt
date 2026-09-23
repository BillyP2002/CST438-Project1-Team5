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
    val viewsCount: Int, // number of views the resource got (for recs)
    val videos: List<AnimeVideo>? = null // only populated when include=videos is used
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
    val nc: Boolean,
    val subbed: Boolean,
    val lyrics: Boolean,
    val uncen: Boolean = false, // necessary to have this be false (default is true)
    val source: Source,
    val overlap: Overlap,
    val tags: String,
    val link: String,
    val views_count: Int,
    val created_at: Date,
    val updated_at: Date,
    val deleted_at: Date,
    val audio: AnimeThemeSong? = null,
    val animethemeentries: List<AnimeThemeEntry> = emptyList(),
)

data class AnimeVideosResponse (
    // The production API uses "videos" while an older response/test fixture
    // uses "video". Accept both so a valid live round is not parsed as empty.
    @SerializedName(value = "videos", alternate = ["video"])
    val videos: List<AnimeVideo> = emptyList()
)

data class AnimeVideoResponse (
    val video: AnimeVideo
)

//ResourceResponse, Resource, and Anime are involved in getting an anime by the MAL id
data class ResourceResponse(
    @SerializedName("resources")
    val resources: List<Resource>
)

data class Resource(
    @SerializedName("anime")
    val anime: List<Anime>
)

data class Anime(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("slug")
    val slug: String
)

/** The small part of the AnimeThemes relationship graph needed by the game. */
data class AnimeThemeEntry(
    val animetheme: AnimeTheme? = null
)

data class AnimeTheme(
    val anime: Anime? = null
)

data class AnimeSearchResponse(
    val search: AnimeSearchResults
)

data class AnimeSearchResults(
    val anime: List<Anime> = emptyList()
)