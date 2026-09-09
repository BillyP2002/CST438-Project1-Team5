package com.example.cst438_project1_team5.api

class malAPIRepository{
    suspend fun getList(name: String): Result<User>{
        return try{
            val response = RetrofitClient.api.getUser(name)
            val user = MalUser(name,
                showsWatched = response.data.map { entry ->
                    Show(
                        title = entry.node.anime_title
                        completedStatus = entry.node.status
                        score = entry.node.list_score
                    )
                Result.success(user)
        } catch(e: Exception){
            Result.failure(e)
        }
    }
}