package com.example.cst438_project1_team5.api

data class malUser(
    val name: String,
    val showsWatched: List<show>
)

//Didn't use anime_id as that is under development; title should be a unique identifier.
data class show(
    val title: String,
    val completedStatus: String,
    val userRating: String
)