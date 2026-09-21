package com.example.cst438_project1_team5.api.malapi

import java.io.IOException
import retrofit2.HttpException

class MalApiRepository(private val accessToken: String) {
    suspend fun getList(): Result<MalUser> = try {
        val response = MalRetrofitClient.api.getUser(authorization = "Bearer $accessToken")
        val user = MalUser(
            name = "Me",
            showsWatched = response.data.map { entry ->
                Show(
                    id = entry.node.animeId,
                    title = entry.node.animeTitle,
                    completedStatus = entry.listStatus?.status,
                    score = entry.listStatus?.score
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
