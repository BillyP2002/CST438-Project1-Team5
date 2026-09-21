package com.example.cst438_project1_team5.api.malapi

data class MalUser(
    val name: String,
    val showsWatched: List<Show> = emptyList()
)

data class Show(val id: Int, val title: String, val completedStatus: String?, val score: Int? = null)
