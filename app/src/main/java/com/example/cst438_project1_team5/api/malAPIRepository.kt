package com.example.cst438_project1_team5.api

class MalAPIRepository {
    suspend fun getList(name: String): Result<MalUser> = try {
        val response = RetrofitClient.api.getUser(name)
        val user = MalUser(
            name = name,
            showsWatched = response.data.map { entry ->
                Show(
                    title = entry.node.animeTitle,
                    completedStatus = entry.node.status,
                    score = entry.node.listScore
                )
            }
        )
        Result.success(user)
    } catch (e: Exception) {
        Result.failure(e)
    }
}
