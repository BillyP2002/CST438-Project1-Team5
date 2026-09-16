package com.example.cst438_project1_team5.api.malAPI

class malAPIRepository (private val accessToken : String) {
    suspend fun getList(name: String): Result<malUser> = try {
        val response = RetrofitClient.api.getUser(name, authorization = "Bearer $accessToken")
        val user = malUser(
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
