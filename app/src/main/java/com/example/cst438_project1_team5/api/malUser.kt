package com.example.cst438_project1_team5.api

data class malUser(
    val name: String,
    val showsWatched: List<Show> = emptyList()
)

// Didn't use anime_id as that is under development; title should be a unique identifier.
data class Show(
    val title: String,
    val completedStatus: String,
    val score: Int? = null
)