package com.example.cst438_project1_team5.api.malapi

import java.io.IOException
import retrofit2.HttpException

class MalApiRepository(private val accessToken: String) {
    suspend fun getList(name: String): Result<MalUser> = try {
        val response = MalRetrofitClient.api.getUser(name, authorization = "Bearer $accessToken")
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
    } catch (e: HttpException) {
        Result.failure(e)
    } catch (e: IOException) {
        Result.failure(e)
    }
}
